@Library('terraform-lib') _  // Jenkins Shared Library name

pipeline {
  agent any

  environment {
    TF_IN_AUTOMATION = "true"
  }

  stages {
    stage('Init') {
      steps {
        terraformWrapper('init')
      }
    }

    stage('Validate') {
      steps {
        terraformWrapper('validate')
      }
    }

    stage('Plan') {
      steps {
        terraformWrapper('plan')
      }
    }

    stage('Apply') {
      when {
        branch 'main'
      }
      steps {
        terraformWrapper('apply')
      }
    }

    stage('Destroy') {
      when {
        branch 'cleanup'
      }
      steps {
        terraformWrapper('destroy')
      }
    }
  }

  post {
    success {
      echo "✅ ScyllaDB EC2 provisioning completed successfully!"
    }
    failure {
      echo "❌ Provisioning failed!"
    }
  }
}
