## Setup

brew install terraform

Infrastructure management tool that allows to provision and mange cloud resources.

AWS IAM - user: terraform with programmatic access (Administrative Access)
prepare credentials config file in .aws folder

## Main features

https://www.hashicorp.com/blog/managing-kubernetes-applications-with-hashicorp-terraform
https://hashicorp.github.io/field-workshops-terraform/slides/gcp/terraform-oss
https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/compute_instance

- dependencies definition and waiting for them 
- outputs

## Commands

terraform init
terraform plan

terraform apply
terraform destroy

terraform state list
terraform state show aws_s3_bucket.tf_course

terraform show
terraform show -json

terraform graph
open http://www.webgraphviz.com/

terraform state show aws_elb.prod_web
