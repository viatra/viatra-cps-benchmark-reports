#!/bin/bash
read -a builds
resultsLocation=$1
case=$2
aggregatorConfig=$3
resultVisualizerLocation=$4
buildConfigTemplate=$5
diagramConfigTemplate=$6
jarLocation=$7
updateConfig=$8
echo "Results location: "$resultsLocation
echo "Case: "$case
echo "aggregatorConfig:"$aggregatorConfig
echo "resultVisualizerLocation: "$resultVisualizerLocation
echo "Build config template: "$buildConfigTemplate
echo "Digram Config Template: "$diagramConfigTemplate
echo "Jar location: " $jarLocation
echo "Update Digram Config: " $updateConfig
for build in "${builds[@]}"
do
  echo "$build"
  java -jar "$jarLocation/com.viatra.cps.benchmark.reports.processing-0.0.1-jar-with-dependencies.jar"  -r "${resultsLocation}/$case/$build/results.json" -c "${aggregatorConfig}" -a "${resultVisualizerLocation}/resultVisualizer/src/results/" -bt "${buildConfigTemplate}" -dt "${diagramConfigTemplate}"  -dc "${resultVisualizerLocation}/resultVisualizer/src/config/diagram.config.json" -b "$build" -ca "${case}" -u ${updateConfig}
done

if [ -d "results" ]; then
  # Repo exists, update
  cd results
  git fetch
  git reset origin/Results --hard
  cd ..
else
  # Clone repo
  git clone git@github.com:viatra/viatra-cps-benchmark-reports.git results 
fi
cd ./results
git checkout Results
cd ..
cp -r ./${case} ./results/${case}
cat ./builds.json >> ./results/builds.json
cd ./results
git add .
git commit -m "upload new Results: ${case}"
git push

