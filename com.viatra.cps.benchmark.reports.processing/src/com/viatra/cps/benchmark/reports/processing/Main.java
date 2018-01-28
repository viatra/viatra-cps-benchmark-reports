package com.viatra.cps.benchmark.reports.processing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.viatra.cps.benchmark.reports.processing.models.Configuration;
import com.viatra.cps.benchmark.reports.processing.models.Metric;
import com.viatra.cps.benchmark.reports.processing.models.PhaseResult;
import com.viatra.cps.benchmark.reports.processing.models.Result;
import com.viatra.cps.benchmark.reports.processing.utils.ListUtil;
import com.viatra.cps.benchmark.reports.processing.utils.Operation;
import com.viatra.cps.benchmark.reports.processing.utils.Wrapper;

public class Main {

	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("result.json"));

			Iterator<JSONObject> iteratorRes = ((JSONArray) obj).iterator();
			ArrayList<Result> results = new ArrayList<>();
			while (iteratorRes.hasNext()) {
				JSONObject function = iteratorRes.next();
				results.add(Wrapper.getResultFromJson(function));
			}

			Object con = parser.parse(new FileReader("config.json"));
			JSONArray plot = (JSONArray) ((JSONObject) con).get("Plot");
			ArrayList<Configuration> configList = new ArrayList<>();
			Iterator<JSONObject> iteratorF = plot.iterator();
			while (iteratorF.hasNext()) {
				JSONObject function = iteratorF.next();
				configList.add(Wrapper.getConfigurationFromJson(function));
			}
			
			List<PhaseResult> phaseResults = ListUtil.getPhaseListByName(results, configList.get(0).getSummarizeFunction().get(0));
			Metric m = ListUtil.getMetricByName(phaseResults.get(0).getMetrics(),"Memory");
				m.print();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}