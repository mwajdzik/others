//
// terraform show to see all values
//

output "instance-dns" {
  value = aws_instance.node_instances.*.public_dns
}

output "private-dns" {
  value = aws_instance.node_instances.*.private_dns
}

output "availability_zone" {
  value = aws_instance.node_instances.*.availability_zone
}

output "subnet_id" {
  value = aws_instance.node_instances.*.subnet_id
}
