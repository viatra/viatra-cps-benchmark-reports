package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class DiagramSet {

	@JsonProperty("Title")
	protected String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("Diagrams")
	protected List<DiagramDescriptor> diagrams;

	public List<DiagramDescriptor> getDiagrams() {
		return diagrams;
	}

	public void setDiagrams(List<DiagramDescriptor> diagrams) {
		this.diagrams = diagrams;
	}

	public DiagramSet() {
		this.diagrams = new ArrayList<>();
	}

	public DiagramSet(String title) {
		this.diagrams = new ArrayList<>();
		this.title = title;
	}
}
