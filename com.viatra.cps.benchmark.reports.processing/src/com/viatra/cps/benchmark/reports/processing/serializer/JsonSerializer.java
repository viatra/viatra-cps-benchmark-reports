package com.viatra.cps.benchmark.reports.processing.serializer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.viatra.cps.benchmark.reports.processing.models.AggregataedResult;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.ResultSerializer;

public class JsonSerializer{

	public void serialize(List<AggregataedResult> aggregataedResults, String fileName) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		// to enable standard indentation ("pretty-printing"):
		mapper.configure(
				SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,
				false);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT,
				true);
		// turn off autodetection
		mapper.configure(
				SerializationConfig.Feature.AUTO_DETECT_FIELDS,
				false);
		mapper.configure(
				SerializationConfig.Feature.AUTO_DETECT_GETTERS,
				false);
		try {

			mapper.writeValue(new File("../resultVisualizer/src/results/"+fileName + ".json"), aggregataedResults);
		} catch (JsonGenerationException e) {
			throw new IOException(e);
		} catch (JsonMappingException e) {
			throw new IOException(e);
		}
		
	}

}
