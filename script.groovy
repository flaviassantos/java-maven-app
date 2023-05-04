def deployApp() {
    sshagent(['ec2-server-key']) {
        echo 'deploying docker image to EC2...'
        
        def dockerComposeCmd = "docker compose -f docker-compose.yaml up --detach"
        sh "scp docker-compose.yaml ec2-user@3.64.127.118:/home/ec2-user"
        sh "ssh -o StrictHostKeyChecking=no ec2-user@3.64.127.118 ${dockerComposeCmd}"
}
}

return this
