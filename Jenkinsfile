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
                    // Pehle agar containers chal rahe hain to unhe gracefully stop karo
                    sh "docker-compose -p ${COMPOSE_PROJECT_NAME} -f ${COMPOSE_FILE} down || true"
                    
                    // Fresh build without cache
                    sh "docker-compose -p ${COMPOSE_PROJECT_NAME} -f ${COMPOSE_FILE} build --no-cache"
                    
                    // Containers ko background me start karo, orphans remove karte hue
                    sh "docker-compose -p ${COMPOSE_PROJECT_NAME} -f ${COMPOSE_FILE} up -d --remove-orphans"
                }
            }
        }

        stage('Run Selenium Tests') {
            steps {
                script {
                    // Run tests using maven-chrome image
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

}
