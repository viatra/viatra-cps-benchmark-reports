package com.viatra.cps.benchmark.reports.processing.verticles.operation;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;

import java.io.IOException;
import java.util.function.Function;

import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.Data;
import com.viatra.cps.benchmark.reports.processing.models.Header;
import com.viatra.cps.benchmark.reports.processing.models.Message;

import eu.mondo.sam.core.results.BenchmarkResult;

public abstract class Operation extends AbstractVerticle {
	protected String next;
	protected String scenario;
	protected ObjectMapper mapper;
	protected Integer numberOfResults = 0;
	protected String ID;
	protected String operationId;

	public Operation(String next, String id, String scenario, ObjectMapper mapper) {
		this.ID = id;
		this.next = next;
		this.scenario = scenario;
		this.mapper = mapper;
	}

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
				case "Header":
					this.resultsSizeReceived(message, m);
					break;
				case "Result":
					Data data = mapper.readValue(message.getData().toString(), Data.class);
					this.addResult(data.getResult());
					break;
				default:
					vertx.eventBus().send(this.scenario, m.body().toString());
					break;
				}
			} catch (IOException e) {
				this.sendError(e);
			}
		});
		startFuture.complete();
	}

	protected void resultsSizeReceived(Message message, io.vertx.core.eventbus.Message<Object> m) {
		try {
			Header header = mapper.readValue(message.getData(), Header.class);
			this.numberOfResults = header.getSize();
			this.operationId = header.getOperationId();
			m.reply("");
		} catch (Exception e) {
			m.fail(20, e.getMessage());
		}
	}

	protected void sendResult(BenchmarkResult result) {
		try {
			vertx.eventBus().send(this.next, this.mapper.writeValueAsString(
					new Message("Result", mapper.writeValueAsString(new Data(this.operationId, result)))));
		} catch (IOException e) {
			this.sendError(e);
		}
	}

	protected void sendResultsSize(Header header,
			Function<AsyncResult<io.vertx.core.eventbus.Message<Object>>, Void> callback) {
		try {
			vertx.eventBus().send(this.next,
					mapper.writeValueAsString(new Message("Header", mapper.writeValueAsString(header))), res -> {
						if (res.succeeded()) {
							callback.apply(res);
						} else {
							this.sendError(new Exception(res.cause().getMessage() + " " + this.ID));
						}
					});
		} catch (IOException e) {
			this.sendError(e);
		}
	}

	protected void sendError(Exception e) {
		try {
			vertx.eventBus().send(this.scenario, mapper.writeValueAsString(new Message("Error", e.getMessage())));
		} catch (IOException e1) {
			vertx.eventBus().send(this.scenario, e.getMessage());
		}
	}
}
