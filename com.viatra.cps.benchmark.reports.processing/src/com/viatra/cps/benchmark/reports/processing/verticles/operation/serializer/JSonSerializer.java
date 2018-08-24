package com.viatra.cps.benchmark.reports.processing.verticles.operation.serializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.AggregataedResult;
import com.viatra.cps.benchmark.reports.processing.models.Builds;
import com.viatra.cps.benchmark.reports.processing.models.Case;
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
	protected DiagramSet dashboard;
	protected String buildId;
	protected Map<String, Integer> opearations;
	protected String ID;
	protected String scenarioId;
	protected Builds builds;
	protected Results output;

	public JSonSerializer(String id, String scenarioId, ObjectMapper mapper, String scenarioName, File out,
			Diagrams digramConfiguration, String path, String caseName, String buildId) {
		this.buildId = buildId;
		this.json = out;
		this.path = path;
		this.mapper = mapper;
		this.ID = id;
		this.scenarioId = scenarioId;
		this.dashboard = new DiagramSet(buildId);
		this.caseName = caseName;
		this.scenarioName = scenarioName;
		this.result = new ArrayList<>();
		this.config = new Diagrams(path);
		this.map = new HashMap<>();
		List<String> s = new ArrayList<>();
		s.add(scenarioName);
		Case c = new Case();
		c.setScenarios(s);
		List<Case> caseList = new ArrayList<>();
		caseList.add(c);
		c.setCaseName(caseName);
		this.builds = new Builds();
		this.builds.setCases(caseList);
		builds.setBuildId(buildId);
		this.template = digramConfiguration;
		this.digramConfiguration = new File(path + "/" + "diagram.config.json");
		this.opearations = new HashMap<>();
	}

	public void start(Future<Void> startFuture) {
		vertx.eventBus().consumer(this.ID, m -> {
			try {
				Message message = mapper.readValue(m.body().toString(), Message.class);
				switch (message.getEvent()) {
				case "Header":
					this.resultsSizeReceived(message, m);
					break;
				case "Result":
						Data data = mapper.readValue(message.getData().toString(), Data.class);
						this.addResult(data);
					break;
				case "Save":
					this.save(m);
					break;
				default:
					vertx.eventBus().send(this.scenarioId, m.body().toString());
					break;
				}
			} catch (IOException e) {
				this.sendError(e);
			}
		});
		startFuture.complete();
	}

	protected void sendError(Exception e) {
		try {
			vertx.eventBus().send(this.scenarioId, mapper.writeValueAsString(new Message("Error", e.getMessage())));
		} catch (IOException e1) {
			vertx.eventBus().send(this.scenarioId, e.getMessage());
		}
	}

	protected void resultsSizeReceived(Message message, io.vertx.core.eventbus.Message<Object> m) {
		try {
			Header header = mapper.readValue(message.getData(), Header.class);
			this.opearations.put(header.getOperationId(), header.getSize());
			m.reply("");
		} catch (Exception e) {
			m.fail(20, e.getMessage());
		}
	}

	private void addToMap(Data data) {
		Map<String, Map<Integer, PhaseResult>> operationMap = this.map.get(data.getOperationId());
		if (data.getResult().getPhaseResults().size() > 0) {
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
	}

	private void append(String operationId) {
		AggregataedResult result = new AggregataedResult(operationId);
		Map<String, Map<Integer, PhaseResult>> operationMap = this.map.get(operationId);
		if (operationMap != null) {
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
					resultData.setTitle(resultData.getTitle().replaceAll("CASENAME", this.caseName)
							.replaceAll("SCENARIO", this.scenarioName));
					this.config.getResultData().add(resultData);
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.dashboard.getDiagrams()
						.add(new DiagramDescriptor(this.caseName, this.buildId, this.scenarioName, operationId, true));
			}
			if (this.opearations.isEmpty()) {
				this.sendNotification();
			}
		}
	}

	private void save(io.vertx.core.eventbus.Message<Object> m) {
		try {
			System.out.println(this.ID + " - Save results");
			if (!json.exists()) {
				Files.createDirectories(Paths.get(json.getParent()));
			}
			this.output = new Results(this.buildId + "/" + this.caseName + "/" + this.scenarioId);
			this.output.setResults(this.result);
			this.mapper.writeValue(json, this.output);
			this.mapper.writeValue(digramConfiguration, this.config);
			vertx.eventBus().send("JsonUpdater",
					mapper.writeValueAsString(new Message("Dashboard", mapper.writeValueAsString(this.dashboard))));
			vertx.eventBus().send("JsonUpdater",
					mapper.writeValueAsString(new Message("Builds", mapper.writeValueAsString(this.builds))));
			m.reply(mapper.writeValueAsString(new Message("Successfull", "")));
		} catch (IOException e) {
			m.fail(20, e.getMessage());
		}
	}

	public void addResult(Data data) {
		this.addToMap(data);
		Integer numberOfResults = this.opearations.get(data.getOperationId());
		numberOfResults--;
		if (numberOfResults == 0) {
			this.opearations.remove(data.getOperationId());
			this.append(data.getOperationId());
			System.out.println(this.ID + " - Chain done. " + this.opearations.size() + " chain remeaning");
		} else {
			this.opearations.put(data.getOperationId(), numberOfResults);
		}
	}

	private void sendNotification() {
		try {
			vertx.eventBus().send(this.scenarioId, this.mapper.writeValueAsString(new Message("Done", "")));
		} catch (IOException e) {
			this.sendError(e);
		}
	}
}
