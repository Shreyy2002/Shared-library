@Library('shared-library@main') _

import org.cloudninja.application.java.codecompile.Javacodecompile
import org.cloudninja.application.generic.Notification

pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/OT-MICROSERVICES/salary-api.git'
        BRANCH = 'main'
        PRIORITY = 'P1'
        SLACK_CHANNEL = '#jenkins-notification'
        EMAIL_RECIPIENTS = 'shrey.tyagi.snaatak@mygurukulam.co'
    }

    stages {
        stage('Java Code Compile') {
            steps {
                script {
                    def javaBuild = new Javacodecompile(this)
                    def status = 'SUCCESS'

                    try {
                        javaBuild.cloneCode(env.REPO_URL, env.BRANCH)
                        javaBuild.compileCode()
                    } catch (Exception e) {
                        status = 'FAILURE'
                        currentBuild.result = status
                        throw e
                    } finally {
                        def notifier = new Notification(this)
                        notifier.send(status, env.PRIORITY, env.SLACK_CHANNEL, env.EMAIL_RECIPIENTS)
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/target/**/*.class', fingerprint: true
            echo 'Cleaning workspace'
            deleteDir()
        }
    }
}
