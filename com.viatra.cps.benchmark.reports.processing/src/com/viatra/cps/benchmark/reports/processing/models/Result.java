package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

public class Result {
	private Case rCase;
	private List<PhaseResult> phaseResults;
	
	public Result(Case rCase, List<PhaseResult> phaseResults) {
		this.rCase = rCase;
		this.phaseResults = phaseResults;
	}
	
	public Case getCase() {
		return rCase;
	}
	
	public List<PhaseResult> getPhasesResults(){
		return phaseResults;
	}
	
	public void print() {
		System.out.print("{ \n Result:\n \t{ \n");
		rCase.print();
		System.out.print("\t}, \n");
		System.out.print("\tPhaseResult: \n\t{[\n");
		for(PhaseResult phRes : phaseResults) {
			phRes.print();
		}
		System.out.print("\n\t]}\n");
		System.out.print("}");
	}
}
