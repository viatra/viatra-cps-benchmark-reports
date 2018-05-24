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
        stage('Build Visualizer'){
            steps {
                sh 'npm run  build --prefix "./resultVisualizer/"'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}