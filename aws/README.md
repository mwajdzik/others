
aws sts get-caller-identity

aws ec2 create-key-pair --key-name terraform_key --query 'KeyMaterial' --output text > terraform_key.pem

terraform apply -var-file=./terraform.tfvars

terraform destroy -var-file=./terraform.tfvars

chmod 400 terraform_key.pem

ssh -i terraform_key.pem ec2-user@54.214.96.116
