package com.viatra.cps.benchmark.reports.processing;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.viatra.cps.benchmark.reports.processing.verticles.ProcessorVerticle;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.impl.FutureFactoryImpl;

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

			/*
			 * cmd = parser.parse(options, args); String buildId = cmd.getOptionValue("b");
			 * String resultInputPath = cmd.getOptionValue("i"); String resultOutputPath =
			 * cmd.getOptionValue("o"); String configPath = cmd.getOptionValue("p"); String
			 * diagramConfigTemplatePath = cmd.getOptionValue("d"); String
			 * visualizerConfigPath = cmd.getOptionValue("v");
			 */

			String buildId = "0.6.0-20180814.140246";
			String resultInputPath = "../results/0.6.0-20180814.1402461";
			String resultOutputPath = "../resultVisualizer/src/results";
			String configPath = "./config.json";
			String diagramConfigTemplatePath = "./diagramConfigTemplate.json";
			String visualizerConfigPath = "../resultVisualizer/src/config";

			Vertx vertx = Vertx.vertx();
			Future<Void> future = Future.future();
			ProcessorVerticle process = new ProcessorVerticle(future, buildId, resultInputPath, resultOutputPath,
					configPath, diagramConfigTemplatePath, visualizerConfigPath);
			vertx.deployVerticle(process, res -> {
				if (res.succeeded()) {
					System.out.println("Processing deployed");
					final String deploymentID = res.result();
					future.setHandler(result -> {
						if (result.succeeded()) {
							System.out.println("Proseccing successfull");
							done(deploymentID, vertx);
						} else {
							System.err.println(result.cause());
							done(deploymentID, vertx);
						}
					});
				} else {
					System.err.println(res.cause().getMessage());
					System.exit(1);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void done(String deploymentID, Vertx vertx) {
		vertx.undeploy(deploymentID, response -> {
			if (response.succeeded()) {
				System.out.println("Processing undeployed");
				System.exit(0);
			} else {
				System.err.println("Unexpected error while undeploying Processor:" + response.cause());
				System.exit(1);
			}
		});
	}
}