package com.viatra.cps.benchmark.reports.processing.verticles.operation;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.Message;

import eu.mondo.sam.core.results.BenchmarkResult;

public abstract class Operation extends AbstractVerticle {
	protected String next;
	protected String scenario;
	protected ObjectMapper mapper;
	protected Integer numberOfResults = 0;
	protected String ID;

	/**
	 * Add one BenchmarkResult to operation
	 * 
	 * @param result
	 */
	public abstract void addResult(BenchmarkResult result);

	@Override
	public void start(Future<Void> startFuture) {
		vertx.eventBus().consumer(this.ID, m -> {
			try {
				Message message = mapper.readValue(m.body().toString(), Message.class);
				switch (message.getEvent()) {
				case "Results-size":
					this.resultsSizeReceived(message, m);
				case "Result":
					try {
						BenchmarkResult result = mapper.readValue(message.getData().toString(), BenchmarkResult.class);
						this.addResult(result);
					} catch (Exception e) {
						vertx.eventBus().send(this.scenario, mapper
								.writeValueAsString(new Message("Error", "Cannot parse message data in " + this.ID)));
					}
					break;
				default:
					vertx.eventBus().send(this.scenario, m);
					break;
				}
			} catch (IOException e) {
				try {
					vertx.eventBus().send(this.scenario,
							mapper.writeValueAsString(new Message("Error", "Cannot parse message in " + this.ID)));
				} catch (IOException e1) {
					vertx.eventBus().send("Processor", "Cannot parse message in " + this.ID);
				}
			}
		});
		startFuture.complete();
	}

	protected void resultsSizeReceived(Message message, io.vertx.core.eventbus.Message<Object> m) {
		try {
			this.numberOfResults = Integer.parseInt(message.getData());
			vertx.eventBus().send(this.next, m);
			m.reply("");
		} catch (Exception e) {
			m.fail(20, "Cannot cast results-size to Integer");
		}
	}
}
