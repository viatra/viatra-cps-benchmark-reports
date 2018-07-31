package com.viatra.cps.benchmark.reports.processing.models;

import java.util.Random;

import org.codehaus.jackson.annotate.JsonProperty;

public class ToolColor {

	@JsonProperty("ToolName")
	protected String toolName;

	@JsonProperty("Color")
	protected String color;

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	public String getToolName() {
		return this.toolName;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return this.color;
	}

	public ToolColor() {
		// TODO Auto-generated constructor stub
	}

	public ToolColor(String tool) {
		this.toolName = tool;
		this.color = this.generateColor();
	}

	private String generateColor() {
		Random rand = new Random();
		int r = rand.nextInt(255);
		int g = rand.nextInt(255);
		int b = rand.nextInt(255);

		return "rgba(" + r + "," + g + "," + b + ",1)";
	}
}
