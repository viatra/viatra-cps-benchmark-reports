pipeline {
    agent any
    stages {
        stage('Build Processor') {
            steps {
                mvn -f .\com.viatra.cps.benchmark.reports.processing\pom.xml install
                
            }
        }
        state('Build Visualizer'){
            steps{
                npm run  build --prefix .\resultVisualizer\
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}