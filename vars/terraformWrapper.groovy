def call(String action = 'apply') {
    script {
        echo " Running Terraform Wrapper: ${action.toUpperCase()}"

        sh 'chmod +x scripts/terraform-wrapper.sh'
        sh "./scripts/terraform-wrapper.sh ${action}"
    }
}
