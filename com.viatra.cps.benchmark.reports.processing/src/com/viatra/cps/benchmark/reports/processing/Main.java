package com.viatra.cps.benchmark.reports.processing;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) {
		// create Options object
		Options options = new Options();

		// add t option
		options.addOption("r", "results", true, "Mondo-Sam result's path");
		options.addOption("c", "config", true, "AggregatedConfigouration's path");
		options.addOption("a", "aggresutl", true, "AggregatedResults's path");
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			String resultPath = cmd.getOptionValue("r");
			String configPath = cmd.getOptionValue("c");
			String aggresult = cmd.getOptionValue("a");
			
			Processor process = new Processor();
			process.loadBenchmarkResults(new File(configPath), aggresult);
			process.process(new File(resultPath));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}