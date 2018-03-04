package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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

	public Processor() {
		mapper = new ObjectMapper();
	}

	public void loadBenchmarkResults(File benchmarkResults)
			throws JsonParseException, JsonMappingException, IOException {
		this.aggregatorConfiguration = mapper.readValue(new File("aggregatorConfig.json"),
				new TypeReference<List<AggregatorConfiguration>>() {
				});
	}

	public void process() throws JsonParseException, JsonMappingException, IOException {
		File file = new File("Aggresults.json");
		mapper.writeValue(file, new ArrayList<>());

		List<BenchmarkResult> benchmarkResults = mapper.readValue(new File("result.json"),
				new TypeReference<List<BenchmarkResult>>() {
				});

		this.aggregatorConfiguration.forEach(aggConfig -> {
			Operation last = null;
			Operation tmp = new JSonSerializer(file, aggConfig.getID(), aggConfig.getTitle());
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
