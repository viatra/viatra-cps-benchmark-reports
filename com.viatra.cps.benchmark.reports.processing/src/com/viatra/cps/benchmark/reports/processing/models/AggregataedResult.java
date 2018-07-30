package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;



public class AggregataedResult {

	@JsonProperty("operationID")
	protected String operationID;

	@JsonProperty("tool")
	protected List<Tool> tool;

	public AggregataedResult() {
		tool = new ArrayList<>();
	}

	public AggregataedResult(String operation) {
		this.operationID = operation;
	}


	public String getOperation() {
		return operationID;
	}

	public void setOperation(String operation) {
		this.operationID = operation;
	}


	public List<Tool> getTool() {
		return tool;
	}

	public void setTool(List<Tool> tool) {
		this.tool = tool;
	}

}
