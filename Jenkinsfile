pipeline {
    agent any
    tools { 
        maven 'Maven 3.3.9' 
        jdk 'OpenJDK 8' 
    }
        stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                ''' 
            }
        }
        stage('Build Processor') {
            steps {
                 sh 'mvn -f "./com.viatra.cps.benchmark.reports.processing/pom.xml" install'
            }
        }
        stage('Update viatra-cps-benchmark-results') {
            steps {
                sshagent(['24f0908d-7662-4e93-80cc-1143b7f92ff1']) {
                    sh "./clone-results.sh"
                }
            }
        }
        stage('Process'){
            steps{
                sshagent(['24f0908d-7662-4e93-80cc-1143b7f92ff1']) {
                    sh './com.viatra.cps.benchmark.reports.processing/run.sh "cps" "./viatra-cps-benchmark-results/"  "./com.viatra.cps.benchmark.reports.processing/config.json" "./out" "./com.viatra.cps.benchmark.reports.processing/diagramConfigTemplate.json" "./com.viatra.cps.benchmark.reports.processing/diagram.config.template.json" "./com.viatra.cps.benchmark.reports.processing" < ./com.viatra.cps.benchmark.reports.processing/builds.txt'
                }
            }
        }
        stage('Initialize visualizer'){
            steps{
                nodejs(nodeJSInstallationName: 'Latest'){
                    sh '''
                    cd ./resultVisualizer
                    npm  install
                    ''' 
                }
            }
        }
        stage('Build visualizer'){
            steps{
                nodejs(nodeJSInstallationName: 'Latest'){
                    sh '''
                    cp -rf ./results/ ./resultVisualizer/src/results/
                    cd ./resultVisualizer
                    npm  run build
                    ''' 
                }
            }
        }
        stage('Deploy'){
            steps{
                sshagent(['24f0908d-7662-4e93-80cc-1143b7f92ff1']) {
                    nodejs(nodeJSInstallationName: 'Latest'){
                        sh '''
                        cd ./resultVisualizer
                        npm run deploy
                        ''' 
                    }

                }
            }
        }
    }
}