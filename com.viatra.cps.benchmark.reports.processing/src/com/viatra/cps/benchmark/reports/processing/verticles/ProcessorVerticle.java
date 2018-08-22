package com.viatra.cps.benchmark.reports.processing.verticles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.models.AggregatorConfiguration;
import com.viatra.cps.benchmark.reports.processing.models.Diagrams;
import com.viatra.cps.benchmark.reports.processing.models.Message;

import eu.mondo.sam.core.results.BenchmarkResult;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

public class ProcessorVerticle extends AbstractVerticle {
	ObjectMapper mapper;
	Object lock;
	Boolean timeout;
	int counter;
	Path resultInputPath;
	Path resutOutputPath;
	List<AggregatorConfiguration> configuration;
	Diagrams diagramConfiguration;
	String buildId;
	File visualizerConfigurationFile;
	File dashboardConfigurationFile;
	File buildFile;
	Future<Void> future;
	EventBus eventBus;
	Integer numberOfCases = 0;
	Integer deployedcaseVerticles = 0;
	DeploymentOptions options;
	List<String> failedCaseVerticles;
	List<String> cases;

	public ProcessorVerticle(Future<Void> future, String buildId, String resultInputPath, String resultOutputPath,
			String configPath, String diagramConfigTemplatePath, String visualizerConfigPath) {
		options = new DeploymentOptions().setWorker(true);
		this.buildId = buildId;
		// Initialize objectmapper
		mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		// turn off autodetection
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, false);
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);

		failedCaseVerticles = new ArrayList<>();
		// Initialize future
		this.future = future;

		// Set input path
		this.resultInputPath = Paths.get(resultInputPath);

		// Set output path
		this.resutOutputPath = Paths.get(resultOutputPath);

		// Load configuration
		this.configuration = this.loadConfiguration(new File(configPath));

		// Load diagram configuration template
		this.diagramConfiguration = this.loadDiagramConfigurationTemplate(new File(diagramConfigTemplatePath));

		this.visualizerConfigurationFile = new File(visualizerConfigPath + "/config.json");
		this.dashboardConfigurationFile = new File(resultOutputPath + "/dashboard.json");
		this.buildFile = new File(resultOutputPath + "/builds.json");
		this.timeout = false;
		this.cases = new ArrayList<>();
	}

	private Diagrams loadDiagramConfigurationTemplate(File diagramConfigTemplate) {
		Diagrams diagrams;
		if (diagramConfigTemplate.exists()) {
			try {
				diagrams = mapper.readValue(diagramConfigTemplate, Diagrams.class);
			} catch (IOException e) {
				System.err.println(e.getMessage());
				diagrams = null;
			}
		} else {
			diagrams = null;
		}
		return diagrams;
	}

	private List<AggregatorConfiguration> loadConfiguration(File configFile) {
		List<AggregatorConfiguration> config;
		if (configFile.exists()) {
			try {
				config = this.mapper.readValue(configFile, new TypeReference<List<AggregatorConfiguration>>() {
				});
			} catch (IOException e) {
				System.err.println(e.getMessage());
				config = null;
			}
		} else {
			config = null;
		}
		return config;
	}

	private Boolean separateResults(Map<String, Map<String, List<BenchmarkResult>>> caseScenarioMap) {

		try (Stream<Path> paths = Files.walk(this.resultInputPath)) {
			paths.filter(Files::isRegularFile).forEach((path) -> {
				String extension = path.toFile().getName().substring(path.toFile().getName().lastIndexOf(".") + 1);
				// Checks if there is any extension after the last . in your input
				if (extension.isEmpty() || !extension.equals("json")) {
					System.out.println(path.toFile().getName() + " is not a json file");
				} else {
					try {
						BenchmarkResult result = mapper.readValue(path.toFile(), BenchmarkResult.class);
						Map<String, List<BenchmarkResult>> scenarioMap = caseScenarioMap
								.get(result.getCaseDescriptor().getCaseName());
						if (scenarioMap != null) {
							List<BenchmarkResult> benchmarkList = (List<BenchmarkResult>) scenarioMap
									.get(result.getCaseDescriptor().getScenario());
							if (benchmarkList != null) {
								benchmarkList.add(result);
							} else {
								benchmarkList = new ArrayList<>();
								benchmarkList.add(result);
								scenarioMap.put(result.getCaseDescriptor().getScenario(), benchmarkList);
							}
						} else {
							List<BenchmarkResult> resultList = new ArrayList<>();
							resultList.add(result);
							scenarioMap = new HashMap<>();
							scenarioMap.put(result.getCaseDescriptor().getScenario(), resultList);
							caseScenarioMap.put(result.getCaseDescriptor().getCaseName(), scenarioMap);
						}
					} catch (IOException e) {
						future.fail("Cannot parse benchmark result");
					}
				}
			});
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void start(Future<Void> startFuture) {
		// Check inputs
		this.eventBus = vertx.eventBus();
		if (!Files.exists(this.resultInputPath)) {
			startFuture.fail("Result input path is not exists");
		} else if (this.configuration == null) {
			startFuture.fail("Configuration not exists or invalid");
		} else if (this.diagramConfiguration == null) {
			startFuture.fail("Diagram configuration template not exists or invalid");
		} else {

			JsonUpdateVerticle jsonUpdateVerticle = new JsonUpdateVerticle(this.visualizerConfigurationFile,
					this.buildFile, this.dashboardConfigurationFile, this.mapper);

			vertx.deployVerticle(jsonUpdateVerticle, this.options, res -> {
				if (res.succeeded()) {
					Map<String, Map<String, List<BenchmarkResult>>> caseScenarioMap = new HashMap<>();
					if (this.separateResults(caseScenarioMap)) {
						Set<String> cases = caseScenarioMap.keySet();
						numberOfCases = cases.size();
						this.deployCasesVerticles(caseScenarioMap, cases, startFuture);
						this.waitForMessage();

					} else {
						startFuture.fail("Cannot load input results");
					}
				} else {
					future.fail(res.cause().getMessage());
				}
			});
		}
	}

	private void caseVerticleDeployed(AsyncResult<String> res, Future<Void> startFuture) {
		this.deployedcaseVerticles++;
		if (res.failed()) {
			this.failedCaseVerticles.add(res.cause().getMessage());
		}
		if (this.deployedcaseVerticles == this.numberOfCases) {
			if (this.failedCaseVerticles.size() > 0) {
				for (String message : this.failedCaseVerticles) {
					System.err.println(message);
				}
				startFuture.fail("Cannot deployed case verticles");
			} else {
				try {
					startFuture.complete();
					System.out.println("Start");
					for (String c : this.cases) {
						eventBus.send(c, mapper.writeValueAsString(new Message("Start", "")));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					future.fail("Cannot send start message");
				}
			}
		}
	}

	private void deployCasesVerticles(Map<String, Map<String, List<BenchmarkResult>>> caseScenarioMap,
			Set<String> cases, Future<Void> startFuture) {
		cases.forEach(caseName -> {
			this.cases.add(caseName);
			Map<String, List<BenchmarkResult>> scenairoMap = caseScenarioMap.get(caseName);
			CaseVerticle caseVerticle = new CaseVerticle(this.buildId, this.resutOutputPath, caseName, scenairoMap,
					mapper, this.configuration, this.diagramConfiguration, this.options);
			vertx.deployVerticle(caseVerticle, this.options, res -> {
				System.out.println("Case deployed " + caseName);
				this.caseVerticleDeployed(res, startFuture);
			});
		});
	}

	private void waitForMessage() {
		this.eventBus.consumer("Processor", handler -> {
			Message message;
			try {
				message = this.mapper.readValue(handler.body().toString(), Message.class);
				switch (message.getEvent()) {
				case "Successfull":
					System.out.println(message.getData());
					this.numberOfCases--;
					if (numberOfCases == 0) {
						this.done(false, "");
					}
					break;
				case "Failed":
					System.err.println(message.getData());
					this.numberOfCases--;
					if (numberOfCases == 0) {
						this.done(false, "");
					}
					break;
				default:
					System.out.println("Unexpected event: " + message.getEvent() + " - " + message.getData());
					break;
				}
			} catch (Exception e) {
				this.done(true, "Cannot parse message");
			}
		});
	}

	private void done(Boolean error, String message) {
		if (error) {
			this.future.fail("Cannot parse message");
		} else {
			try {
				eventBus.send("JsonUpdater", mapper.writeValueAsString(new Message("Save", "")), res -> {
					if (res.succeeded()) {
						future.complete();
					} else {
						future.fail(res.cause().getMessage());
					}
				});
			} catch (IOException e) {
				future.fail("Cannot send save message");
			}
		}
	}
}
