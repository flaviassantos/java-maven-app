#!/usr/bin/env groovy

pipeline {
    agent any
    stages {
        stage('build app') {
            steps {
               script {
                   echo "building the application..."
               }
            }
        }
        stage('build image') {
            steps {
                script {
                    echo "building the docker image..."
                }
            }
        }
        stage('deploy') {
            steps {
                script {
                   echo 'deploying docker image...'
                   withKubeConfig([credentialsId: 'lke-credentials', serverUrl: 'https://f19194c5-08ff-46d2-a219-507f85ad51d2.eu-central-1.linodelke.net']) {
                       sh 'kubectl create deployment nginx-deployment --image=nginx'
                   }
                }
            }
        }
    }
}
