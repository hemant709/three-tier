This project demonstrates a complete CI/CD pipeline using:

* **Infrastructure Provisioning:** Terraform + Ansible
* **CI/CD Pipelines:** Jenkins
* **Containerization:** Docker
* **Orchestration:** Kubernetes (multi-node)
* **Monitoring:** Prometheus + Grafana
* **Cloud Platform:** AWS (Free Tier compatible)

---

## Install Jenkins Agent:

| Tool        | Version (min) | Required on | Purpose                         |
| ----------- | ------------- | ----------- | ------------------------------- |
| Terraform   | 1.0+          | Jenkins     | Provision AWS infra             |
| Ansible     | 2.9+          | Jenkins     | Configure K8s                   |
| AWS CLI     | 2.x           | Jenkins     | Authenticate infra, push to ECR |
| kubectl     | Latest        | Jenkins     | Deploy to K8s                   |
| Docker      | Latest        | Jenkins     | Build + push images             |
| JDK + Maven | 8+/3.8+       | Jenkins     | Build backend                   |
| jq          | Latest        | Jenkins     | Parse Terraform output          |

### AWS Configuration

* IAM user with permissions for EC2, VPC, ECR, IAM, etc.
* Access + Secret Key added to Jenkins:
  * **Jenkins > Credentials > AWS Credentials**

### 🗝 SSH Key

* Generate on Jenkins node (or your local):
  ```bash
  ssh-keygen -t rsa -b 4096 -f ~/.ssh/id_rsa
  ```
* Use public key in Terraform's `aws_key_pair` resource.

---

## 📁 Directory Structure (as implemented)

```
terraform/              # Infra scripts (VPC, EC2, etc.)
ansible/                # Ansible setup for Kubernetes
jenkins/                # Jenkins pipelines
services/               # App microservices: frontend, backend, database
monitoring/             # Prometheus & Grafana configs
```

---

## 🚀 Step-by-Step Execution

### ✅ STEP 1: Infra Provision Pipeline

1. **Create a Jenkins pipeline job** → Name it: `infra-pipeline`
2. Use `jenkins/InfraProvisionPipeline.groovy` as Pipeline Script
3. Run the job
4. This will:
   * Spin 3 EC2 `t2.micro` instances
   * Create inventory for Ansible
   * Install Kubernetes (1 master, 2 workers)

---

### ✅ STEP 2: Application Deployment Pipeline

1. **Push your project to GitHub**
2. Create another Jenkins pipeline → Name it: `app-deploy-pipeline`
3. Use `jenkins/AppDeployPipeline.groovy` as Pipeline Script
4. Set `ECR_REPO` value in environment block
5. This will:
   * Checkout code
   * Build backend with Maven
   * Dockerize and push image to AWS ECR
   * Deploy backend to Kubernetes

> 💡 You can extend this for frontend & database with additional stages.

---

## 🧪 Monitoring

You can manually deploy Prometheus and Grafana using `monitoring/prometheus_values.yaml` and `grafana_values.yaml` via Helm or kubectl.

---

## 💸 Cost Management

| Resource      | Use             | Free Tier?         | Cost-Saving Tips                      |
| ------------- | --------------- | ------------------ | ------------------------------------- |
| EC2 (3x)      | Nodes           | ✅ (750 hrs/month) | Use `t2.micro`, shut down when done |
| EBS (8GB)     | Root Volume     | ✅                 | Keep volumes under 30GB total         |
| ECR           | Image storage   | ✅                 | First 500MB free                      |
| Data Transfer | K8s pull & push | ✅                 | Limit internet usage                  |

---

## 🛑 Shutdown After Use

To stop billing:

```bash
terraform destroy
```

Or stop instances manually in AWS console.

---

## ✅ Summary

| Component      | Tool                 | Managed via                                      |
| -------------- | -------------------- | ------------------------------------------------ |
| Infrastructure | Terraform            | `main.tf`                                      |
| K8s Setup      | Ansible              | `k8s_cluster_setup.yml`                        |
| CI/CD          | Jenkins              | `InfraProvisionPipeline`,`AppDeployPipeline` |
| Docker Images  | Docker + ECR         | Jenkins                                          |
| Deployment     | kubectl + YAMLs      | Jenkins                                          |
| Monitoring     | Prometheus + Grafana | K8s                                              |

---

Would you like me to:

* Add this guide to your `README.md`?
* Zip the project and prepare a downloadable GitHub-ready package?
