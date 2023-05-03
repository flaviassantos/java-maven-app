def deployApp() {
    sshagent(['ec2-server-key']) {
        def dockerCmd = "docker run -p 3080:3080 -d flaviassantos/react-nodejs-app:2.0"
        sh "ssh -o StrictHostKeyChecking=no ec2-user@3.64.127.118 ${dockerCmd}"
}
}

return this
