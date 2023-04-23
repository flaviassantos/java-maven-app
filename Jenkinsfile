#!/usr/bin/env groovy

pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage('build jar') {
            steps {
                script {
                    echo "Building the application..."
                    sh "mvn package"
                }
            }
        }
        stage('build image') {
            steps {
                script {
                    echo "Building the docker image..."
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable:'PASS', usernameVariable: 'USER')]){
                        sh "docker build -t flaviassantos/my-repo:jma-2.0 ."
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh "docker push flaviassantos/my-repo:jma-2.0"
                    }
                }
            }
        }
        stage('deploy') {
            steps {
                script {
                    echo "Deploying the application..."
                }
            }
        }
    }
}
