pipeline {
    agent any

    tools {
        maven 'Maven_Latest'
        jdk   'JDK_21'
    }

    options {
        timeout(time: 2, unit: 'HOURS')
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '5'))
    }

    triggers {
        githubPush()
    }

    environment {
        MAVEN_HOME = tool 'Maven_Latest'
        PATH = "${MAVEN_HOME}/bin:${PATH}"
        TEST_REPORT_DIR = "${WORKSPACE}/test-output"
        EXTENT_REPORT_DIR = "${WORKSPACE}/reports"
    }

    stages {
        stage('Checkout') {
            steps {
                echo '========== Checking out code =========='
                checkout scm
                sh 'git log --oneline -5'
            }
        }

        stage('Build') {
            steps {
                echo '========== Compiling Project =========='
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                echo '========== Executing TestNG Tests =========='
                sh 'mvn test -Dmaven.test.failure.ignore=true'
            }
        }

        stage('Archive Reports') {
            steps {
                echo '========== Archiving Test Reports =========='
                script {
                    // Archive TestNG reports
                    if (fileExists("${TEST_REPORT_DIR}")) {
                        archiveArtifacts artifacts: "test-output/**", allowEmptyArchive: true
                    }
                    // Archive Extent reports
                    if (fileExists("${EXTENT_REPORT_DIR}")) {
                        archiveArtifacts artifacts: "reports/**", allowEmptyArchive: true
                    }
                    // Archive screenshots
                    if (fileExists("${WORKSPACE}/screenshots")) {
                        archiveArtifacts artifacts: "screenshots/**", allowEmptyArchive: true
                    }
                }
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
            // Publish test results
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
