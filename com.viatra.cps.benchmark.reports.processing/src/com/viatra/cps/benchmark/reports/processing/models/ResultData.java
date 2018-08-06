package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class ResultData implements Cloneable{
	
	@JsonProperty("DiagramType")
	protected String diagramType;
	
	@JsonProperty("OperationID")
	protected String operationId;
	
	@JsonProperty("Title")
	protected String title;
	
	@JsonProperty("XLabel")
	protected String xLabel;
	
	@JsonProperty("YLabel")
	protected String yLabel;
	
	@JsonProperty("Metric")
	protected String Metric;
	
	
	@JsonProperty("XType")
	protected String xType;
	
	
	public String getxType() {
		return xType;
	}

	public void setxType(String xType) {
		this.xType = xType;
	}

	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

	@JsonProperty("YType")
	protected String xtype;
	

	public String getDiagramType() {
		return diagramType;
	}

	public void setDiagramType(String diagramType) {
		this.diagramType = diagramType;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getxLabel() {
		return xLabel;
	}

	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}

	public String getyLabel() {
		return yLabel;
	}

	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}

	public String getMetric() {
		return Metric;
	}

	public void setMetric(String metric) {
		Metric = metric;
	}
	
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
}
