pipeline {
  agent any
  environment {
    TF_DIR = "terraform"
    ANSIBLE_DIR = "ansible"
  }
  stages {
    stage('Terraform Init & Apply') {
      steps {
        dir(env.TF_DIR) {
          sh 'terraform init'
          sh 'terraform apply -auto-approve'
        }
      }
    }

    stage('Generate Ansible Inventory') {
      steps {
        script {
          def ips = sh(
            script: "terraform -chdir=${env.TF_DIR} output -json node_ips | jq -r '.[]'",
            returnStdout: true
          ).trim().split("\n")

          def master = ips[0]
          def workers = ips.drop(1)

          def inventory = "[masters]\n${master}\n\n[workers]\n${workers.join('\n')}\n"
          writeFile file: "${env.ANSIBLE_DIR}/inventory.ini", text: inventory
        }
      }
    }

    stage('Run Ansible Playbook') {
      steps {
        dir(env.ANSIBLE_DIR) {
          sh 'ansible-playbook -i inventory.ini k8s_cluster_setup.yml'
        }
      }
    }
  }
}
