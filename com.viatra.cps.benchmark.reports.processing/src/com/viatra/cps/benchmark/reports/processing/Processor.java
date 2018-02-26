package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.AggregatorConfiguration;
import com.viatra.cps.benchmark.reports.processing.models.Data;
import com.viatra.cps.benchmark.reports.processing.models.OperationConfig;
import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.OperationFactory;
import com.viatra.cps.benchmark.reports.processing.operation.OperationType;


public class Processor {
	Data data;
	AggregatorConfiguration aggregatorConfiguration;
	ObjectMapper mapper;
	public Processor() {
		mapper = new ObjectMapper();
	} 
	
	public void loadBenchmarkResults(File benchmarkResults)
			throws JsonParseException, JsonMappingException, IOException {
		this.aggregatorConfiguration = mapper.readValue(new File("aggregatorConfig.json"), AggregatorConfiguration.class);
		System.out.println(aggregatorConfiguration);
	}

	public void process() {
		List<Operation> operationChain = new ArrayList<Operation>();
		for(OperationConfig op : aggregatorConfiguration.getOperations(true)) {
			OperationType operationType = null;
			switch (op.getType()) {
			case "Mean":
				operationType = OperationType.Mean;
				break;
			case "Average":
				operationType = OperationType.Average;
				break;
			case "Summary":
				operationType = OperationType.Summary;
			default:
				break;
			}
			operationChain.add(OperationFactory.createOperation(operationType, op.getFilter(), op.getAttribute()));
		}
		for(Operation oper : operationChain) {
			oper.calculate(null);
		}
	}
}
