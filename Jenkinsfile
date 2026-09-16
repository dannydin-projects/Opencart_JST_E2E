pipeline {
    agent any

    tools {
        maven 'Maven 3.9'
        jdk   'jdk22'
    }

    options {
        timeout(time: 2, unit: 'HOURS')
        buildDiscarder(logRotator(
            numToKeepStr: '10',
            artifactNumToKeepStr: '5'
        ))
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

                // Don't fail the pipeline immediately when tests fail.
                // This allows Jenkins to publish the reports.
                bat 'mvn clean test'
            }
        }

        stage('Publish Reports') {
            steps {
                echo '========== Publishing Test Reports =========='

                // Archive the complete report directory.
                archiveArtifacts(
                    artifacts: 'reports/**/*',
                    allowEmptyArchive: true,
                    fingerprint: true
                )

                // Publish HTML report inside Jenkins.
                publishHTML([
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'reports',
                    reportFiles: 'index.html',
                    reportName: 'HTML Test Report',
                    reportTitles: 'Test Execution Report'
                ])

                echo '✓ Reports published successfully'
            }
        }
    }

    post {

        always {
            echo '========== Publishing Test Results =========='

            // Publish TestNG/JUnit XML results
            junit(
                testResults: '**/test-output/testng-results.xml',
                allowEmptyResults: true
            )
        }

        success {
            echo '✓ Pipeline executed successfully!'
        }

        failure {
            echo '✗ Pipeline failed! Check the Jenkins reports and console logs.'
        }

        unstable {
            echo '⚠ Pipeline is unstable. Some tests may have failed.'
        }

        cleanup {
            echo '========== Cleaning Up =========='
            cleanWs()
        }
    }
}