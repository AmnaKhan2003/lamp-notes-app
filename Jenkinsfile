pipeline {
    agent any

    environment {
        COMPOSE_PROJECT_NAME = 'lamp-notes-ci'
        COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/AmnaKhan2003/lamp-notes-app.git'
            }
        }

        stage('Build and Deploy Containers') {
            steps {
                script {
                    echo "🛠️ Stopping old containers (if running)..."
                    sh "docker-compose -p ${COMPOSE_PROJECT_NAME} -f ${COMPOSE_FILE} down || true"

                    echo "🧱 Building containers without cache..."
                    sh "docker-compose -p ${COMPOSE_PROJECT_NAME} -f ${COMPOSE_FILE} build --no-cache"

                    echo "🚀 Starting containers..."
                    sh "docker-compose -p ${COMPOSE_PROJECT_NAME} -f ${COMPOSE_FILE} up -d --remove-orphans"
                }
            }
        }

        stage('Run Selenium Tests') {
            steps {
                script {
                    echo "🧪 Running Selenium test cases using Maven..."
                    sh """
                    docker run --rm \
                      --network ${COMPOSE_PROJECT_NAME}_default \
                      -v \$PWD/tests-java:/project \
                      -w /project \
                      markhobson/maven-chrome mvn test
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ All test cases passed successfully!'
        }
        failure {
            echo '❌ Some test cases failed. Please check the Console Output and Test Report.'
        }
        always {
            echo '📦 Publishing test reports...'
            junit 'tests-java/target/surefire-reports/*.xml'
        }
    }
}
