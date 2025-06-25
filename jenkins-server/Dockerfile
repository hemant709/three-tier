FROM jenkins/jenkins:lts
USER root

RUN apt-get update && \
    apt-get install -y docker.io unzip jq ansible maven awscli curl && \
    curl -Lo /usr/local/bin/terraform.zip https://releases.hashicorp.com/terraform/1.5.7/terraform_1.5.7_linux_amd64.zip && \
    unzip /usr/local/bin/terraform.zip -d /usr/local/bin && \
    rm /usr/local/bin/terraform.zip && \
    curl -Lo /usr/local/bin/kubectl $(curl -s https://dl.k8s.io/release/stable.txt | sed 's|^|https://dl.k8s.io/release/|;s|$|/bin/linux/amd64/kubectl|') && \
    chmod +x /usr/local/bin/kubectl && \
    rm -rf /var/lib/apt/lists/*

COPY plugins.yaml /usr/share/jenkins/ref/plugins.yaml
RUN jenkins-plugin-cli --plugins-file /usr/share/jenkins/ref/plugins.yaml