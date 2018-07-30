package com.viatra.cps.benchmark.reports.processing;

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
import com.viatra.cps.benchmark.reports.processing.models.Builds;
import com.viatra.cps.benchmark.reports.processing.models.Case;
import com.viatra.cps.benchmark.reports.processing.models.VisualizerConfiguration;
import com.viatra.cps.benchmark.reports.processing.models.OperationConfig;
import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.OperationFactory;
import com.viatra.cps.benchmark.reports.processing.operation.serializer.JSonSerializer;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Processor {
	ObjectMapper mapper;
	Object lock;
	Boolean timeout;
	int counter;
	Path resultInputPath;
	Path resutOutputPath;
	List<AggregatorConfiguration> configuration;
	VisualizerConfiguration visualizerConfiguration;
	Diagrams diagramConfiguration;
	String buildId;

	List<Builds> builds;

	public Processor(String buildId, String resultInputPath, String resultOutputPath, String configPath,
			String diagramConfigTemplatePath, String visualizerConfigPath, String buildsPath) {

		this.buildId = buildId;
		// Initialize objectmapper
		mapper = new ObjectMapper();

		// Set input path
		this.resultInputPath = Paths.get(resultInputPath);

		// Set output path
		this.resutOutputPath = Paths.get(resultOutputPath);

		// Load configuration
		this.configuration = this.loadConfiguration(new File(configPath));

		// Load or create visualizer configuration
		this.visualizerConfiguration = this.loadVisualizerConfiguration(new File(visualizerConfigPath));

		// Load or create Builds.json
		this.builds = this.loadBuilds(new File(buildsPath+ "/builds.json"));

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

	private List<Builds> loadBuilds(File buildsJson) {
		List<Builds> builds;
		if (buildsJson.exists()) {
			try {
				builds = this.mapper.readValue(buildsJson, new TypeReference<List<Builds>>() {
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				builds = new ArrayList<>();
			}
		} else {
			builds = new ArrayList<>();
		}
		return builds;
	}

	private VisualizerConfiguration loadVisualizerConfiguration(File visualizerConfigurationFile) {
		VisualizerConfiguration visualizerConfiguration;
		if (visualizerConfigurationFile.exists()) {
			try {
				visualizerConfiguration = mapper.readValue(visualizerConfigurationFile, VisualizerConfiguration.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				visualizerConfiguration = new VisualizerConfiguration();
			}
		} else {
			visualizerConfiguration = new VisualizerConfiguration();
		}
		return visualizerConfiguration;
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

	public void start() {

		// Check inputs

		if (!Files.exists(this.resultInputPath)) {
			System.err.println("Result input path is not exists");
			System.exit(1);
		}

		if (this.configuration == null) {
			System.err.println("Configuration not exists or invalid");
			System.exit(1);
		}

		if (this.diagramConfiguration == null) {
			System.err.println("Diagram configuration template not exists or invalid");
			System.exit(1);
		}

		// Load and separate benchmark results
		Map<String, Map<String, List<BenchmarkResult>>> caseScenarioMap = new HashMap<>();

		try (Stream<Path> paths = Files.walk(this.resultInputPath)) {
			paths.filter(Files::isRegularFile).forEach((path) -> {
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
					e.printStackTrace();
					System.err.println("Cannot load all result");
					System.exit(3);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Cannot load input results");
			System.exit(2);
		}

		Set<String> cases = caseScenarioMap.keySet();
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
	}

	public void process(List<BenchmarkResult> results, String caseName, String scenario)
			throws JsonParseException, JsonMappingException, IOException {
		
		System.out.println("Start to process: " + this.buildId +"/"+caseName+"/"+scenario);
		if (!Files.exists(Paths.get(this.resutOutputPath.toString(), caseName, scenario))) {
			Files.createDirectories(Paths.get(this.resutOutputPath.toString(), caseName, scenario));
		}

		counter = this.configuration.size();
		lock = new Object();
		this.configuration.forEach(aggConfig -> {
			Operation last = null;
			JSonSerializer tmp = new JSonSerializer(
					Paths.get(this.resutOutputPath.toString(), caseName, scenario, "results.json").toFile(),
					aggConfig.getID());
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
	}

	public void chainEnd(String ID) {
		synchronized (lock) {
			counter--;
			System.out.println("Operation_" + ID + " done. " + counter + " operation left");
			lock.notify();
		}
	}
}
