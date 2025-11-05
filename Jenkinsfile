pipeline {
    agent any

    environment {
        BASE_URL    = credentials('baseurl-env')
        TEST_USER   = credentials('testuser-env')
        TEST_PASS   = credentials('testpass-env')
        BROWSER     = 'chrome'
        ENVIRONMENT = 'qa'
    }

    tools {
        maven 'Maven_3.9'
        jdk 'jdk17'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/VrindaNagarro/TeamCodeNova.git',
                    credentialsId: 'github-token'
            }
        }

        stage('Build & Clean') {
            steps {
                echo "🧹 Cleaning previous builds..."
                sh 'mvn clean'
            }
        }

        stage('Run Tests') {
            steps {
                echo "🚀 Running Selenium & API Tests..."
                sh '''
                    mvn test \
                        -Denvironment=${ENVIRONMENT} \
                        -DbaseUrl=${BASE_URL} \
                        -Dusername=${TEST_USER} \
                        -Dpassword=${TEST_PASS} \
                        -Dbrowser=${BROWSER}
                '''
            }
        }

        stage('Publish Reports') {
            steps {
                echo "📄 Publishing Test Reports..."
                junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                archiveArtifacts artifacts: 'target/surefire-reports/**/*.*', fingerprint: true
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'test-output',   // Path where TestNG generates HTML
                    reportFiles: 'index.html', // More reliable than index.html
                    reportName: 'TestNG HTML Report'
                ])
            }
        }
    }

    post {
        always {
            echo "📁 Archiving additional reports..."
            archiveArtifacts artifacts: 'test-output/**/*.html', fingerprint: true
        }
        success {
            echo "✅ Build succeeded!"
        }
        failure {
            echo "❌ Build failed!"
        }
    }
}
