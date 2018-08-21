package com.viatra.cps.benchmark.reports.processing.verticles.operation.serializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.AggregataedResult;
import com.viatra.cps.benchmark.reports.processing.models.Data;
import com.viatra.cps.benchmark.reports.processing.models.DiagramDescriptor;
import com.viatra.cps.benchmark.reports.processing.models.DiagramSet;
import com.viatra.cps.benchmark.reports.processing.models.Diagrams;
import com.viatra.cps.benchmark.reports.processing.models.Header;
import com.viatra.cps.benchmark.reports.processing.models.Message;
import com.viatra.cps.benchmark.reports.processing.models.Tool;
import com.viatra.cps.benchmark.reports.processing.models.Result;
import com.viatra.cps.benchmark.reports.processing.models.ResultData;
import com.viatra.cps.benchmark.reports.processing.models.Results;

import com.viatra.cps.benchmark.reports.processing.verticles.ProcessorVerticle;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.Operation;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.MetricResult;
import eu.mondo.sam.core.results.PhaseResult;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class JSonSerializer extends AbstractVerticle {
	protected Thread thread;

	public Thread getThread() {
		return thread;
	}

	protected ProcessorVerticle processor;
	protected List<AggregataedResult> result;
	protected File json;
	protected Map<String, Map<String, Map<Integer, PhaseResult>>> map;
	protected String path;
	protected File digramConfiguration;
	protected ObjectMapper mapper;
	protected Diagrams config;
	protected Diagrams template;
	protected String caseName;
	protected String scenarioName;
	protected List<DiagramSet> dashboard;
	protected File dashboardConfogurationFile;
	protected String buildId;
	protected Map<String, Integer> opearations;
	protected String ID;
	protected String caseId;

	public JSonSerializer(String next, String id, String caseId, ObjectMapper mapper, String scenarioName, File out,
			File digramConfiguration, String ID, String path, Diagrams template, String caseName,
			List<DiagramSet> dashboard, File dashboardConfogurationFile, String buildId) {
		this.buildId = buildId;
		this.json = out;
		this.path = path;
		this.mapper = mapper;
		this.ID = id;
		this.caseId = caseId;
		this.dashboard = dashboard;
		this.dashboardConfogurationFile = dashboardConfogurationFile;
		this.caseName = caseName;
		this.caseName = scenarioName;
		this.template = template;
		this.result = new ArrayList<>();
		this.config = new Diagrams(path);
		this.map = new HashMap<>();
		this.digramConfiguration = digramConfiguration;
		this.opearations = new HashMap<>();
	}

	public void start(Future<Void> startFuture) {
		vertx.eventBus().consumer(this.ID, m -> {
			try {
				Message message = mapper.readValue(m.body().toString(), Message.class);
				switch (message.getEvent()) {
				case "Header":
					this.resultsSizeReceived(message, m);
				case "Result":
					try {
						Data data = mapper.readValue(message.getData().toString(), Data.class);
						this.addResult(data);
					} catch (Exception e) {
						vertx.eventBus().send(this.caseId, mapper
								.writeValueAsString(new Message("Error", "Cannot parse message data in " + this.ID)));
					}
					break;
				case "Save":
					this.save(m);
				default:
					vertx.eventBus().send(this.caseId, m);
					break;
				}
			} catch (IOException e) {
				this.sendError();
			}
		});
		startFuture.complete();
	}

	protected void sendError() {
		try {
			vertx.eventBus().send(this.caseId,
					mapper.writeValueAsString(new Message("Error", "Cannot parse message in " + this.ID)));
		} catch (IOException e1) {
			vertx.eventBus().send(this.caseId, "Cannot parse message in " + this.ID);
		}
	}

	protected void resultsSizeReceived(Message message, io.vertx.core.eventbus.Message<Object> m) {
		try {
			Header header = mapper.readValue(message.getData(), Header.class);
			this.opearations.put(header.getOperationId(), header.getSize());
			m.reply("");
		} catch (Exception e) {
			m.fail(20, "Data is not a valid header");
		}
	}

	private void addToMap(Data data) {
		Map<String, Map<Integer, PhaseResult>> operationMap = this.map.get(data.getOperationId());
		if (operationMap == null) {
			operationMap = new HashMap<>();
			Map<Integer, PhaseResult> sizeMap = new HashMap<>();
			sizeMap.put(data.getResult().getCaseDescriptor().getSize(), data.getResult().getPhaseResults().get(0));
			operationMap.put(data.getResult().getCaseDescriptor().getTool(), sizeMap);
			this.map.put(data.getOperationId(), operationMap);
		} else {
			Map<Integer, PhaseResult> sizeMap = operationMap.get(data.getResult().getCaseDescriptor().getTool());
			if (sizeMap == null) {
				sizeMap = new HashMap<>();
				operationMap.put(data.getResult().getCaseDescriptor().getTool(), sizeMap);
			}
			sizeMap.put(data.getResult().getCaseDescriptor().getSize(), data.getResult().getPhaseResults().get(0));
		}
	}

	private void append(String operationId) {
		AggregataedResult result = new AggregataedResult(operationId);
		Map<String, Map<Integer, PhaseResult>> operationMap = this.map.get(operationId);
		Set<String> toolKeys = operationMap.keySet();
		if (!toolKeys.isEmpty()) {
			List<Tool> tools = new ArrayList<>();
			toolKeys.forEach(tool -> {
				Tool newTool = new Tool(tool);
				Set<Integer> sizekeys = operationMap.get(tool).keySet();
				final List<Result> results = new ArrayList<>();
				if (!sizekeys.isEmpty()) {
					sizekeys.forEach(size -> {
						Result res = new Result(size);
						MetricResult metric = operationMap.get(tool).get(size).getMetrics().get(0);
						metric.setValue(metric.getValue());
						res.setMetrics(metric);
						results.add(res);
					});
					List<Result> sortedResult = results.stream()
							.sorted((object1, object2) -> object1.getSize().compareTo(object2.getSize()))
							.collect(Collectors.toList());
					newTool.setResults(sortedResult);
					tools.add(newTool);
				}
			});
			result.setTool(tools);
			this.result.add(result);
			this.map.remove(operationId);
			try {
				List<ResultData> resultDatas = this.template.getResultData();
				ResultData dataTemplate = resultDatas.stream()
						.filter(resultData -> resultData.getOperationId().equals(operationId)).findFirst().get();
				ResultData resultData;
				resultData = (ResultData) dataTemplate.clone();
				resultData.setTitle(resultData.getTitle().replaceAll("CASENAME", this.caseName).replaceAll("SCENARIO",
						this.scenarioName));
				this.config.getResultData().add(resultData);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DiagramSet diagSet = this.dashboard.stream()
					.filter(diagramSet -> diagramSet.getTitle().equals(this.buildId)).findFirst().get();
			if (!diagSet.getDiagrams().stream().filter(diagram -> {
				return diagram.getBuild().equals(this.buildId) && diagram.getCaseName().equals(this.caseName)
						&& diagram.getOperationId().equals(operationId)
						&& diagram.getScenario().equals(this.scenarioName);
			}).findFirst().isPresent()) {
				diagSet.getDiagrams()
						.add(new DiagramDescriptor(this.caseName, this.buildId, this.scenarioName, operationId, true));
			}
			if (this.map.isEmpty()) {
				this.sendNotification();
			}
		}
	}

	private void save(io.vertx.core.eventbus.Message<Object> m) {
		try {
			this.mapper.writeValue(json, this.result);
			this.mapper.writeValue(digramConfiguration, this.config);
			this.mapper.writeValue(dashboardConfogurationFile, this.dashboard);
			m.reply(mapper.writeValueAsString(new Message("Successfull", "")));
		} catch (IOException e) {
			m.fail(20, "Cannot save json files");
		}
	}

	public void addResult(Data data) {
		this.addToMap(data);
		Integer numberOfResults = this.opearations.get(data.getOperationId());
		numberOfResults--;
		if (numberOfResults == 0) {
			this.opearations.remove(data.getOperationId());
			this.append(data.getOperationId());
		} else {
			this.opearations.put(data.getOperationId(), numberOfResults);
		}
	}

	private void sendNotification() {
		try {
			vertx.eventBus().send(this.caseId, this.mapper.writeValueAsString(new Message("Done", "")));
		} catch (IOException e) {
			this.sendError();
		}
	}
}
