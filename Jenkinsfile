pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        NODE_VERSION = "18"

        IMAGE_NAME = "your-registry/project-name"
        IMAGE_TAG = "${env.BUILD_NUMBER}"

        REGISTRY_URL = "your-registry-url"

        // REGISTRY_CREDENTIALS = credentials('docker-registry-creds')
    }

    stages {

        stage('Init') {
            steps {
                echo "Initializing"

                sh 'mvn --version'
                sh 'java --version'
            }
        }

        stage('Build Backend') {
            steps {
                echo "Building backend with Maven"

                sh 'mvn clean package -DskipTests'
            }

            post {
                success {
                    archiveArtifacts artifacts: 'target/**', fingerprint: true
                }
            }
        }

        stage('Docker Build') {
            steps {

                echo "Building Docker Image"

                sh """
                    docker-compose build app
                """

                // sh """
                //     docker tag scm2.0:latest ${IMAGE_NAME}:${IMAGE_TAG}
                // """
            }
        }

        // stage('Docker Push') {
        //     steps {

        //         echo "Logging into Docker Registry"

        //         sh """
        //             echo ${REGISTRY_CREDENTIALS_PSW} | docker login \
        //             -u ${REGISTRY_CREDENTIALS_USR} \
        //             --password-stdin ${REGISTRY_URL}
        //         """

        //         echo "Pushing Docker Image"

        //         sh """
        //             docker push ${IMAGE_NAME}:${IMAGE_TAG}
        //         """
        //     }
        // }
    }

    post {

        success {
            echo 'Pipeline executed successfully!'
        }

        failure {
            echo 'Pipeline failed!'
        }

        always {
            cleanWs()
        }
    }
}
