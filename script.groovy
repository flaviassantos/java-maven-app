def deployApp() {
    sshagent(['ec2-server-key']) {
        def dockerCmd = "docker run -p 8080:8080 -d ${IMAGE_NAME}"
        sh "ssh -o StrictHostKeyChecking=no ec2-user@3.64.127.118 ${dockerCmd}"
}
}

return this
