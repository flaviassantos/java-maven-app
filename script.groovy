def deployApp() {
    echo 'deploying docker image to EC2...'

    def shellCmd = "bash ./server-cmds.sh ${IMAGE_NAME}"
    def ec2Instance = "ec2-user@3.64.127.118"

    sshagent(['ec2-server-key']) {
        sh "scp -o StrictHostKeyChecking=no server-cmds.sh ${ec2Instance}:/home/ec2-user"
        sh "scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2Instance}:/home/ec2-user"
        sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${shellCmd}"
    }
}
return this
