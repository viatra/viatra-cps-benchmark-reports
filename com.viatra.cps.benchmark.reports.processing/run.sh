read -a builds
resultsLocation=$1
case=$2
aggregatorConfig=$3
resultVisualizerLocation=$4
buildConfigTemplate=$5
diagramConfigTemplate=$6
echo $resultsLocation
echo $case
echo $aggregatorConfig
echo $resultVisualizerLocation
echo $buildConfigTemplate
echo $diagramConfigTemplate
for build in "${builds[@]}"
do
  echo "$build"
  java -jar ./com.viatra.cps.benchmark.reports.processing-0.0.1-jar-with-dependencies.jar  -r "${resultsLocation}/$case/$build/results.json" -c "${aggregatorConfig}" -a "${resultVisualizerLocation}/resultVisualizer/src/results/" -bt "${buildConfigTemplate}" -dt "${diagramConfigTemplate}" -bs "${resultVisualizerLocation}/resultVisualizer/src/config/builds.json" -dc "${resultVisualizerLocation}/resultVisualizer/src/config/diagram.config.json" -b "$case-$build"
done
