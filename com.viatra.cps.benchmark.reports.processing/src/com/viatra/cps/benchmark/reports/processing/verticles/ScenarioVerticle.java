package com.viatra.cps.benchmark.reports.processing.verticles;

import java.nio.file.Path;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.AggregatorConfiguration;
import com.viatra.cps.benchmark.reports.processing.models.Diagrams;

import eu.mondo.sam.core.results.BenchmarkResult;
import io.vertx.core.AbstractVerticle;

public class ScenarioVerticle extends AbstractVerticle {

	public ScenarioVerticle(Diagrams diagramConfiguration, List<AggregatorConfiguration> configuration,
			Path outputResultsPath, String buildId, String caseName, String scenario, List<BenchmarkResult> results,
			ObjectMapper mapper) {
	}

}

/*
 * public void process(List<BenchmarkResult> results, String caseName, String
 * scenario) throws JsonParseException, JsonMappingException, IOException { if
 * (!Files.exists(Paths.get(this.resutOutputPath.toString(), this.buildId,
 * caseName, scenario))) {
 * Files.createDirectories(Paths.get(this.resutOutputPath.toString(),
 * this.buildId, caseName, scenario)); }
 * 
 * counter = this.configuration.size(); lock = new Object(); File resultJson =
 * Paths.get(this.resutOutputPath.toString(), this.buildId, caseName, scenario,
 * "results.json") .toFile(); File diagramJson = Paths
 * .get(this.resutOutputPath.toString(), this.buildId, caseName, scenario,
 * "diagram.config.json").toFile(); mapper.writeValue(diagramJson, new
 * Diagrams(this.buildId + "/" + caseName + "/" + scenario));
 * mapper.writeValue(resultJson, new Results(this.buildId + "/" + caseName + "/"
 * + scenario)); this.configuration.forEach(aggConfig -> { Operation last =
 * null; JSonSerializer tmp = new JSonSerializer(resultJson, diagramJson,
 * aggConfig.getID(), this.buildId + "/" + caseName + "/" + scenario,
 * this.diagramConfiguration, caseName, scenario, this.diagramSet,
 * this.dashboardConfigurationFile, this.buildId, this.mapper);
 * tmp.setProcessor(this); List<OperationConfig> opconf =
 * aggConfig.getOperations(false); for (OperationConfig opconfig : opconf) { if
 * (last == null) { last = OperationFactory.createOperation(tmp,
 * opconfig.getType(), opconfig.getFilter(), opconfig.getAttribute(),
 * aggConfig.getID()); } else { last = OperationFactory.createOperation(last,
 * opconfig.getType(), opconfig.getFilter(), opconfig.getAttribute(),
 * aggConfig.getID()); } } ; last.start(); List<BenchmarkResult> list =
 * results.subList(0, results.size()); for (BenchmarkResult res : list) {
 * last.addResult(res); } last.stop(); }); } }
 */