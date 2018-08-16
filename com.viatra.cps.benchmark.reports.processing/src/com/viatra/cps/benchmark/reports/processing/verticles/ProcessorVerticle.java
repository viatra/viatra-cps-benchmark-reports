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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.models.AggregatorConfiguration;
import com.viatra.cps.benchmark.reports.processing.models.Diagrams;
import com.viatra.cps.benchmark.reports.processing.models.Message;
import com.viatra.cps.benchmark.reports.processing.models.Builds;
import com.viatra.cps.benchmark.reports.processing.models.Case;
import com.viatra.cps.benchmark.reports.processing.models.DiagramSet;
import com.viatra.cps.benchmark.reports.processing.models.VisualizerConfiguration;
import com.viatra.cps.benchmark.reports.processing.models.OperationConfig;
import com.viatra.cps.benchmark.reports.processing.models.Results;
import com.viatra.cps.benchmark.reports.processing.models.Scale;
import com.viatra.cps.benchmark.reports.processing.models.ToolColor;
import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.OperationFactory;
import com.viatra.cps.benchmark.reports.processing.operation.serializer.JSonSerializer;

import eu.mondo.sam.core.results.BenchmarkResult;
import io.vertx.core.AbstractVerticle;
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
	Future<Void> future;
	EventBus eventBus;
	Integer numberOfCases = 0;
	List<String> caseIds;

	public ProcessorVerticle(Future<Void> future, String buildId, String resultInputPath, String resultOutputPath,
			String configPath, String diagramConfigTemplatePath, String visualizerConfigPath) {
		this.caseIds = new ArrayList<>();
		this.buildId = buildId;
		// Initialize objectmapper
		mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		// turn off autodetection
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, false);
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);

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


		this.timeout = false;
	}



	private Diagrams loadDiagramConfigurationTemplate(File diagramConfigTemplate) {
		Diagrams diagrams;
		if (diagramConfigTemplate.exists()) {
			try {
				diagrams = mapper.readValue(diagramConfigTemplate, Diagrams.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
				config = null;
			}
		} else {
			config = null;
		}
		return config;
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
		} else if (this.visualizerConfiguration == null) {
			startFuture.fail("Visualizer configuration is invalid");
		} else if (this.diagramSet == null) {
			startFuture.fail("Dashboard configuration is invalid");
		} else {
			
			JsonUpdateVerticle jsonUpdateVerticle = new JsonUpdateVerticle();
			
			// Load and separate benchmark results
			Map<String, Map<String, List<BenchmarkResult>>> caseScenarioMap = new HashMap<>();

			try (Stream<Path> paths = Files.walk(this.resultInputPath)) {
				paths.filter(Files::isRegularFile).forEach((path) -> {
					String extension = path.toFile().getName().substring(path.toFile().getName().lastIndexOf(".") + 1);
					// Checks if there is any extension after the last . in your input
					if (extension.isEmpty() || !extension.equals("json")) {
						System.out.println(path.toFile().getName() + " is not a json file");
					} else {
						try {
							BenchmarkResult result = mapper.readValue(path.toFile(), BenchmarkResult.class);
							this.updateVisualizerConfig(result);
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
							startFuture.fail("Cannot load input results");
						}
					}
				});
			} catch (Exception e) {
				startFuture.fail("Cannot load input results");
			}

			Set<String> cases = caseScenarioMap.keySet();
			numberOfCases = cases.size();
			cases.forEach(caseName -> {
				Map<String, List<BenchmarkResult>> scenairoMap = caseScenarioMap.get(caseName);
				Set<String> scenarios = scenairoMap.keySet();
				scenarios.forEach(scenario -> {
					List<BenchmarkResult> results = scenairoMap.get(scenario);
					try {
						this.process(results, caseName, scenario);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			});
			
			
			
			
			
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
						System.out.println("Unexpected message: " + message.getEvent() + " - " + message.getData());
						break;
					}
				} catch (Exception e) {
					this.done(true, "Cannot parse message");
				}
			});
			startFuture.complete();
		}
	}

	private void done(Boolean error, String message) {
		if (error) {
			this.future.fail("Cannot parse message");
		} else {
			try {
				mapper.writeValue(this.visualizerConfigurationFile, this.visualizerConfiguration);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				this.future.fail("Cannot update visualizes configuration: " + e.getMessage());
			}
			this.future.complete();
		}
	}

	public void updateVisualizerConfig(BenchmarkResult result) {
		result.getPhaseResults().forEach(phaseResult -> phaseResult.getMetrics().forEach(metric -> {
			if (!this.visualizerConfiguration.getScales().stream()
					.filter(scale -> scale.getMetric().equals(metric.getName())).findFirst().isPresent()) {
				this.visualizerConfiguration.getScales().add(new Scale(metric.getName()));
			}
		}));

		if (this.visualizerConfiguration.getToolColors().size() == 0) {
			this.visualizerConfiguration.getToolColors().add(new ToolColor(result.getCaseDescriptor().getTool()));
		} else if (!this.visualizerConfiguration.getToolColors().stream()
				.filter(tool -> tool.getToolName().equals(result.getCaseDescriptor().getTool())).findFirst()
				.isPresent()) {
			this.visualizerConfiguration.getToolColors().add(new ToolColor(result.getCaseDescriptor().getTool()));
		}
	}

	public void process(List<BenchmarkResult> results, String caseName, String scenario)
			throws JsonParseException, JsonMappingException, IOException {
		if (!Files.exists(Paths.get(this.resutOutputPath.toString(), this.buildId, caseName, scenario))) {
			Files.createDirectories(Paths.get(this.resutOutputPath.toString(), this.buildId, caseName, scenario));
		}

		counter = this.configuration.size();
		lock = new Object();
		File resultJson = Paths.get(this.resutOutputPath.toString(), this.buildId, caseName, scenario, "results.json")
				.toFile();
		File diagramJson = Paths
				.get(this.resutOutputPath.toString(), this.buildId, caseName, scenario, "diagram.config.json").toFile();
		mapper.writeValue(diagramJson, new Diagrams(this.buildId + "/" + caseName + "/" + scenario));
		mapper.writeValue(resultJson, new Results(this.buildId + "/" + caseName + "/" + scenario));
		this.configuration.forEach(aggConfig -> {
			Operation last = null;
			JSonSerializer tmp = new JSonSerializer(resultJson, diagramJson, aggConfig.getID(),
					this.buildId + "/" + caseName + "/" + scenario, this.diagramConfiguration, caseName, scenario,
					this.diagramSet, this.dashboardConfigurationFile, this.buildId, this.mapper);
			tmp.setProcessor(this);
			List<OperationConfig> opconf = aggConfig.getOperations(false);
			for (OperationConfig opconfig : opconf) {
				if (last == null) {
					last = OperationFactory.createOperation(tmp, opconfig.getType(), opconfig.getFilter(),
							opconfig.getAttribute(), aggConfig.getID());
				} else {
					last = OperationFactory.createOperation(last, opconfig.getType(), opconfig.getFilter(),
							opconfig.getAttribute(), aggConfig.getID());
				}
			}
			;
			last.start();
			List<BenchmarkResult> list = results.subList(0, results.size());
			for (BenchmarkResult res : list) {
				last.addResult(res);
			}
			last.stop();
		});

		while (counter > 0 && !this.timeout) {
			int tmp = counter;
			synchronized (lock) {
				try {
					lock.wait(10000);
					if (tmp == counter) {
						this.timeout = true;
						System.err.println(this.buildId + "/" + caseName + "/" + scenario + "/" + counter + " timeout");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.updateBuilds(caseName, scenario);
	}

	public void updateBuilds(String caseName, String scenario) {
		if (this.builds.size() > 0) {
			Builds build = null;
			Optional<Builds> option = this.builds.stream().filter(b -> b.getBuildId().equals(this.buildId)).findFirst();
			if (option.isPresent()) {
				build = option.get();
			}
			if (build != null) {
				Case caseElement = null;
				Optional<Case> optional = build.getCases().stream().filter(c -> c.getCaseName().equals(caseName))
						.findFirst();
				if (optional.isPresent()) {
					caseElement = optional.get();
				}
				if (caseElement != null) {
					if (!caseElement.getScenarios().stream().filter(s -> s.equals(scenario)).findFirst().isPresent()) {
						if (caseElement.getScenarios().size() == 0) {
							List<String> scenarios = new ArrayList<>();
							scenarios.add(scenario);
							caseElement.setScenarios(scenarios);
						} else {
							caseElement.getScenarios().add(scenario);
						}
					}
				} else {
					List<String> scenarios = new ArrayList<>();
					scenarios.add(scenario);
					caseElement = new Case();
					caseElement.setCaseName(caseName);
					caseElement.setScenarios(scenarios);

					if (build.getCases().size() == 0) {
						build.setCases(new ArrayList<>());
					}
					build.getCases().add(caseElement);
				}
			} else {
				List<String> scenarios = new ArrayList<>();
				scenarios.add(scenario);
				Case caseElement = new Case();
				caseElement.setCaseName(caseName);
				caseElement.setScenarios(scenarios);
				build = new Builds();
				build.setBuildId(this.buildId);
				build.setCases(new ArrayList<>());
				build.getCases().add(caseElement);
				this.builds.add(build);
			}
		} else {
			List<String> scenarios = new ArrayList<>();
			scenarios.add(scenario);
			Case caseElement = new Case();
			caseElement.setCaseName(caseName);
			caseElement.setScenarios(scenarios);
			Builds build = new Builds();
			build.setBuildId(this.buildId);
			build.setCases(new ArrayList<>());
			build.getCases().add(caseElement);
			this.builds.add(build);
		}

		try {
			mapper.writeValue(new File(this.resutOutputPath.toString() + "/builds.json"), this.builds);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void chainEnd(String ID) {
		synchronized (lock) {
			counter--;
			System.out.println("Operation_" + ID + " done. " + counter + " operation left");
			lock.notify();
		}
	}

	@Override
	public void stop(Future<Void> stopFuture) {
		this.caseIds.forEach(id -> {
			vertx.undeploy(id, res -> {
				if (res.failed()) {
					stopFuture.fail(res.cause());
				}
			});
		});
		stopFuture.complete();
	}
}
