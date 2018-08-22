package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Message {
	
	
	@JsonProperty("event")
	protected String event;
	
	@JsonProperty("data")
	protected String data;

	public String getEvent() {
		return event;
	}

	public void setEvetnType(String event) {
		this.event = event;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public Message() {
		// TODO Auto-generated constructor stub
	}
	
	public Message(String event, String data) {
		this.event = event;
		this.data = data;
	}

}
