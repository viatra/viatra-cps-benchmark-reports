package com.viatra.cps.benchmark.reports.processing.utils;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.viatra.cps.benchmark.reports.processing.models.Case;
import com.viatra.cps.benchmark.reports.processing.models.Configuration;
import com.viatra.cps.benchmark.reports.processing.models.Metric;
import com.viatra.cps.benchmark.reports.processing.models.PhaseResult;
import com.viatra.cps.benchmark.reports.processing.models.Result;

public class Wrapper {

	/**
	 * Prase Json Object to result
	 * 
	 * @return new Result
	 */
	public static Result getResultFromJson(JSONObject obj) {
		JSONObject jCase = (JSONObject) obj.get("Case");
		Case caseObj = Wrapper.getCaseFromJson(jCase);
		ArrayList<PhaseResult> phaseResults = new ArrayList<>();
		JSONArray phaseResult = (JSONArray) obj.get("PhaseResults");
		Iterator<JSONObject> iterator = phaseResult.iterator();
		while (iterator.hasNext()) {
			JSONObject phaseRes = iterator.next();
			phaseResults.add(Wrapper.getPhaseResultFromJson(phaseRes));
		}
		return new Result(caseObj, phaseResults);
	}

	/**
	 * Parse json object to case
	 * 
	 * @return new Case
	 */
	public static Case getCaseFromJson(JSONObject obj) {
		String caseName = (String) obj.get("CaseName");
		Integer runIndex = ((Long) obj.get("RunIndex")).intValue();
		String scenario = (String) obj.get("Scenario");
		String tool = (String) obj.get("Tool");
		Integer size = ((Long) obj.get("Size")).intValue();
		return new Case(caseName, runIndex, scenario, tool, size);
	}

	/**
	 * Parse json object to phaseResult
	 * 
	 * @return new PhaseReult
	 */
	public static PhaseResult getPhaseResultFromJson(JSONObject obj) {
		String phaseName = (String) obj.get("PhaseName");
		Integer sequence = Integer.parseInt((String) obj.get("Sequence"));
		ArrayList<Metric> metrics = new ArrayList<>();
		JSONArray metric = (JSONArray) obj.get("Metrics");
		Iterator<JSONObject> iterator = metric.iterator();
		while (iterator.hasNext()) {
			JSONObject metricElement = iterator.next();
			metrics.add(Wrapper.getPhaseMetricFromJson(metricElement));
		}
		return new PhaseResult(phaseName, sequence, metrics);

	}

	/**
	 * Parse json object to metric
	 * 
	 * @return new Metric
	 */
	public static Metric getPhaseMetricFromJson(JSONObject obj) {
		String name = (String) obj.get("MetricName");
		Long value = Long.parseLong((String) obj.get("MetricValue"));
		return new Metric(name, value);
	}

	/**
	 * Parse json object to configuration
	 * 
	 * @return newConfiguration
	 */
	public static Configuration getConfigurationFromJson(JSONObject obj) {
		String xDimension = (String) obj.get("X_Dimension");
		String legend = (String) obj.get("Legend");
		ArrayList<String> summarizeFunction = new ArrayList<>();
		JSONArray functions = (JSONArray) obj.get("Summarize_Function");
		Iterator<String> iteratorF = functions.iterator();
		while (iteratorF.hasNext()) {
			String function = iteratorF.next();
			summarizeFunction.add(function);
		}
		String title = (String) obj.get("Title");
		JSONArray metrics = (JSONArray) obj.get("Metrics");
		Iterator<String> iteratorM = metrics.iterator();
		while (iteratorM.hasNext()) {
			String metricElement = iteratorM.next();
			summarizeFunction.add(metricElement);
		}
		Integer metricScale = ((Long) obj.get("Metric_Scale")).intValue();
		Long minIterationL = ((Long) obj.get("Min_Iteration"));
		Integer minIteration = 0;
		if (minIterationL != null) {
			minIteration = minIterationL.intValue();
		}
		Long maxIterationL = ((Long) obj.get("Max_Iteration"));
		Integer maxIteration = 0;
		if (maxIterationL != null) {
			maxIteration = maxIterationL.intValue();
		}
		String yLabel = (String) obj.get("Y_Label");
		String yAxisScale = (String) obj.get("X_Axis_Scale");
		String xAxisScale = (String) obj.get("Y_Axis_Scale");
		return new Configuration(xDimension, legend, summarizeFunction, title, metricScale, minIteration, maxIteration,
				yLabel, xAxisScale, yAxisScale);
	}
}
