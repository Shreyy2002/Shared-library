package org.cloudninja.application.java.codecompile

class Javacodecompile implements Serializable {

    def steps

    Javacodecompile(steps) {
        this.steps = steps
    }

    def cloneCode(String repo, String branch) {
        steps.stage('Clone Code') {
            steps.git branch: branch, url: repo
        }
    }

    def compileCode() {
        steps.stage('Compile Code') {
            steps.sh 'mvn clean compile'
        }
    }
}
