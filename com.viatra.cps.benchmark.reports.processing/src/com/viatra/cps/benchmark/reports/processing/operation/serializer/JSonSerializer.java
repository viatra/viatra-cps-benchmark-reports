package com.viatra.cps.benchmark.reports.processing.operation.serializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.Processor;
import com.viatra.cps.benchmark.reports.processing.models.AggregataedResult;
import com.viatra.cps.benchmark.reports.processing.models.Tool;
import com.viatra.cps.benchmark.reports.processing.models.Result;
import com.viatra.cps.benchmark.reports.processing.operation.Operation;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.MetricResult;
import eu.mondo.sam.core.results.PhaseResult;

public class JSonSerializer implements Operation {
	protected Thread thread;

	public Thread getThread() {
		return thread;
	}

	protected Processor processor;
	protected Boolean running;
	protected Object lock;
	protected ConcurrentLinkedQueue<BenchmarkResult> queue;
	protected AggregataedResult result;
	protected File json;
	protected Map<String, Map<Integer, PhaseResult>> map;

	public JSonSerializer(File out, String ID) {
		this.lock = new Object();
		this.running = false;
		this.json = out;
		this.result = new AggregataedResult(ID, "", "");
		this.map = new HashMap<>();
	}

	public void setProcessor(Processor p) {
		this.processor = p;
	}

	@Override
	public void run() {
		while (this.running || !this.queue.isEmpty()) {
			BenchmarkResult benchmarkResult = null;
			synchronized (this.lock) {
				if (this.queue.isEmpty()) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			benchmarkResult = this.queue.poll();
			if (benchmarkResult != null) {
				this.addToMap(benchmarkResult);
			}
		}
		this.save();
		this.processor.chainEnd(result.getOperation());
	}

	private void addToMap(BenchmarkResult benchmarkResult) {
		Map<Integer, PhaseResult> sizeMap = this.map.get(benchmarkResult.getCaseDescriptor().getTool());
		if (sizeMap == null) {
			sizeMap = new HashMap<>();
			this.map.put(benchmarkResult.getCaseDescriptor().getTool(), sizeMap);
		}
		sizeMap.put(benchmarkResult.getCaseDescriptor().getSize(), benchmarkResult.getPhaseResults().get(0));
	}

	private void save() {
		Set<String> toolKeys = this.map.keySet();
		List<Tool> tools = new ArrayList<>();
		toolKeys.forEach(tool -> {
			Tool newTool = new Tool(tool);
			Set<Integer> sizekeys = this.map.get(tool).keySet();
			final List<Result> results = new ArrayList<>();

			sizekeys.forEach(size -> {
				Result res = new Result(size);
				MetricResult metric = this.map.get(tool).get(size).getMetrics().get(0);
				metric.setValue(metric.getValue());
				res.setMetrics(metric);
				results.add(res);
			});
			List<Result> sortedResult = results.stream()
					.sorted((object1, object2) -> object1.getSize().compareTo(object2.getSize()))
					.collect(Collectors.toList());
			newTool.setResults(sortedResult);
			tools.add(newTool);
		});
		this.result.setTool(tools);
		this.append();
	}

	private void append() {
		ObjectMapper mapper = new ObjectMapper();
		// to enable standard indentation ("pretty-printing"):
		mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		// turn off autodetection
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, false);
		mapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);
		List<AggregataedResult> tmp = null;
		try {
			synchronized (json) {
				if (json.exists()) {

					tmp = mapper.readValue(json, new TypeReference<List<AggregataedResult>>() {
					});

				}
				if (tmp == null) {
					tmp = new ArrayList<>();
				}
				tmp.add(this.result);
				mapper.writeValue(json, tmp);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean start() {
		try {
			this.thread = new Thread(this);
			this.thread.setDaemon(true);
			this.queue = new ConcurrentLinkedQueue<>();
			this.running = true;
			this.thread.start();
			return true;
		} catch (IllegalThreadStateException e) {
			return false;
		}
	}

	@Override
	public void addResult(BenchmarkResult result) {
		synchronized (this.lock) {
			this.queue.add(result);
			this.lock.notify();
		}
	}

	@Override
	public void stop() {
		synchronized (this.lock) {
			this.running = false;
			this.lock.notify();
		}
	}

	@Override
	public void setNext(Operation next) {
		// Do nothing
	}

}
