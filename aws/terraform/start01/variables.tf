variable "aws_access_key" {
  default = "!!! define it in tfvars !!!"
}

variable "aws_secret_key" {
  default = "!!! define it in tfvars !!!"
}

variable "ssh_key_name" {
  default = "!!! define it in tfvars !!!"
}

variable "private_key_path" {
  default = "!!! define it in tfvars !!!"
}

variable "deploy_environment" {
  default = "DEV"
}

variable "region" {
  default = "us-west-2"
}

variable "vpc_cidr" {
  default = "172.16.0.0/16"
}

variable "subnet1_cidr" {
  default = "172.16.0.0/24"
}

variable "environment_list" {
  type    = list(string)
  default = ["DEV", "QA", "STAGE", "PROD"]
}

variable "environment_map" {
  type    = map(string)
  default = {
    "DEV"   = "DEV",
    "QA"    = "QA",
    "STAGE" = "STAGE",
    "PROD"  = "PROD"
  }
}

variable "environment_instance_type" {
  type    = map(string)
  default = {
    "DEV"   = "t2.micro",
    "QA"    = "t2.micro",
    "STAGE" = "t2.micro",
    "PROD"  = "t2.micro"
  }
}

variable "environment_instance_settings" {
  type = map(object({
    instance_type = string,
    monitoring    = bool
  }))

  default = {
    "DEV"   = {
      instance_type = "t2.micro",
      monitoring    = false
    },
    "QA"    = {
      instance_type = "t2.micro",
      monitoring    = false
    },
    "STAGE" = {
      instance_type = "t2.micro",
      monitoring    = false
    },
    "PROD"  = {
      instance_type = "t2.micro",
      monitoring    = true
    }
  }
}

variable "iam_accounts" {
  type    = set(string)
  default = ["Bob", "Mary", "Joe"]
}
