package com.viatra.cps.benchmark.reports.processing.models;
 
public class Case {
	private String caseName;
	private int runIndex;
    private String  scenario;
    private String tool;
    private int size;
    
    public Case(String caseName, int runIndex, String scenario, String tool, int size) {
    	this.caseName = caseName;
    	this.runIndex = runIndex;
    	this.scenario = scenario;
    	this.tool = tool;
    	this.size = size;
    }
    
    public String getCaseNumber() {
    	return caseName;
    }
    
    public int getRunIndex() {
    	return runIndex;
    }
    
    public String getScenario() {
    	return scenario;
    }
    
    public String getTool() {
    	return tool;
    }
    
    public int getSzie() {
    	return size;
    }
    
    public void print() {
    	System.out.println("\tCaseName: " + caseName);
    	System.out.println("\tRunIndex: " + runIndex);
    	System.out.println("\tScenario: " + scenario);
    	System.out.println("\tTool: " + tool);
    	System.out.println("\tSize: " + size);
    }
}
