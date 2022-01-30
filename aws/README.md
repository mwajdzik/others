
aws sts get-caller-identity

aws ec2 create-key-pair --key-name terraform_key --query 'KeyMaterial' --output text > terraform_key.pem

terraform plan -var-file=./terraform.tfvars

terraform apply -var-file=./terraform.tfvars

terraform show

terraform destroy -var-file=./terraform.tfvars

---

chmod 400 terraform_key.pem

ssh -i terraform_key.pem ec2-user@54.214.96.116

---

export TF_VAR_aws_access_key=[value]

-var aws_access_key=[value]

-var-file file

---

terraform console

1 < 2 ? 1 : 2

length("string")
upper("string")
split(",", "string1,string2,string3")
join("&", split(",", "string1,string2,string3"))

terraform show

length(aws_instance.nodejs1.subnet_id)
join(",", data.aws_availability_zones.available.names)

---



