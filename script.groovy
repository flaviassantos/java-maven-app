def incrementVersion(){
    echo 'incrementing app version...'
    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                        versions:commit'
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = matcher[0][1]
    env.IMAGE_NAME = "flaviassantos/my-repo:$version-$BUILD_NUMBER"
}
return this


def deployApp() {
    echo 'deploying docker image to EC2...'

    //def shellCmd = "bash ./server-cmds.sh ${IMAGE_NAME}"
    //def ec2Instance = "ec2-user@3.64.127.118"

    //sshagent(['ec2-server-key']) {
    //    sh "scp -o StrictHostKeyChecking=no server-cmds.sh ${ec2Instance}:/home/ec2-user"
    //    sh "scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2Instance}:/home/ec2-user"
    //   sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${shellCmd}"
    //}
}
return this


def commitVersionUpdate(){
    script {
        withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            // git config here for the first time run
            sh 'git config --global user.email "jenkins@example.com"'
            sh 'git config --global user.name "jenkins"'

            //sh 'git status'
            //sh 'git config --list'

            sh "git remote set-url origin https://${USER}:${PASS}@github.com/flaviassantos/java-maven-app.git"
            sh 'git add .'
            sh 'git commit -m "ci: version bump"'
            sh 'git push origin HEAD:version-increment'
        }
    }
}
return this
