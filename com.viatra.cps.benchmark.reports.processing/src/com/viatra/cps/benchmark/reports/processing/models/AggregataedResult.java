package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class AggregataedResult {

	@JsonProperty("operation")
	protected String operation;

	@JsonProperty("function")
	protected String function;

	@JsonProperty("tool")
	protected List<Tool> tool;

	public AggregataedResult() {
		tool = new ArrayList<>();
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public List<Tool> getTool() {
		return tool;
	}

	public void setTool(List<Tool> tool) {
		this.tool = tool;
	}

}
