pipeline {
    agent any
    tools {
        maven 'maven-3.9.4' 
        nodejs 'nodejs'
    }
    environment {
        DOCKER_IMAGE = "eyaea/devops-demo"
        DOCKER_TAG = "latest"
        DOCKER_REGISTRY = "your-docker-registry-url"
        DOCKER_CREDENTIALS_ID = 'dockerhub'
        SONARQUBE_CREDENTIALS_ID = 'sonartoken'
    }
    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build node') {
            steps {
                dir('front') {
                    sh 'npm install '
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
        /***stage('SonarQube analysis for spring app') {
            steps{
               dir('/var/jenkins_home/workspace/devops-test/back') {
                withSonarQubeEnv(credentialsId: 'sonartoken') {

                 }
           }
        }
        }

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
    
        stage('Build Docker Image') {
            steps {
                script {
                    dir('back') {
                        // Build your Docker image
                        def dockerImage = docker.build("${DOCKER_IMAGE}:${BUILD_TAG}", ".")
                    }
                }
            }
        }
       
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_CREDENTIALS_ID) {
                        def dockerImage = docker.image(env.DOCKER_IMAGE)
                        dockerImage.push("${BUILD_TAG}")
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
        }***/
        
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
          
            
        }
        
        stage('Test') {
            steps {
                echo "Running tests..."
                // Add test steps here
            }
        }
    }
}