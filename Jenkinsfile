#!/usr/bin/env groovy

library identifier: 'jenkins-shared-library@master', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/flaviassantos/jenkins-shared-library.git',
         credentialsId: 'gitlab-credentials'
        ]
)

def gv

pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    environment {
        APP_NAME = 'java-maven-app' // 'my-repo'
        IMAGE_PULL_SECRETS = 'aws-registry-key' // 'my-registry-key'
        CREDENTIALS_SERVER = 'ecr-credentials' // 'docker-hub-credentials'
        DOCKER_REPO_SERVER = '716187863110.dkr.ecr.eu-central-1.amazonaws.com' // ''
        DOCKER_REPO_ID = '716187863110.dkr.ecr.eu-central-1.amazonaws.com' // 'flaviassantos'
        DOCKER_REPO = "${DOCKER_REPO_ID}/${APP_NAME}"

    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage('increment version') {
            steps {
                script {
                    gv.incrementVersion()
                }
            }
        }
        stage('test') {
            steps {
                script {
                    echo "Testing the application..."
                    echo "Executing pipeline for ${BRANCH_NAME}"
                }
            }
        }
        stage("build jar") {
            steps {
                script {
                    buildJar()
                }
            }
        }
        stage("build image") {
            environment {
                IMAGE_NAME = "$DOCKER_REPO:${env.IMAGE_VERSION}"
            }
            steps {
                script {
                    echo "building the docker image..."
                    withCredentials([usernamePassword(credentialsId: CREDENTIALS_SERVER, passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "docker build -t ${DOCKER_REPO}:${IMAGE_NAME} ."
                        sh "echo $PASS | docker login -u $USER --password-stdin ${DOCKER_REPO_SERVER}"
                        sh "docker push ${DOCKER_REPO}:${IMAGE_NAME}"
                    }
                }
            }
//             steps {
//                 script {
//                     buildImage(IMAGE_NAME)
//                     dockerLogin(CREDENTIALS_SERVER)
//                     dockerPush(IMAGE_NAME)
//                 }
//             }
        }
        stage("deploy") {
//             when {
//                 expression {
//                     BRANCH_NAME == 'master' | BRANCH_NAME == "test"
//                 }
//             }
            environment {
                AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
                AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
            }
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
        stage('commit version update'){
            steps {
                script {
                    gv.commitVersionUpdate()
                }
            }
        }
    }
}
