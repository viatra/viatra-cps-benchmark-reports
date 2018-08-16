package com.viatra.cps.benchmark.reports.processing.verticles.operation;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.shareddata.impl.AsynchronousCounter;

import java.io.IOException;
import java.util.function.Function;

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
				this.sendError();
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

	protected void sendResults(BenchmarkResult result) {
		try {
			vertx.eventBus().send(this.next,
					this.mapper.writeValueAsString(new Message("Result", mapper.writeValueAsString(result))));
		} catch (IOException e) {
			this.sendError();
		}
	}

	protected void sendResultsSize(String size,
			Function<AsyncResult<io.vertx.core.eventbus.Message<Object>>, Void> callback) {
		try {
			vertx.eventBus().send(this.next, mapper.writeValueAsString(new Message("Results-size", size)), res -> {
				if (res.succeeded()) {
					callback.apply(res);
				} else {
					vertx.eventBus().send(this.scenario,
							new Message("Failed", res.cause().getMessage() + " " + this.ID));
				}
			});
		} catch (IOException e) {
			this.sendError();
		}
	}

	protected void sendError() {
		try {
			vertx.eventBus().send(this.scenario,
					mapper.writeValueAsString(new Message("Error", "Cannot parse message in " + this.ID)));
		} catch (IOException e1) {
			vertx.eventBus().send(this.scenario, "Cannot parse message in " + this.ID);
		}
	}
}
