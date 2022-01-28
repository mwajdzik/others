output "instance-dns" {
  value = aws_instance.nodejs1.public_dns
}

output "private-dns" {
  value = aws_instance.nodejs1.private_dns
}
