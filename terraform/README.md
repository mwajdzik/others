## Setup

brew install terraform

Infrastructure management tool that allows to provision and mange cloud resources.

AWS IAM - user: terraform with programmatic access (Administrative Access)
prepare credentials config file in .aws folder

## Main features

https://www.hashicorp.com/blog/managing-kubernetes-applications-with-hashicorp-terraform

- dependencies definition and waiting for them 
- outputs

## Commands

terraform init
terraform apply
terraform destroy

terraform state list
terraform state show aws_s3_bucket.tf_course

terraform show
terraform show -json

terraform graph
open http://www.webgraphviz.com/

terraform state show aws_elb.prod_web
