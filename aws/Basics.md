
## Main characteristics

- High Availability - systems that are durable and likely to operate continuously without failure for a long time

- Fault Tolerance - enables a system to continue operating properly in the event of the failure of (or one or more faults within) some of its component

- Scalability - is the ability to increase in size as demand increases

- Elasticity - term commonly used to describe the ability to both grow AND shrink based on demand


## VPC - Virtual Private Cloud - a private section of AWS where we can place AWS resources, and allow/restrict access to them

## EC2 - Elastic Cloud Compute - EC2 instance (eg. Web Hosting)
- EBS - local storage, Security Group - a firewall
- purchase options
	- on-demand - most expensive
	- reserved - allows to purchase an instance for a set time period of 1 or 3 years
	- spot - allows to bid on an instance type and pay for it only if the price is less or equal to our bid
- instance type 
	- general purpose
	- compute optimized
	- GPU optimized
	- memory optimized
	- storage optimized
- AMI - Amazon Machine Image
	- preconfigured package required to launch EC2 (includes OS, software packages, required settings)
- EBS - Elastic Block Store - a storage volume for an EC2 instance
	- IOPS - Input/Output Operations per Second
	- the amount of data thta can be written/retrieved from EBS per second
	- every EC2 must have a ROOT volume, which may or may not be EBS
	- by default EBS root vaules are deleted when the instance is terminated (this can be changed)
	- when creating an instance, or afterwards, we can add any additional EBS volumes
	- any additional volume may be attached or detached from an instance at any time and is NOT deleted (by default) when the instance is terminated
	- Snapshots are images of EBS volumes that can be stored as backups, to restore a snapshot we create a new EBS and use a snapshot as a template
- Security Groups - allow/deny traffic
	- defined on the INSTANCE level (instead of subnet level as in case of NACLs)
	- all rules are evaluated (it doesn't go from top to the bottom)
	- by default when a new SG gets created ALL inbound traffic is denied and ALL outbound traffic is allowed
	- all traffic is denied unless there is an explicit allow rule for it
	- we can only configure ALLOW rules for security groups, if there is not an explicit allow rules for a certain traffic type, then that traffic type will be denied.
- IPs
	- by default all EC2 instances have private addresses that allow the comunication within the same VPC
	- EC2 can be launched with public IPs based on VPC/subnet settings
	- public IP addresses are required for the instance to communicate with the Internet

- What is needed for an instance to communicate with the Internet:
	- public IP address
	- security group with ALLOW rule
	- NACL with ALLOW rule
	- Route Table with IGW as a route
	- Internet Gateway attached to the VPC

- Launching an EC2 instance
	- select an AMI and an instance type
	- configure instance details
		- script:
			#!/bin/bash
			yum update -y
			yum install -y httpd
			service httpd start


## RDS - Rational Database Service (eg. user data, catalog of products)
 - options:
 	- Amazon Aurora, MySQL, MariaDB, PostreSQL, Oracle, MsSQL

 - within RDS we create a subnet group that contains our two private subnets
 - MySQL Workbench can be used to SSH tunnel to the DB in a private network


## DynamoDB - NoSQL DB from Amazon


## S3 - Simple Storage Service - a massive storage bucket 
- a bucket name must be unique accross AWS!!!
- storage class:
	- standard (default, durability=11 nines, availability=99.99%, expensive)
	- RRS - reduced redundancy storage (durability=99.99%, availability=99.99%) - can be used if object is reproducable
	- S3-IA - infrequent access (durability=11 nines, availability=99.90%)
	- glacier (durability=11 nines, make take serveral hours to retrieve data, for archiving, change done thru object lifecycle, may take 1-2 days to take effect)
- object lifecycle - a set of rules that automates the migration of an object's storage class to a different one based on specified time intervals, can be set on a bucket, folder, object level
- permisions - allows to specify how can view, access, use specific buckets and objects
- object versioning - keeps track of old and new versions of an object
	- by default turned off
	- once turned on, cannot be turned off, only suspended


## SNS - Simple Notification Service
- a service that allows to automate the sending of email or text message notifications based on events that happen in the AWS account
- eg. for billing and EC2 crash notifications
- TOPICS - labels for groups of different endpoints that we send messeges to
- SUBSCRIPTIONS - the endpoints that messages are sent to (eg. email or phone number of an admin)
- PUBLISHERS - the human/alarm/event that gives SNS the message that needs to be send
- eg. create a topic MyAppSNSBilling, add subscriptions (protocols: email, AWS SQS, AWS Lambda, SMS, ...)


## CloudWatch
- a service that allows to monitor various elements of the AWS account
- eg. can be used for:
	- EC2 - CPU utilization, status check, disk read/writes
	- S3 - number of objects, bucket size
	- billing
- based on the metrics, we can set thresholds to trigger alarms
- CloudWatch alarms can trigger SNS topics
- three states of an alarm: ALARM, INSUFFICENT DATA, OK


## ELB - Elastic Load Balancer
- evenly distributes traffic between EC2 instances
- not available in Free Tier
- configuration under EC2


## Autoscaling
- automates the process of adding or removing EC2 instances based on traffic demand for the application


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




