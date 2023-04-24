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
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
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
            when {
                expression {
                    BRANCH_NAME == 'master'
                }
            }
            steps {
                script {
                    buildJar()
                }
            }
        }
        stage("build image") {
            steps {
                script {
                    buildImage('flaviassantos/my-repo:jma-3.0')
                    dockerLogin()
                    dockerPush('flaviassantos/my-repo:jma-3.0')
                }
            }
        }
        stage("deploy") {
            when {
                expression {
                    BRANCH_NAME == 'master'
                }
            }
            steps {
                script {
                    gv.deployApp()
                }
            }
        }
    }
}