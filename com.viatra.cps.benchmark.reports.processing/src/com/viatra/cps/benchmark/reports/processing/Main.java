package com.viatra.cps.benchmark.reports.processing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.viatra.cps.benchmark.reports.processing.models.Result;
import com.viatra.cps.benchmark.reports.processing.utils.Wrapper;

public class Main {

	public static void main(String[] args) {
		  JSONParser parser = new JSONParser();
		  try {
			Object obj = parser.parse(new FileReader("result.json"));
	        Result result = Wrapper.getResultFromJson((JSONObject)obj);
	        result.print();
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
