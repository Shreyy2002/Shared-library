@Library('your-shared-library') _

pipeline {
    agent any
    stages {
        stage('Run Build Pipeline') {
            steps {
                script {
                    def pipeline = new BuildPipeline(this)
                    pipeline.run([
                        gitRepo: 'https://github.com/OT-MICROSERVICES/salary-api.git',
                        branch: 'main',
                        priority: 'P1',
                        slackChannel: '#jenkins-notification',
                        emailRecipients: 'shrey.tyagi.snaatak@mygurukulam.co'
                    ])
                }
            }
        }
    }
}
