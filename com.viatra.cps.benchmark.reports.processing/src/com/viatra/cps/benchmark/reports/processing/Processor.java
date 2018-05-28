package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.models.AggregatorConfiguration;
import com.viatra.cps.benchmark.reports.processing.models.Build;
import com.viatra.cps.benchmark.reports.processing.models.Case;
import com.viatra.cps.benchmark.reports.processing.models.DiagramConfig;
import com.viatra.cps.benchmark.reports.processing.models.OperationConfig;
import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.OperationFactory;
import com.viatra.cps.benchmark.reports.processing.operation.serializer.JSonSerializer;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Processor {
	List<AggregatorConfiguration> aggregatorConfiguration;
	ObjectMapper mapper;
	Object lock;
	String aggResult;
	String buildName;
	String buildTemplate;
	String digramTemplate;
	String diagramConfig;
	Boolean updateDiagConfig;
	Boolean timeout;
	String caseName;
	int counter;

	public Processor(String buildName, String buildTemplate, String digramTemplate, String diagramConfig,
			Boolean updateDiagConfig, String caseName) {
		this.buildName = buildName;
		this.buildTemplate = buildTemplate;
		this.digramTemplate = digramTemplate;
		this.diagramConfig = diagramConfig;
		this.caseName = caseName;
		this.updateDiagConfig = updateDiagConfig;
		this.timeout = false;
		mapper = new ObjectMapper();
	}

	public void loadBenchmarkResults(File config, String aggResult)
			throws JsonParseException, JsonMappingException, IOException {
		this.aggResult = aggResult;
		this.aggregatorConfiguration = mapper.readValue(config, new TypeReference<List<AggregatorConfiguration>>() {
		});
	}

	public String updateBuildConfig() {
		File buildsJson = new File("builds.json");
		List<Case> cases;
		String id;
		try {
			if (buildsJson.exists()) {
				cases = mapper.readValue(buildsJson, new TypeReference<List<Case>>() {
				});
			} else {
				cases = new ArrayList<>();
			}
			Optional<Case> c = cases.stream().filter(tmp -> tmp.getCaseName().equals(this.caseName)).findFirst();
			if (c.isPresent()) {
				List<String> builds = c.get().getBuilds();
				if (!builds.stream().filter(build -> build.equals(buildName)).findFirst().isPresent()) {
					builds.add(this.buildName);
				}
				id = this.caseName + "_" + c.get().getBuilds().size();
			} else {
				Case newCase = new Case();
				List<String> buildsList = new ArrayList<>();
				buildsList.add(this.buildName);
				newCase.setBuilds(buildsList);
				newCase.setCaseName(this.caseName);
				cases.add(newCase);
				id = this.caseName + "_" + 1;
			}
			mapper.writeValue(buildsJson, cases);
			return id;
		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void updateDiagramConfig() {
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		// turn off autodetection
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, false);
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);
		try {
			String buildId = updateBuildConfig();

			// Updated diagram configuration
			File diagramConfigJson = new File(this.digramTemplate);
			DiagramConfig diagramConfig = mapper.readValue(diagramConfigJson, DiagramConfig.class);
			File newConfig = new File(this.diagramConfig);
			if (!newConfig.exists()) {
				Files.createDirectories(Paths.get(this.diagramConfig).getParent());
			}
			mapper.writeValue(newConfig, diagramConfig);

			// Save new build config
			File buildTemplate = new File(this.buildTemplate);
			Build build = mapper.readValue(buildTemplate, Build.class);
			build.setId(buildId);
			build.setName(buildName);
			Files.createDirectories(Paths.get(this.caseName + "/" + this.buildName));
			File resultsJson = new File(this.caseName + "/" + this.buildName + "/build.config.json");
			mapper.writeValue(resultsJson, build);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void process(File results) throws JsonParseException, JsonMappingException, IOException {
		File file = new File(this.caseName + "/" + this.buildName + "/results.json");
		if (!file.exists()) {
			Files.createDirectories(Paths.get(this.caseName + "/" + this.buildName));
		}

		mapper.writeValue(file, new ArrayList<>());

		List<BenchmarkResult> benchmarkResults = mapper.readValue(results, new TypeReference<List<BenchmarkResult>>() {
		});
		counter = aggregatorConfiguration.size();
		lock = new Object();
		this.aggregatorConfiguration.forEach(aggConfig -> {
			Operation last = null;
			JSonSerializer tmp = new JSonSerializer(file, aggConfig.getID());
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
			List<BenchmarkResult> list = benchmarkResults.subList(0, benchmarkResults.size());
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
						System.err.println(this.buildName + " timeout");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		updateDiagramConfig();
	}

	public void chainEnd(String ID) {
		synchronized (lock) {
			counter--;
			System.out.println("Operation_" + ID + " done. " + counter + " operation left");
			lock.notify();
		}
	}
}
