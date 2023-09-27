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
        checkout([
            $class: 'GitSCM',
            branches: [
                [
                    name: '*/main'
                ],
                [
                    name: 'develop'
                ]
            ],
            doGenerateSubmoduleConfigurations: false,
            extensions: [],
            submoduleCfg: [],
            userRemoteConfigs: [
                [
                    url: 'https://github.com/robinmetro1/jenkins-pipeline.git'
                ]
            ]
        ])
        echo "Current branch is: ${env.BRANCH_NAME}"
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
        }**/
    
        stage('SonarQube analysis for spring app') {
            when {
    expression { currentBuild.branch == 'main' || currentBuild.branch == 'develop' }
}
            steps{
                   dir('back') {

                withSonarQubeEnv(credentialsId: 'sonartoken',installationName: 'sonarserver') {
                      echo "${env.SONAR_HOST_URL}"
                            //sh 'mvn clean package sonar:sonar'
                         sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'
                 }
            }
           }
        
        } 
      
        /**stage("Quality Gate") {
            steps {
              timeout(time: 1, unit: 'HOURS') {
                waitForQualityGate abortPipeline: true
              }
            }
          }**/
 
        stage('Build Maven') {
            when {
    expression { currentBuild.branch == 'main' || currentBuild.branch == 'develop' }
}
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
           when {
        expression { currentBuild.branch == 'main' }
            }
            steps {
                script {
                        def currentBranch = env.BRANCH_NAME
                         echo "Current branch is: ${currentBranch}"
                    dir('back') {
                        // Build your Docker image
                        def dockerImageBack = docker.build("${BACK_DOCKER_IMAGE}", ".")

                    }/**
                    dir('front') {
                        // Build your Docker image
                        def dockerImageFront = docker.build("${FRONT_DOCKER_IMAGE}", ".")

                    }**/

                }
            }é 
        }

        stage('Push Docker Images') {
           when {
        expression { currentBuild.branch == 'main' }
            }
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_CREDENTIALS_ID) {
                        def dockerImageBack = docker.image(env.BACK_DOCKER_IMAGE)
                        dockerImageBack.push("${env.DOCKER_TAG}")
                        /**
                         def dockerImageFront = docker.image(env.FRONT_DOCKER_IMAGE)
                        dockerImageFront.push("${env.DOCKER_TAG}")**/
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

        stage('Deploy to Kubernetes') {
            when {
        expression { currentBuild.branch == 'main' }
            }
            steps{
                script{
                    withKubeCredentials(kubectlCredentials: [[caCertificate: '', clusterName: '', contextName: '', credentialsId: 'kubeconfig', namespace: '', serverUrl: '']]) {
                   dir('back/k8s') {
                   sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
                   sh 'chmod u+x ./kubectl'  
                   sh './kubectl get pods'
                   sh './kubectl apply -f mongo-configmap.yaml'
                   sh './kubectl apply -f mongo-secret.yaml'
                   sh './kubectl apply -f mongo.yaml'
                   sh './kubectl apply -f deployment-service.yaml'
                    }
                }
                }
            }
          
            
        }
        
    
    }
 }

