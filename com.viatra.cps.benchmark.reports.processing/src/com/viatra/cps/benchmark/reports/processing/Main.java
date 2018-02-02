package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.models.BenchmarkResult;
import com.viatra.cps.benchmark.reports.processing.models.CaseDescriptor;

public class Main {

	public static void main(String[] args) {
		//Processing processing = new Processing();
		//processing.process();
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<BenchmarkResult> result = mapper.readValue(new File("result.json"),new TypeReference<List<BenchmarkResult>>(){});
			result.forEach(element -> System.out.println(element.getPhaseResults().get(0).getMetrics().get(0).getValue()));
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}