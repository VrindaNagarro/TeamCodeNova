pipeline {
    agent any

    environment {
        // 🌍 Global environment variables (can be overridden from Jenkins UI)
        BASE_URL    = credentials('baseurl-env')      // or use System.getenv('BASE_URL')
        TEST_USER   = credentials('testuser-env')
        TEST_PASS   = credentials('testpass-env')
        BROWSER     = 'chrome'
        ENVIRONMENT = 'qa'
    }

    tools {
        maven 'Maven_3.9'   // Make sure Maven_3.9 is installed under Jenkins → Global Tool Configuration
        jdk 'jdk17'         // Your configured JDK in Jenkins
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

        stage('Publish Extent Report') {
            steps {
                echo "📊 Publishing Extent Report..."
                publishHTML([
                    reportDir: 'target',
                    reportFiles: 'extent-report.html',
                    reportName: 'Extent Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
                ])
            }
        }
    }

    post {
        always {
            echo "📁 Archiving reports..."
            archiveArtifacts artifacts: 'target/**/*.html', fingerprint: true
        }
        success {
            echo "✅ Build succeeded!"
        }
        failure {
            echo "❌ Build failed!"
        }
    }
}
