package com.viatra.cps.benchmark.reports.processing.verticles;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.AggregatorConfiguration;
import com.viatra.cps.benchmark.reports.processing.models.Diagrams;
import com.viatra.cps.benchmark.reports.processing.models.Message;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.serializer.JSonSerializer;

import eu.mondo.sam.core.results.BenchmarkResult;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

public class CaseVerticle extends AbstractVerticle {
	private String buildId;
	private Path outputResultsPath;
	private String caseName;
	private Map<String, List<BenchmarkResult>> scenairoMap;
	private ObjectMapper mapper;
	private List<AggregatorConfiguration> configuration;
	private Diagrams diagramConfiguration;
	private Integer numberOfScenario = 0;
	private Integer deployedScenario = 0;
	private List<String> failedScenarioDeploy;
	private List<String> deployedScenarioVerticles;
	private Boolean failed = false;
	private DeploymentOptions options;

	public CaseVerticle(String buildId, Path outputResultsPath, String caseName,
			Map<String, List<BenchmarkResult>> scenairoMap, ObjectMapper mapper,
			List<AggregatorConfiguration> configuration, Diagrams diagramConfiguration, DeploymentOptions options) {
		this.buildId = buildId;
		this.outputResultsPath = outputResultsPath;
		this.caseName = caseName;
		this.scenairoMap = scenairoMap;
		this.mapper = mapper;
		this.configuration = configuration;
		this.diagramConfiguration = diagramConfiguration;
		this.options = options;
		this.failedScenarioDeploy = new ArrayList<String>();
		this.deployedScenarioVerticles = new ArrayList<>();
	}

	@Override
	public void start(Future<Void> startFuture) {
		Set<String> scenarios = this.scenairoMap.keySet();
		this.numberOfScenario = scenarios.size();
		scenarios.forEach(scenario -> {
			List<BenchmarkResult> results = this.scenairoMap.get(scenario);
			ScenarioVerticle scenarioVerticle = new ScenarioVerticle(this.diagramConfiguration, this.configuration,
					this.outputResultsPath, this.buildId, caseName, scenario, results, mapper, this.options);
			this.deployedScenarioVerticles.add(scenario);
			vertx.deployVerticle(scenarioVerticle, this.options, res -> {
				System.out.println("Scenario deployed: " + caseName + "." + scenario);
				this.scenarioVerticleDeployed(res, startFuture);
			});
		});
		vertx.eventBus().consumer(this.caseName, m -> {
			try {
				Message message = mapper.readValue(m.body().toString(), Message.class);
				switch (message.getEvent()) {
				case "Start":
					System.out.println("Case started: " + this.caseName);
					for (String scenario : this.deployedScenarioVerticles) {
						vertx.eventBus().send(caseName + "." + scenario,
								mapper.writeValueAsString(new Message("Start", "")));
					}
					break;
				case "Successfull":
					System.out.println(message.getData());
					this.numberOfScenario--;
					if (numberOfScenario == 0) {
						this.done();
					}
					break;
				case "Failed":
					System.err.println(message.getData());
					this.numberOfScenario--;
					if (numberOfScenario == 0) {
						this.failed = true;
						this.done();
					}
					break;
				default:
					vertx.eventBus().send("Processor", m.body().toString());
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				try {
					vertx.eventBus().send("Processor",
							mapper.writeValueAsString(new Message("Error", "Cannot parse message in " + caseName)));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					vertx.eventBus().send("Processor", "Cannot parse message in " + caseName);
				}
			}
		});
	}

	private void done() {
		try {
			if (this.failed) {
				vertx.eventBus().send("Processor", this.mapper.writeValueAsString(
						new Message("Failed", this.caseName + ": Failed to process one or more scenario")));
			} else {
				vertx.eventBus().send("Processor", this.mapper
						.writeValueAsString(new Message("Successfull", this.caseName + " processing successfull")));
			}
		} catch (IOException e) {
			try {
				vertx.eventBus().send("Processor",
						mapper.writeValueAsString(new Message("Cannot send message " + caseName, "")));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				vertx.eventBus().send("Processor", "Cannot send message" + caseName);
			}
		}
	}

	private void scenarioVerticleDeployed(AsyncResult<String> res, Future<Void> startFuture) {
		this.deployedScenario++;
		if (res.failed()) {
			this.failedScenarioDeploy.add(res.cause().getMessage());
		}
		if (this.deployedScenario == this.numberOfScenario) {
			if (this.failedScenarioDeploy.size() > 0) {
				for (String scenario : this.failedScenarioDeploy) {
					System.err.println(scenario);
				}
				startFuture.fail("Cannot deploy scenario verticles");
			} else {
				startFuture.complete();
			}
		}
	}
}
