read -a builds
resultsLocation=$1
case=$2
aggregatorConfig=$3
resultVisualizerLocation=$4
buildConfigTemplate=$5
diagramConfigTemplate=$6
updateConfig=$7
echo "Results location: "$resultsLocation
echo "Case: "$case
echo "aggregatorConfig:"$aggregatorConfig
echo "resultVisualizerLocation: "$resultVisualizerLocation
echo "Build config template: "$buildConfigTemplate
echo "Digram Config Template: "$diagramConfigTemplate
echo "Update Digram Config: " $updateConfig
for build in "${builds[@]}"
do
  echo "$build"
  java -jar ./com.viatra.cps.benchmark.reports.processing-0.0.1-jar-with-dependencies.jar  -r "${resultsLocation}/$case/$build/results.json" -c "${aggregatorConfig}" -a "${resultVisualizerLocation}/resultVisualizer/src/results/" -bt "${buildConfigTemplate}" -dt "${diagramConfigTemplate}" -bs "${resultVisualizerLocation}/resultVisualizer/src/config/builds.json" -dc "${resultVisualizerLocation}/resultVisualizer/src/config/diagram.config.json" -b "$case-$build" -u ${updateConfig}
done
