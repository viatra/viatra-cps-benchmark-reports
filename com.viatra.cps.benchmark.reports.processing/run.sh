#!/bin/bash
read -a builds
BuildID=$1
inputResults=$2
processingConfig=$3
outputResults=$4
diagramConfigTemplate=$5
visualizerConfiguration=$6
jarLocation=$7
echo "BuilID: "$BuildID
echo "Input results path: "$inputResults
echo "Output results path: "$outputResults
echo "Processing configuration:"$processingConfig
echo "Visualizer configuration: "$visualizerConfiguration
echo "Digram Config Template: "$diagramConfigTemplate
echo "Jar location: " $jarLocation
for build in "${builds[@]}"
do
  echo "$build"
  java -jar "$jarLocation/com.viatra.cps.benchmark.reports.processing-0.0.1-jar-with-dependencies.jar"  -b "${BuildID}" -i "${resultsLocation=$1}" -c "${aggregatorConfig}" -o "${outputResults}" -p "${processingConfig}" -d "${diagramConfigTemplate}" -v "${visualizerConfiguration}"
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
cp -rf ./${BuildID} ./results/${BuildID}
cp ./builds.json  ./results/builds.json

