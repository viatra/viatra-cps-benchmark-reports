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
		options.addOption("b", "build-name", true, "Build's name");
		options.addOption("r", "results", true, "Mondo-Sam result's path");
		options.addOption("c", "config", true, "AggregatedConfigouration's path");
		options.addOption("a", "aggregated-results", true, "AggregatedResults's path");
		options.addOption("bt", "build-template", true, "Build template");
		options.addOption("dt", "digram-config-template", true, "Diagram Config Template's path");
		options.addOption("bs", "builds", true, "Builds.json's path");
		options.addOption("dc", "diagram-config", true, "Diagram config's path");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
			String resultPath = cmd.getOptionValue("r");
			String configPath = cmd.getOptionValue("c");
			String aggresult = cmd.getOptionValue("a");
			String buildName = cmd.getOptionValue("b");
			String buildTemplate = cmd.getOptionValue("bt");
			String diagramTemplate = cmd.getOptionValue("dt");
			String builds = cmd.getOptionValue("bs");
			String diagramConfig = cmd.getOptionValue("dc");
			Processor process = new Processor(buildName, buildTemplate, diagramTemplate, diagramConfig, builds);
			try {
				process.loadBenchmarkResults(new File(configPath),
						aggresult + "/" + buildName + "/results.json");
				process.process(new File(resultPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}