package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.models.AggregataedResult;
import com.viatra.cps.benchmark.reports.processing.models.Configuration;
import com.viatra.cps.benchmark.reports.processing.models.Data;
import com.viatra.cps.benchmark.reports.processing.models.Tool;
import com.viatra.cps.benchmark.reports.processing.models.Plot;
import com.viatra.cps.benchmark.reports.processing.models.Result;
import com.viatra.cps.benchmark.reports.processing.utils.ListUtil;
import com.viatra.cps.benchmark.reports.processing.utils.Operation;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.JsonSerializer;
import eu.mondo.sam.core.results.MetricResult;

public class Processor {
	Plot plot;
	Data data;
	List<eu.mondo.sam.core.results.BenchmarkResult> benchmarkResults;
	ObjectMapper mapper;
	List<AggregataedResult> aggregataedResults;

	public Processor() {
		benchmarkResults = new ArrayList<>();
		aggregataedResults = new ArrayList<>();
		mapper = new ObjectMapper();
	}

	public void loadData(File data) throws JsonParseException, JsonMappingException, IOException {
		this.data = mapper.readValue(data, Data.class);
	}

	public void loadPlot(File plot) throws JsonParseException, JsonMappingException, IOException {
		this.plot = mapper.readValue(plot, Plot.class);
	}

	public void loadBenchmarkResults(File benchmarkResults)
			throws JsonParseException, JsonMappingException, IOException {
		this.benchmarkResults = mapper.readValue(benchmarkResults, new TypeReference<List<BenchmarkResult>>() {
		});
	}

	public void calculateAvarage() {
		data.getBenchmarks().forEach(benchmark -> {
			plot.getConfigs().forEach(config -> {
				AggregataedResult aggRes = new AggregataedResult();
				aggRes.setOperation("AVG");
				aggRes.setFunction(config.getTitle());
				List<String> tools = getTools(config.getLegendFilters(), benchmark.getTransformationTypes(),
						benchmark.getGeneratorTypes());
				List<Tool> ts = new ArrayList<>();
				tools.forEach(tool -> {
					Tool t = new Tool();
					t.setName(tool);
					List<Result> resList = new ArrayList<>();
					benchmark.getScales().forEach(scale -> {
						Result res = new Result();
						res.setSize(scale);
						List<BenchmarkResult> filteredResults = ListUtil
								.getBenchmarkResultBySizeAndTool(benchmarkResults, scale, tool);
						MetricResult metric = new MetricResult();
						metric.setName(config.getMetrics().get(0));
						metric.setValue(Operation
								.avg(filteredResults, config.getSummarizeFunction(), config.getMetrics(), scale)
								.toString());
						res.setMetrics(metric);
						resList.add(res);
						t.setResults(resList);
					});
					ts.add(t);
				});
				aggRes.setTool(ts);
				aggregataedResults.add(aggRes);
			});
		});
	}

	private List<String> getTools(List<String> legendFilters, List<String> transformationTypes,
			List<String> generatorTypes) {
		if (legendFilters.size() > 0) {
			return legendFilters;
		} else {
			List<String> tools = new ArrayList<>();
			transformationTypes.forEach(transformationType -> {
				generatorTypes.forEach(generatorType -> {
					tools.add(transformationType + "-" + generatorType);
				});
			});
			return tools;
		}
	}

	public void print(File out) throws JsonGenerationException, JsonMappingException, IOException {
		JsonSerializer ser = new JsonSerializer();
		JsonSerializer.setResultPath("");
		ser.serialize(benchmarkResults, "test");
	}
}
