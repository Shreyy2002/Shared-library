class Javacodecompile implements Serializable {

    def steps

    Javacodecompile(steps) {
        this.steps = steps
    }

    def run(Map config) {
        def buildStatus = 'SUCCESS'

        steps.node {
            try {
                steps.stage('Clone Code') {
                    steps.git branch: config.branch, url: config.gitRepo
                }

                steps.stage('Compile Code') {
                    steps.sh 'mvn clean compile'
                }

            } catch (Exception e) {
                buildStatus = 'FAILURE'
                steps.currentBuild.result = 'FAILURE'
                throw e
            } finally {
                steps.stage('Notify') {
                    notify(
                        buildStatus,
                        config.priority,
                        config.slackChannel,
                        config.emailRecipients
                    )
                }
            }
        }
    }

    private def notify(status, priority, slackChannel, emailRecipients) {
        def icons = [SUCCESS: '🟢', FAILURE: '🔴']
        def results = [
            P0: [SUCCESS: 'Urgent task completed successfully! ✅', FAILURE: 'Urgent task FAILED! 🚨'],
            P1: [SUCCESS: 'Important task completed successfully! ✅', FAILURE: 'Important task FAILED! ⚠️'],
            P2: [SUCCESS: 'Standard task completed! ✅', FAILURE: 'Standard task FAILED. ❗']
        ]
        def colors = [SUCCESS: 'good', FAILURE: 'danger']
        def subjects = [
            SUCCESS: "${priority} SUCCESS: '${steps.env.JOB_NAME} [${steps.env.BUILD_NUMBER}]'",
            FAILURE: "${priority} FAILURE: '${steps.env.JOB_NAME} [${steps.env.BUILD_NUMBER}]'"
        ]
        def buildTime = new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('Asia/Kolkata'))
        def triggeredBy = steps.currentBuild.getBuildCauses().find { it.userId }?.userName ?: "Automated/Unknown"

        def slackMessage = """
${icons[status]} *${priority} Build ${status}*
*Status:* ${results[priority][status]}
*Project:* `${steps.env.JOB_NAME}`
*Build Number:* #${steps.env.BUILD_NUMBER}
*Triggered By:* ${triggeredBy}
*Time (IST):* ${buildTime}
🔗 *Build URL:* <${steps.env.BUILD_URL}|Click to view>
"""

        def emailBody = """
<html>
  <body>
    <p><strong>${icons[status]} ${priority} Build ${status}</strong></p>
    <p><strong>Status:</strong> ${results[priority][status]}</p>
    <p><strong>Project:</strong> ${steps.env.JOB_NAME}</p>
    <p><strong>Build Number:</strong> #${steps.env.BUILD_NUMBER}</p>
    <p><strong>Triggered By:</strong> ${triggeredBy}</p>
    <p><strong>Time (IST):</strong> ${buildTime}</p>
    <p><strong>Build URL:</strong> <a href="${steps.env.BUILD_URL}">${steps.env.BUILD_URL}</a></p>
  </body>
</html>
"""

        steps.slackSend(channel: slackChannel, color: colors[status], message: slackMessage)
        steps.emailext(to: emailRecipients, subject: subjects[status], body: emailBody, mimeType: 'text/html')
    }
}
