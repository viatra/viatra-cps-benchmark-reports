package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.models.AggregatorConfiguration;
import com.viatra.cps.benchmark.reports.processing.models.Build;
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
	String builds;
	String diagramConfig;
	Boolean updateDiagConfig;
	Boolean timeout;
	int counter;

	public Processor(String buildName, String buildTemplate, String digramTemplate, String diagramConfig, String builds,
			Boolean updateDiagConfig) {
		this.buildName = buildName;
		this.buildTemplate = buildTemplate;
		this.digramTemplate = digramTemplate;
		this.diagramConfig = diagramConfig;
		this.builds = builds;
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

	public Integer updateBuildConfig() {
		File buildsJson = new File(this.builds);
		List<String> builds;
		try {
			if (buildsJson.exists()) {
				builds = mapper.readValue(buildsJson, new TypeReference<List<String>>() {
				});
			} else {
				builds = new ArrayList<>();
			}
			if (!builds.stream().filter(build -> build.equals(buildName)).findFirst().isPresent()) {
				builds.add(this.buildName);
				mapper.writeValue(buildsJson, builds);
			}
			return builds.size();
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
			Integer buildId = updateBuildConfig();
			File diagramConfigJson = new File(this.diagramConfig);
			File buildTemplate = new File(this.buildTemplate);
			Build build = mapper.readValue(buildTemplate, Build.class);
			build.setId(buildId);
			build.setName(buildName);
			DiagramConfig diagramConfig;
			if (!diagramConfigJson.exists() || this.updateDiagConfig) {
				File digramConfigTemplate = new File(this.digramTemplate);
				if (!diagramConfigJson.exists()) {
					diagramConfig = mapper.readValue(digramConfigTemplate, DiagramConfig.class);
				} else {
					diagramConfig = mapper.readValue(diagramConfigJson, DiagramConfig.class);
					DiagramConfig tmp = mapper.readValue(digramConfigTemplate, DiagramConfig.class);
					diagramConfig.setToolColors(tmp.getToolColors());
					mapper.writeValue(diagramConfigJson, diagramConfig);
				}
			} else {
				diagramConfig = mapper.readValue(diagramConfigJson, DiagramConfig.class);
			}

			if (!diagramConfig.getResultConfig().getBuilds().stream().filter(savedBuild -> {
				return savedBuild.getName().equals(buildName);
			}).findFirst().isPresent()) {
				diagramConfig.getResultConfig().getBuilds().add(build);
				mapper.writeValue(diagramConfigJson, diagramConfig);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void process(File results) throws JsonParseException, JsonMappingException, IOException {

		if (!new File(this.aggResult).exists()) {
			Files.createDirectories(Paths.get(this.aggResult));
		}
		File file = new File(this.aggResult + "/results.json");
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
							opconfig.getAttribute(),aggConfig.getID());
				} else {
					last = OperationFactory.createOperation(last, opconfig.getType(), opconfig.getFilter(),
							opconfig.getAttribute(),aggConfig.getID());
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
					if(tmp == counter) {
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
