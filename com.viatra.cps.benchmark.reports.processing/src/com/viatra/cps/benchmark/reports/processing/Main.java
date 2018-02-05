package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
			Processor process = new Processor();
			process.loadData(new File("data.json"));
			process.loadPlot(new File("config.json"));
			process.loadBenchmarkResults(new File("result.json"));
			process.calculateAvarage();
			process.print(new File("out.json"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}