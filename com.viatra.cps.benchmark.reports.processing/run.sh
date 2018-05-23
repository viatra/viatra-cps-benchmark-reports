read -a arr
tool=$1
echo $tool
for i in "${arr[@]}"
do
  echo "$i"
  java -jar ./com.viatra.cps.benchmark.reports.processing-0.0.1-jar-with-dependencies.jar  -r "/mnt/g/viatra-cps-benchmark-results-master/$tool/$i/results.json" -c "aggregatorConfig.json" -a "./../resultVisualizer/src/results/" -bt "./build.config.template.json" -dt "./diagram.config.template.json" -bs "./../resultVisualizer/src/config/builds.json" -dc "./../resultVisualizer/src/config/diagram.config.json" -b "$tool-$i"
done
