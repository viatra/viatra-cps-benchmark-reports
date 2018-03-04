package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			Processor process = new Processor();
			process.loadBenchmarkResults(new File("result.json"));
			process.process();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}