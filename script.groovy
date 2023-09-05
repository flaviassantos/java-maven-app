def incrementVersion(){
    echo 'incrementing app version...'
    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                        versions:commit'
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = matcher[0][1]
    env.IMAGE_VERSION = "$version-$BUILD_NUMBER"
}
return this


def deployApp() {
    echo 'deploying docker image...'
    sh 'envsubst < kubernetes/deployment.yaml | kubectl apply -f -'
    sh 'envsubst < kubernetes/service.yaml | kubectl apply -f -'
}
return this


def commitVersionUpdate(){
    script {
        withCredentials([usernamePassword(credentialsId: 'github-token-as-pwd', passwordVariable: 'GITHUB_TOKEN', usernameVariable: 'USER')]) {
            // git config here for the first time run
            sh 'git config --global user.email "jenkins@example.com"'
            sh 'git config --global user.name "jenkins"'

            sh 'git status'
            sh 'git config --list'

            sh "git remote set-url origin https://${GITHUB_TOKEN}@github.com/${USER}/java-maven-app.git"
            sh 'git add .'
            sh 'git commit -m "ci: version bump"'
            sh 'git push origin HEAD:master'
        }
    }
}
return this
