pipeline {
    agent any

    environment {
        GIT_REPO = "https://lab.ssafy.com/s12-webmobile1-sub1/S12P11A304.git"
        CREDENTIALS_ID = "gitlab-jenkins"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'BuildTest/Deploy_EC2', credentialsId: "${CREDENTIALS_ID}", url: "${GIT_REPO}"
            }
        }

        stage('Build Backend') {
            steps {
                dir('Back') {
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('Front') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Build and Push Docker Images') {
            steps {
                script {
                    sh 'docker build -t s12p11a304-backend:latest ./Back'
                    sh 'docker build -t s12p11a304-frontend:latest ./Front'
                    sh 'docker build -t s12p11a304-nginx:latest -f nginx/Dockerfile .'
                }
            }
        }

        stage('Deploy Containers') { 
            steps {
                script {
                    // 기존 컨테이너 강제 삭제
                    sh 'docker rm -f frontend backend nginx || true'

                    // 기존 네트워크 삭제
                    sh 'docker network prune -f || true'

                    // Docker Compose 실행
                    sh 'docker-compose down || true'
                    sh 'docker-compose up -d --build'
                }
            }
        }
    }
}
