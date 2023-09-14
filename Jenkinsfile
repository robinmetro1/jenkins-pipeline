pipeline {
    agent any
    tools {
        maven 'maven-3.9.4' 
        nodejs 'nodejs'
    }
    /***
    properties(
    [
        [$class: 'BuildDiscarderProperty', strategy:
          [$class: 'LogRotator', artifactDaysToKeepStr: '14', artifactNumToKeepStr: '5', daysToKeepStr: '30', numToKeepStr: '60']],
        pipelineTriggers(
          [
              pollSCM('* * * * *'),
              cron('@daily'),
          ]
        )
    ]
     )***/
    environment {
        BACK_DOCKER_IMAGE = "eyaea/devops-back"
        FRONT_DOCKER_IMAGE = "eyaea/devops-front"
        DOCKER_REGISTRY = "your-docker-registry-url"
        DOCKER_TAG = "latest"
        DOCKER_CREDENTIALS_ID = 'dockerhub'
        SONARQUBE_CREDENTIALS_ID = 'sonartoken'
    }
    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }
/***
        stage('Build frontend app') {
            steps {
                dir('front') {
                    sh 'npm install '
                    sh 'npm run build --prod'

                }
            }
            post {
                success {
                    echo "Success: front build completed"
                }
                failure {
                    echo "Failed: front build"
                }
            }
        }
        stage('SonarQube analysis for spring app') {
            steps{
               dir('/var/jenkins_home/workspace/devops-test/back') {
                withSonarQubeEnv(credentialsId: 'sonartoken') {

                 }
           }
        }
        } ***/

        stage('Build Maven') {
            steps {
                dir('back') {
                    sh 'mvn clean package'
                }
            }
            post {
                success {
                    echo "Success: Maven build completed"
                }
                failure {
                    echo "Failed: Maven build"
                }
            }
        }
  
        stage('Build Docker Images') {
            steps {
                script {
                    dir('back') {
                        // Build your Docker image
                        def dockerImageBack = docker.build("${BACK_DOCKER_IMAGE}", ".")

                    }
                    dir('front') {
                        // Build your Docker image
                        def dockerImageFront = docker.build("${FRONT_DOCKER_IMAGE}", ".")

                    }

                }
            }
        }
        stage('Push Docker Images') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_CREDENTIALS_ID) {
                        def dockerImageBack = docker.image(env.BACK_DOCKER_IMAGE)
                        dockerImageBack.push("${env.BUILD_ID}")
                         def dockerImageFront = docker.image(env.FRONT_DOCKER_IMAGE)
                        dockerImageFront.push("${env.BUILD_ID}")
                    }
                }
            }
            post {
                always {
                    script {
                        sh 'docker logout'
                    }                
                }
            }
        }
        /***
        stage('Deploy to Kubernetes') {
            steps{
                script{
                    withKubeCredentials(kubectlCredentials: [[caCertificate: '', clusterName: '', contextName: '', credentialsId: 'kubeconfig', namespace: '', serverUrl: '']]) {
                   dir('back') {
                   sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
                   sh 'chmod u+x ./kubectl'  
                   sh './kubectl apply -f k8s/deployment-service.yaml'
                   sh './kubectl get pods'
                    }

                }
                }
            }
          
            
        }**/
        
        stage('Test') {
            steps {
                echo "Running tests..."
                // Add test steps here
            }
        }
    }
}
