FROM jenkins/jenkins:lts
USER root

RUN apt-get update && \
    apt-get install -y docker.io unzip jq ansible maven awscli curl && \
    curl -Lo /usr/local/bin/terraform.zip https://releases.hashicorp.com/terraform/1.5.7/terraform_1.5.7_linux_amd64.zip && \
    chmod +x /usr/local/bin/terraform && \
    unzip /usr/local/bin/terraform.zip -d /usr/local/bin && \
    rm /usr/local/bin/terraform.zip && \
    curl -Lo /usr/local/bin/kubectl https://dl.k8s.io/release/v1.30.0/bin/linux/amd64/kubectl && \
    chmod +x /usr/local/bin/kubectl && \
    rm -rf /var/lib/apt/lists/*

COPY plugins.yaml /usr/share/jenkins/ref/plugins.yaml
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.yaml