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

    }
}