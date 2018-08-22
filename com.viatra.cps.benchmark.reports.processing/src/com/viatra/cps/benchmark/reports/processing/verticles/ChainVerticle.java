package com.viatra.cps.benchmark.reports.processing.verticles;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.models.AggregatorConfiguration;
import com.viatra.cps.benchmark.reports.processing.models.Data;
import com.viatra.cps.benchmark.reports.processing.models.Header;
import com.viatra.cps.benchmark.reports.processing.models.Message;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.OperationDescriptor;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.OperationFactory;
import eu.mondo.sam.core.results.BenchmarkResult;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

public class ChainVerticle extends AbstractVerticle {
	protected String operationId;
	protected String scenarioId;
	protected List<OperationDescriptor> descriptors;
	protected Integer chainSize;
	protected ObjectMapper mapper;
	protected Boolean error = false;
	protected String id;
	private DeploymentOptions options;

	public ChainVerticle(String operationId, String scenarioId, Integer index, List<OperationDescriptor> descriptors,
			ObjectMapper mapper, DeploymentOptions options) {
		this.operationId = operationId;
		this.scenarioId = scenarioId;
		this.descriptors = descriptors;
		this.chainSize = descriptors.size();
		this.id = scenarioId + "." + index;
		this.mapper = mapper;
		this.options = options;
	}

	@Override
	public void start(Future<Void> startFuture) {
		for (OperationDescriptor descriptor : this.descriptors) {
			Operation operation = OperationFactory.createOperation(descriptor, scenarioId, mapper);
			vertx.deployVerticle(operation, this.options, res -> {
				this.operationDeployed(startFuture, res.succeeded());
			});
		}
		vertx.eventBus().consumer(this.id, m -> {
			Message message;
			try {
				message = mapper.readValue(m.body().toString(), Message.class);
				switch (message.getEvent()) {
				case "Start":
					System.out.println("Chain started: " + this.id);
					List<BenchmarkResult> results = mapper.readValue(message.getData(),
							new TypeReference<List<BenchmarkResult>>() {
							});
					vertx.eventBus()
							.send(this.descriptors.get(0).getId(),
									mapper.writeValueAsString(new Message("Header",
											mapper.writeValueAsString(new Header(results.size(), this.operationId)))),
									res -> {
										if (res.succeeded()) {
											for (BenchmarkResult result : results) {
												try {
													vertx.eventBus().send(this.descriptors.get(0).getId(),
															mapper.writeValueAsString(
																	new Message("Result", mapper.writeValueAsString(
																			new Data(this.operationId, result)))));
												} catch (IOException e) {
													this.sendError(e);
												}
											}
										} else {
											this.sendError(new Exception(res.cause()));
										}
									});

					break;

				default:
					vertx.eventBus().send(this.scenarioId, m.body().toString());
				}
			} catch (IOException e) {
				this.sendError(e);
			}
		});

	}

	private void operationDeployed(Future<Void> startFuture, Boolean success) {
		if (!success) {
			this.error = false;
		}
		this.chainSize--;
		if (chainSize == 0) {
			if (this.error) {
				startFuture.fail("Cannot deploy all operation");
			} else {
				startFuture.complete();
			}
		}
	}

	protected void sendError(Exception e) {
		try {
			vertx.eventBus().send(this.scenarioId,
					mapper.writeValueAsString(new Message("Error", e.getMessage())));
		} catch (IOException e1) {
			vertx.eventBus().send(this.scenarioId, e.getMessage());
		}
	}
}
