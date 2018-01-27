package com.viatra.cps.benchmark.reports.processing.utils;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.viatra.cps.benchmark.reports.processing.models.Case;
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
	 * parse Result to json object
	 * 
	 * @param result
	 *            json object
	 */

	public static void getJsonFromResult(Result result) {
		// TODO change return Type to Json, create Json object from result object
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
	 * parse Case to json object
	 * 
	 * @return case json object
	 */
	public static void getJsonFromCase(Case caseObject) {
		// TODO change return Type to Json, create Json object from case object
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
	 * parse PhaseReult to json object
	 * 
	 * @return PhaseReult json object
	 */
	public static void getJsonFromPhaseReult(PhaseResult phaseResult) {
		// TODO change return Type to Json, create Json object from case object
	}

	/**
	 * Parse json object to metric
	 * 
	 * @return new Metric
	 */
	public static Metric getPhaseMetricFromJson(JSONObject obj) {
		String name = (String) obj.get("MetricName");
		Integer value = Integer.parseInt((String) obj.get("MetricValue"));
		return new Metric(name, value);
	}

	/**
	 * parse PhaseReult to json object
	 * 
	 * @return PhaseReult json object
	 */
	public static void getJsonFrommetric(Metric phaseResult) {
		// TODO change return Type to Json, create Json object from case object
	}
}
