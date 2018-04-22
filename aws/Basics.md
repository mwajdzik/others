
## Main characteristics

- High Availability - systems that are durable and likely to operate continuously without failure for a long time

- Fault Tolerance - enables a system to continue operating properly in the event of the failure of (or one or more faults within) some of its component

- Scalability - is the ability to increase in size as demand increases

- Elasticity - term commonly used to describe the ability to both grow AND shrink based on demand


## VPC - Virtual Private Cloud - a private section of AWS where we can place AWS resources, and allow/restrict access to them

## EC2 - Elastic Cloud Compute - EC2 instance (eg. Web Hosting)

## RDS - Rational Database Service (eg. user data, catalog of products)

## S3 - Simple Storage Service - a massive storage bucket 


## Examples
- Netflix - EC2/RDS/S3
- Dropbox - UI for S3


## Global Infrostructure

- a region - a collection of availability zones (2-3)
- an availability zone - a data center in a specific geographical location separated from other centers of the same region


## IAM - Identity & Access Management

- a place where we can manage AWS users and their access to accounts and services
- we can manage: users, groups, access polices, roles
- a user created when we create an AWS account is a ROOT user with FULL administrative rights and access to every part of the account
- by default any NEW user has NO access to any AWS service (except the ability to log in), permissions must be given to grant access
- for full admin access create an IAM user and attach the AdministratorAccess policy (instead of using root)

- AccountId: 179656703644
- Logging in page [https://179656703644.signin.aws.amazon.com/console]

- IAM roles are a secure way to grant permissions to entities that you trust, eg. application code running on an EC2 instance that needs to perform actions on AWS resources


- IAM policies can be directly attached to IAM roles, users and groups.
- IAM policies can only be applied to other AWS services (indirectly) through IAM roles.


## VPC - Virtual Private Cloud
- a private subsetion of AWS that we control, and in which we can place AWS resources (such as EC2 instances)
- when we create an AWS account a "default" VPC is created

### Internet Gateway - IGW - provides a route to the outside world of the VPC

### Route Table - contains a set of rules (routes), that are used to determine where network trafic is directed

### NACL - Network Access Control Lists
- an optional layer of security for a VPC that acts as a firewall for controlling trafic IN and OUT of one or more subnets
- Inbound Rules vs Outbound Rules
- by default all traffic is allowed in both directions
- rules are evaluated from lowest to highest by the rule number
- DENY all at the bottom
- a subnet can be associated only with one NACL at a time

### Subnets - a subsection of a network
- a VPC can contain one or more subnets in each Availability Zone
- a subnet cannot span multiple zones
- a public/private subnet - a public subnet has a route to the Internet, a private doesn't (a subnet is connected to a route table without IGW)
























