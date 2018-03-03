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
import com.viatra.cps.benchmark.reports.processing.models.Data;
import com.viatra.cps.benchmark.reports.processing.models.OperationConfig;
import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.OperationFactory;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.Summary;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Processor {
	Data data;
	AggregatorConfiguration aggregatorConfiguration;
	ObjectMapper mapper;

	public Processor() {
		mapper = new ObjectMapper();
	}

	public void loadBenchmarkResults(File benchmarkResults)
			throws JsonParseException, JsonMappingException, IOException {
		this.aggregatorConfiguration = mapper.readValue(new File("aggregatorConfig.json"),
				AggregatorConfiguration.class);
		System.out.println(aggregatorConfiguration);
	}

	public void process() throws JsonParseException, JsonMappingException, IOException {
		Operation tmp = new Summary(null, null);
		OperationConfig opconf = aggregatorConfiguration.getFirts();
		Operation operation = OperationFactory.createOperation(tmp, opconf.getType(), opconf.getFilter(),
				opconf.getAttribute());
		operation.start();
		List<BenchmarkResult> benchmarkResults = mapper.readValue(new File("result.json"),
				new TypeReference<List<BenchmarkResult>>() {
				});
		benchmarkResults.forEach(res -> {
			operation.addResult(res);
		});
		operation.stop();
	}
}
