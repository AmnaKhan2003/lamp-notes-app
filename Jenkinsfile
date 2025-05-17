pipeline {
    agent any

    environment {
        COMPOSE_PROJECT_NAME = 'lamp-notes-ci'
        COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git url: 'https://github.com/AmnaKhan2003/lamp-notes-app.git'
            }
        }

        stage('Build with Docker Compose') {
            steps {
                script {
                    sh "docker-compose -p ${COMPOSE_PROJECT_NAME} -f ${COMPOSE_FILE} up -d --build"
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            sh "docker-compose -p ${COMPOSE_PROJECT_NAME} -f ${COMPOSE_FILE} down"
        }
    }
}
