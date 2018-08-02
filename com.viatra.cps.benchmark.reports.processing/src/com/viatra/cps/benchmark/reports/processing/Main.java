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
		options.addOption("b", "build-name", true, "Build's name");
		options.addOption("i", "input_results", true, "input results path");
		options.addOption("p", "proecessing_config", true, "Processing Configouration");
		options.addOption("o", "output resuts", true, "Output results path");
		options.addOption("d", "diagram_template", true, "Diagram configuration template");
		options.addOption("v", "visualizer_config", true, "Visualizer configuration path");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;

		try {

			cmd = parser.parse(options, args);
			String buildId = cmd.getOptionValue("b");
			String resultInputPath = cmd.getOptionValue("i");
			String resultOutputPath = cmd.getOptionValue("o");
			String configPath = cmd.getOptionValue("p");
			String diagramConfigTemplatePath = cmd.getOptionValue("d");
			String visualizerConfigPath = cmd.getOptionValue("v");

			Processor process = new Processor(buildId, resultInputPath, resultOutputPath, configPath,
					diagramConfigTemplatePath, visualizerConfigPath);
			process.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}