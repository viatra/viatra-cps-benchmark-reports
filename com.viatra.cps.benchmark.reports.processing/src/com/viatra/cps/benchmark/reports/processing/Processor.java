package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.models.AggregataedResult;
import com.viatra.cps.benchmark.reports.processing.models.Data;
import com.viatra.cps.benchmark.reports.processing.models.Plot;
import com.viatra.cps.benchmark.reports.processing.models.Tool;
import com.viatra.cps.benchmark.reports.processing.models.Result;
import com.viatra.cps.benchmark.reports.processing.results.FilterableBenchmarkResult;
import com.viatra.cps.benchmark.reports.processing.results.FilterablePhaseResult;
import com.viatra.cps.benchmark.reports.processing.serializer.JsonSerializer;

import eu.mondo.sam.core.results.MetricResult;

public class Processor {
	Plot plot;
	Data data;
	List<FilterableBenchmarkResult> benchmarkResults;
	ObjectMapper mapper;
	List<AggregataedResult> aggregataedResults;

	public Processor() {
		benchmarkResults = new ArrayList<>();
		aggregataedResults = new ArrayList<>();
		mapper = new ObjectMapper();
		mapper.registerSubtypes(FilterableBenchmarkResult.class);
	}

	public void loadData(File data) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("Load data.json");
		this.data = mapper.readValue(data, Data.class);
		System.out.println("Done");
	}

	public void loadPlot(File plot) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("Load config.json");
		this.plot = mapper.readValue(plot, Plot.class);
		System.out.println("Done");
	}

	public void loadBenchmarkResults(File benchmarkResults)
			throws JsonParseException, JsonMappingException, IOException {
		System.out.println("Load result.json");
		this.benchmarkResults = mapper.readValue(benchmarkResults,
				new TypeReference<List<FilterableBenchmarkResult>>() {
				});
		System.out.println("Done");
	}

	public void process() {
		data.getBenchmarks().forEach(benchmark -> {
			plot.getConfigs().forEach(config -> {
				Double metricScale;
				if (config.getMetricScale() < 0) {
					metricScale = 1 / (Math.pow(10, -1 * config.getMetricScale()));
				} else {
					metricScale = Math.pow(10, config.getMetricScale());
				}
				// Initialize AggregataedResult
				AggregataedResult aggRes = new AggregataedResult("AVG", config.getxDimension(), config.getyLabel(),
						config.getTitle());

				// Get toolsNames
				List<String> toolNames = config.getLegendFilters().size() > 0 ? config.getLegendFilters()
						: benchmark.getToolNames();

				// Initialize tool List
				List<Tool> tools = new ArrayList<>();

				toolNames.forEach(toolName -> {
					// Initialize new Tool
					Tool tool = new Tool(toolName);

					// Initialize Result list
					List<Result> results = new ArrayList<>();

					benchmark.getScales().forEach(scale -> {
						// Initialize new Result
						Result result = new Result(scale);

						// Filter benchmarks
						List<FilterableBenchmarkResult> filteredBenchmarkResults = this
								.getBenchmarkResultBySizeAndTool(scale, toolName);

						DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();

						// Initialize metric result
						MetricResult metricResult = new MetricResult();
						metricResult.setName(config.getMetrics().get(0));
						filteredBenchmarkResults.forEach(filteredBenchmakResult -> {

							// Get phaseResult
							List<FilterablePhaseResult> filterablePhaseResults = filteredBenchmakResult
									.getPharesultByPhaseNames(config.getSummarizeFunction());
							Double sum = 0.0;
							// sum selected phasevalues
							for (int i = 0; i < filterablePhaseResults.size(); i++) {
								Double tmp = filterablePhaseResults.get(i).getMetricsByMetricName(Arrays.asList(config.getMetrics().get(0))).get(0).getValue();
								sum += (tmp * metricScale);
							}
							descriptiveStatistics.addValue(sum);
						});

						// Calculate average
						Double avg = descriptiveStatistics.getMean();
						if (Double.isNaN(avg)) {
							return;
						}

						metricResult.setValue(avg);
						result.setMetrics(metricResult);
						results.add(result);
						tool.setResults(results);
					});
					tools.add(tool);
				});
				aggRes.setTool(tools);
				aggregataedResults.add(aggRes);
			});
		});
	}

	private List<FilterableBenchmarkResult> getBenchmarkResultBySizeAndTool(Integer size, String tool) {
		return this.benchmarkResults.stream().filter(benchmarkResult -> {
			return benchmarkResult.getCaseDescriptor().getSize() == size
					&& benchmarkResult.getCaseDescriptor().getTool().equals(tool);
		}).collect(Collectors.toList());
	}

	public void print(String fileName) throws JsonGenerationException, JsonMappingException, IOException {
		System.out.println("Write results");
		JsonSerializer ser = new JsonSerializer();
		ser.serialize(aggregataedResults, fileName);
		System.out.println("done");
	}
}
