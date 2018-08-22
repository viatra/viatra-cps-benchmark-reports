package com.viatra.cps.benchmark.reports.processing.verticles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.viatra.cps.benchmark.reports.processing.models.Builds;
import com.viatra.cps.benchmark.reports.processing.models.DiagramSet;
import com.viatra.cps.benchmark.reports.processing.models.Message;
import com.viatra.cps.benchmark.reports.processing.models.Scale;
import com.viatra.cps.benchmark.reports.processing.models.ToolColor;
import com.viatra.cps.benchmark.reports.processing.models.VisualizerConfiguration;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class JsonUpdateVerticle extends AbstractVerticle {

	private File visualizerConfigurationJson;
	private File buildsJson;
	private File dashboardJson;
	private VisualizerConfiguration visualizerConfiguration;
	private List<Builds> builds;
	private List<DiagramSet> dashboard;
	private ObjectMapper mapper;

	public JsonUpdateVerticle(File visualizerConfigurationJson, File buildsJson, File dashboardJson,
			ObjectMapper mapper) {
		this.buildsJson = buildsJson;
		this.visualizerConfigurationJson = visualizerConfigurationJson;
		this.dashboardJson = dashboardJson;
		this.mapper = mapper;
	}

	private Boolean loadVisualizerConfiguration() {
		if (visualizerConfigurationJson.exists()) {
			try {
				this.visualizerConfiguration = mapper.readValue(this.visualizerConfigurationJson,
						VisualizerConfiguration.class);
			} catch (IOException e) {
				System.err.println(e.getMessage());
				return false;
			}
		} else {
			this.visualizerConfiguration = new VisualizerConfiguration();
		}
		return true;
	}

	private Boolean loadBuilds() {
		if (buildsJson.exists()) {
			try {
				this.builds = this.mapper.readValue(buildsJson, new TypeReference<List<Builds>>() {
				});
			} catch (IOException e) {
				System.err.println(e.getMessage());
				return false;

			}
		} else {
			this.builds = new ArrayList<>();
		}
		return true;
	}

	private Boolean loadDashboard() {
		if (this.dashboardJson.exists()) {
			try {
				this.dashboard = mapper.readValue(dashboardJson, new TypeReference<List<DiagramSet>>() {
				});
			} catch (IOException e) {
				System.err.println(e.getMessage());
				return false;
			}
		} else {
			dashboard = new ArrayList<>();
		}
		return true;
	}

	private Boolean saveVisualizer() {
		try {
			System.out.println("Save visualizer configuration");
			mapper.writeValue(this.visualizerConfigurationJson, this.visualizerConfiguration);
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	private Boolean saveBuilds() {
		try {
			System.out.println("Save builds");
			mapper.writeValue(this.buildsJson, this.builds);
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	private Boolean saveDashboard() {
		try {
			System.out.println("Save dashboard");
			mapper.writeValue(this.dashboardJson, this.dashboard);
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	private Boolean mergeScales(List<Scale> newScales) {
		try {
			List<Scale> scales = this.visualizerConfiguration.getScales();
			if (scales.size() == 0) {
				scales.addAll(newScales);
			} else {
				for (Scale scale : newScales) {
					if (!scales.stream().filter(s -> s.getMetric().equals(scale.getMetric())).findFirst().isPresent()) {
						scales.add(scale);
					}
				}
			}
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	private Boolean mergeTools(List<ToolColor> newTools) {
		try {
			List<ToolColor> tools = this.visualizerConfiguration.getToolColors();
			if (tools.size() == 0) {
				tools.addAll(newTools);
			} else {
				for (ToolColor tool : newTools) {
					if (!tools.stream().filter(t -> t.getToolName().equals(tool.getToolName())).findFirst()
							.isPresent()) {
						tools.add(tool);
					}
				}
			}
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	private Boolean clearBuild(String buildId) {
		try {
			Optional<Builds> optBuild = this.builds.stream().filter(b -> b.getBuildId().equals(buildId)).findFirst();
			if (optBuild.isPresent()) {
				this.builds.remove(optBuild.get());
			}
			Optional<DiagramSet> optDiagram = this.dashboard.stream().filter(d -> d.getTitle().equals(buildId))
					.findFirst();
			if (optDiagram.isPresent()) {
				this.dashboard.remove(optDiagram.get());
			}
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	private Boolean mergeBuilds(Builds newBuild) {
		try {
			Optional<Builds> optBuild = this.builds.stream().filter(b -> b.getBuildId().equals(newBuild.getBuildId()))
					.findFirst();
			if (optBuild.isPresent()) {
				this.builds.remove(optBuild.get());
			}
			this.builds.add(newBuild);
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	private Boolean mergeDashboard(DiagramSet newDiagramSet) {
		try {
			Optional<DiagramSet> optDash = this.dashboard.stream()
					.filter(d -> d.getTitle().equals(newDiagramSet.getTitle())).findFirst();
			if (optDash.isPresent()) {
				this.dashboard.remove(optDash.get());
			}
			this.dashboard.add(newDiagramSet);
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public void start(Future<Void> startFuture) {
		if (this.loadVisualizerConfiguration() && this.loadBuilds() && this.loadDashboard()) {
			startFuture.complete();
			vertx.eventBus().consumer("JsonUpdater", m -> {
				try {
					Message message = mapper.readValue(m.body().toString(), Message.class);
					switch (message.getEvent()) {
					case "Save":
						if (this.saveVisualizer() && this.saveBuilds() && this.saveDashboard()) {
							m.reply(mapper.writeValueAsString(new Message("Successfull", "")));
						} else {
							m.fail(3, "Cannot save all json");
						}
						break;
					case "Visualizer-Color":
						try {
							List<ToolColor> tools = mapper.readValue(message.getData(),
									new TypeReference<List<ToolColor>>() {
									});
							if (this.mergeTools(tools)) {
								m.reply(mapper.writeValueAsString(new Message("Done", "")));
							} else {
								m.fail(6, "Cannot merge ToolColor");
							}
						} catch (Exception e) {
							System.err.println(e.getMessage());
							m.fail(4, "Cannot parse message data");
						}
						break;
					case "Visualizer-Scale":
						try {
							List<Scale> scales = mapper.readValue(message.getData(), new TypeReference<List<Scale>>() {
							});
							if (this.mergeScales(scales)) {
								m.reply(mapper.writeValueAsString(new Message("Done", "")));
							} else {
								m.fail(6, "Cannot merge Scles");
							}
						} catch (Exception e) {
							System.err.println(e.getMessage());
							m.fail(4, "Cannot parse message data");
						}
						break;
					case "Builds":
						try {
							Builds builds = mapper.readValue(message.getData(), Builds.class);
							if (this.mergeBuilds(builds)) {
								m.reply(mapper.writeValueAsString(new Message("Done", "")));
							} else {
								m.fail(6, "Cannot merge Builds");
							}
						} catch (Exception e) {
							System.err.println(e.getMessage());
							m.fail(4, "Cannot parse message data");
						}
						break;
					case "Dashboard":
						try {
							DiagramSet diagramSet = mapper.readValue(message.getData(), DiagramSet.class);
							if (this.mergeDashboard(diagramSet)) {
								m.reply(mapper.writeValueAsString(new Message("Done", "")));
							} else {
								m.fail(6, "Cannot merge Dashboard");
							}
						} catch (Exception e) {
							System.err.println(e.getMessage());
							m.fail(4, "Cannot parse message data");
						}
						break;
					case "Clear-build":
						if (this.clearBuild(message.getData())) {
							m.reply(mapper.writeValueAsString("Done"));
						} else {
							m.fail(5, "Cannot clear builds or dashboard");
						}
						break;
					default:
						m.fail(2, "Unexpected event");
						break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					m.fail(1, "Cannot parse message");
				}
			});
		} else {
			startFuture.fail("Cannot parse all input json");
		}
	}

}
