output "node_ips" {
  value = aws_instance.k8s_nodes[*].public_ip
}
