# viatra.github.io/viatra-cps-benchmark-reports
Visualization of CPS benchmark results

## Prerequisite

* Maven
* Java 8
* Node.js / npm

## Usage

### Processor

#### Build

*  mvn -f "{git_repository}/com.viatra.cps.benchmark.reports.processing/pom.xml" install

#### Run

##### Java

 * java -jar "{git_repository}/com.viatra.cps.benchmark.reports.processing/com.viatra.cps.benchmark.reports.processing-0.0.1-jar-with-dependencies.jar"  -b {ID} -i {intput results path} -c {processing configuration file} -o {output results path} -p {processing config file} -d {diagram Config Template file} -v {visualizer Configuration path}

##### Schell script

* {git_repository}/com.viatra.cps.benchmark.reports.processing/run.sh -b {ID} -i {intput results path} -c {processing configuration file} -o {output results path} -p {processing config file} -d {diagram Config Template file} -v {visualizer Configuration path}

### Visualizer

#### Build and Run

##### Webserver

* cd {git_repository}/resultVisualizer
* costumize metrics in visualizer configuration
* npm install
* ng build [--prod --base-href ""]
* copy build folder



##### Local

* cd {git_repository}/resultVisualizer
* costumize metrics in visualizer configuration
* npm install
* ng serve


### Metric
* Default value:
```json
{
    "Metric" : "Name",
    "DefaultScale" : "u", 
    "Units" : [ { --units
      "Label" : "u",
      "Value" : [ 1.0, 1.0 ]
    }]
  }
  ```
  
  * Metric: unique name of metric
  * DefaultScale: Default unit label, must be in unit list
  * Unints: sorted list of units
  ** Label: unit label
  ** Value: unit value [down,up]

