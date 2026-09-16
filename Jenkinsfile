pipeline {
    agent any

    tools {
        maven 'Maven 3.9'
        jdk   'jdk22'
    }

    options {
        timeout(time: 2, unit: 'HOURS')
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '5'))
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                echo '========== Checking out code =========='
                checkout scm
            }
        }

        stage('Run Tests') {
            steps {
                echo '========== Executing TestNG Tests =========='
                bat 'mvn clean test'
            }
        }

        stage('Archive Reports') {
            steps {
                echo '========== Archiving Test Reports =========='
                archiveArtifacts artifacts: "reports/**", allowEmptyArchive: true
                echo '✓ Reports ready for download in Build Artifacts'
            }
        }
    }

    post {
        always {
            echo '========== Cleaning Up =========='
            cleanWs()
        }

        success {
            echo '✓ Pipeline executed successfully!'
            junit testResults: '**/test-output/testng-results.xml', allowEmptyResults: true
        }

        failure {
            echo '✗ Pipeline failed! Check logs and reports above.'
            junit testResults: '**/test-output/testng-results.xml', allowEmptyResults: true
        }

        unstable {
            echo '⚠ Pipeline is unstable. Some tests may have failed.'
        }
    }
}
