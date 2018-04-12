package com.viatra.cps.benchmark.reports.processing.models;

import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;




public class AggregatorConfiguration {

	@JsonProperty("ID")
	private String ID;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	@JsonProperty("OperationChain")
	private List<OperationConfig> operationChain;

	public void setOperation(List<OperationConfig> operationConfigs) {
		this.operationChain = operationConfigs;
	}

	public List<OperationConfig> getOperations(Boolean reverse) {
		if(reverse) {
			List<OperationConfig> tmp = this.operationChain.subList(0, this.operationChain.size());
			Collections.reverse(tmp);
			return tmp;
		}else {
			return this.operationChain;
		}
	}

	public OperationConfig getFirts() {
		return this.operationChain.get(0);
	}
}
