package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.models.AggregatorConfiguration;
import com.viatra.cps.benchmark.reports.processing.models.OperationConfig;
import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.OperationFactory;
import com.viatra.cps.benchmark.reports.processing.operation.serializer.JSonSerializer;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Processor {
	List<AggregatorConfiguration> aggregatorConfiguration;
	ObjectMapper mapper;
	String aggResult;
	String buildName;

	public Processor(String buildName) {
		this.buildName = buildName;
		mapper = new ObjectMapper();
	}

	public void loadBenchmarkResults(File config, String aggResult)
			throws JsonParseException, JsonMappingException, IOException {
		this.aggResult = aggResult;
		this.aggregatorConfiguration = mapper.readValue(config, new TypeReference<List<AggregatorConfiguration>>() {
		});
	}

	public void updateBuildConfig() {
		File buildsJson = new File("../resultVisualizer/src/config/builds.json");
			List<String> builds;
			try {
				builds = mapper.readValue(buildsJson, new TypeReference<List<String>>() {});
				builds.add(this.buildName);
				mapper.writeValue(buildsJson, builds);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void process(File results) throws JsonParseException, JsonMappingException, IOException {
		File file = new File(aggResult);
		mapper.writeValue(file, new ArrayList<>());

		List<BenchmarkResult> benchmarkResults = mapper.readValue(results, new TypeReference<List<BenchmarkResult>>() {
		});

		this.aggregatorConfiguration.forEach(aggConfig -> {
			Operation last = null;
			Operation tmp = new JSonSerializer(file, aggConfig.getID());
			List<OperationConfig> opconf = aggConfig.getOperations(false);
			for (OperationConfig opconfig : opconf) {
				if (last == null) {
					last = OperationFactory.createOperation(tmp, opconfig.getType(), opconfig.getFilter(),
							opconfig.getAttribute());
				} else {
					last = OperationFactory.createOperation(last, opconfig.getType(), opconfig.getFilter(),
							opconfig.getAttribute());
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
	}
}
