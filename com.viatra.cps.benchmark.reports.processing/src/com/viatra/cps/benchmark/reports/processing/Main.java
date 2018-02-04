package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.models.BenchmarkResult;
import com.viatra.cps.benchmark.reports.processing.models.Plot;

public class Main {

	public static void main(String[] args) {
		// Processing processing = new Processing();
		// processing.process();
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<BenchmarkResult> result = mapper.readValue(new File("result.json"),
					new TypeReference<List<BenchmarkResult>>() {
					});
			Plot plot = mapper.readValue(new File("config.json"), Plot.class);
			plot.getConfigs().forEach(element -> System.out.println(element.getTitle()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}