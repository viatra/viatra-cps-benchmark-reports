package com.viatra.cps.benchmark.reports.processing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.viatra.cps.benchmark.reports.processing.models.AggregatedPhaseResult;
import com.viatra.cps.benchmark.reports.processing.models.Configuration;
import com.viatra.cps.benchmark.reports.processing.models.Metric;
import com.viatra.cps.benchmark.reports.processing.models.PhaseResult;
import com.viatra.cps.benchmark.reports.processing.models.Result;
import com.viatra.cps.benchmark.reports.processing.utils.ListUtil;
import com.viatra.cps.benchmark.reports.processing.utils.Operation;
import com.viatra.cps.benchmark.reports.processing.utils.Wrapper;

public class Processing {
	private List<Result> results;
	private List<Configuration> configs;
	private JSONParser parser;
	private List<AggregatedPhaseResult> aggregatedPhaseResults;

	public Processing() {
		results = new ArrayList<>();
		configs = new ArrayList<>();
		parser = new JSONParser();
		aggregatedPhaseResults = new ArrayList<>();
		try {
			loadResults();
			loadConfig();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadConfig() throws FileNotFoundException, IOException, ParseException {
		Object con = parser.parse(new FileReader("config.json"));
		JSONArray plot = (JSONArray) ((JSONObject) con).get("Plot");
		Iterator<JSONObject> iteratorF = plot.iterator();
		while (iteratorF.hasNext()) {
			JSONObject function = iteratorF.next();
			this.configs.add(Wrapper.getConfigurationFromJson(function));
		}
	}

	private void loadResults() throws FileNotFoundException, IOException, ParseException {
		Object obj = parser.parse(new FileReader("result.json"));
		Iterator<JSONObject> iteratorRes = ((JSONArray) obj).iterator();
		while (iteratorRes.hasNext()) {
			JSONObject function = iteratorRes.next();
			this.results.add(Wrapper.getResultFromJson(function));
		}
	}

	public void process() {

		JSONArray resultArray = new JSONArray();
		AggregatedPhaseResult aResult = null;
		for (Configuration config : configs) {
			StringBuilder names = new StringBuilder();
			config.getSummarizeFunction().forEach(function -> names.append(function + " "));
			JSONObject obj = new JSONObject();
			obj.put("Functions", names.toString());
			int size = 1;
			JSONArray valueArray = new JSONArray();
			for (; size <= 64; size *= 2) {

				aResult = new AggregatedPhaseResult(names.toString(), -1,
						Arrays.asList(new Metric(config.getMetrics().get(0),
								Operation.avg(results, config.getSummarizeFunction(), config.getMetrics(), size))),
						"avg");
				JSONObject vRes = new JSONObject();
				vRes.put("size", size);
				vRes.put("Value", Wrapper.getJSONObjectFromAggregatedPhaseResult(aResult));
				valueArray.add(vRes);
			}
			obj.put("Result", valueArray);
			resultArray.add(obj);
		}
		System.out.println(resultArray);
		try (FileWriter file = new FileWriter("test.json")) {

			file.write(resultArray.toJSONString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
