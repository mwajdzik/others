
## Main characteristics

- *High Availability* - systems that are durable and likely to operate continuously without failure for a long time (= the app should be always accesable)

- *Fault Tolerance* - enables a system to continue operating properly in the event of the failure of (or one or more faults within) some of its component (= can handle error, repair itself if needed)

- *Scalability* - is the ability to increase in size as demand increases

- *Elasticity* - term commonly used to describe the ability to both grow AND shrink based on demand



## Global Infrostructure

- a region - a collection of availability zones (2-4)
- an availability zone - a collection of data centers (1-6) in a specific geographical location separated from other AZ of the same region, AZ's have direct low letency connections between each AZ in a region, each AZ is isolated from other AZ's to ensure fault tolerance
- an edge location - is an AWS datacenter which does not contain AWS services, it is used to deliver content to parts of the world (eg. CloudFront which is a CDN - Content Delivery)



## EC2 - Elastic Cloud Compute - EC2 instance (eg. Web Hosting)

- provides scalable virtual servers in the cloud
- EBS - local storage, Security Group (required!) - a firewall 

- purchase options:
	- on-demand - most expensive, most flexible
	- reserved - allows to purchase an instance for a set time period of 1 or 3 years
	- spot - allows to bid on an instance type and pay for it only if the price is less or equal to our bid, charged by hour (conditions)

- instance type 
	- general purpose - t2, m3
	- compute optimized - c3/c4 - high traffic web servers
	- GPU optimized - g2, p2 - for machine lerning, rendering, high performance DB
	- memory optimized - r3/r4, x1 - DB, memcache
	- storage optimized - d2, i2 - large scale data werehouses

- AMI - Amazon Machine Image
	- preconfigured package required to launch EC2 (includes OS, software packages, required settings)

- EBS - Elastic Block Store - a storage volume for an EC2 instance that are persisted and can live beyond the life of the EC2 instance they are attached to - network attached storage
	- IOPS - Input/Output Operations per Second
	- 1 IOPS = 256kb of data, eg. 0.5mb = 2 IOPS
	- the amount of data that can be written/retrieved from EBS per second
	- every EC2 must have a ROOT volume, which may or may not be EBS
	- by default EBS root vaules are deleted when the instance is terminated (this can be changed)
	- when creating an instance, or afterwards, we can add any additional EBS volumes
	- any additional volume may be attached or detached from an instance at any time and is NOT deleted (by default) when the instance is terminated
	- snapshots are images of EBS volumes that can be stored as backups, to restore a snapshot we create a new EBS and use a snapshot as a template
		- snapshots are incremental in nature (only changes are stored)
		- charged by space taken in S3
	- volume types:
		- general purpose SSD - dev/test env, 3 IOPS per gb, eg. 8 gb = 24 IOPS, burstable - for a short period of time can burst up
		- provisioned IOPS SSD - used for mission critical apps, performance at provisioned level, up to 20000 IPOS
		- magnetic - low cost, low performance

- Instance Store
	- ephemeral data - the data exists during the life of the instance

- Placement Group 
	- a cluster of instances within the same AZ
	- used for apps that require an extremely low latency network
	- AWS attempts to place all the insstances as close as physically possible in the data center to reduce latency
	- capacity error can happen when adding new instance - to resolve it we can stop instances and starting them again

- EFS - Elastic File System - Elastic and Sharable Storage
	- a storage option for EC2 that allows for a scalable storage option
	- storage capacity is elastic
	- is fully managed - no maintenance is required
	- supports NFS protocols when mounting (4.0, 4.1)
	- can be accessed by one or more EC2 instances at the same time - shared file access across EC2 instances
	- can be mounted to on-premise servers when connected to VPC via AWS Direct Connect
	- you pay for the mount of storage you are using
	- POSIX permissions
	- usage: big data and analytics, media processing workflows, web serving and content management
	- requires the default security group (???)

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
	- Elastic IP addresses (EIPs) are designed to be attached/detached and moved from one EC2 instance to another. They are a great solution for keeping a static IP address and moving it to a new instance if the current instance fails. This will reduce or eliminate any downtime uses may experience.

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

	- select an instance, actions => create an image


- viewing user data and instance meta data 
	- curl http://169.254.169.254/latest/user-data
	- curl http://169.254.169.254/latest/meta-data


### Bastion Host
- an EC2 instance that lives in a public subnet and is used as a gateway for traffic that is destined for instances that live in private subnets
- is considered the critical strong point of the network - all the traffic must pass through it
- should have increaded and extremely tight security (usually with extra 3rd party security and monitoring software installed)
- ssh-add -K key.pem
- ssh -A ec2-user@BASTION_IP
- ssh ec2-user@HOST_IP   			- from Bastion host


### NAT Gateway
- designed to provide EC2 instances that live in a private subnet with a route to the Internet (updates, etc.)
- will prevent any host located outside of the VPC from initiating a connection with instances that are associated with it
- will allow incoming traffic through if a request for it was orginated from an instance in a private network
- needed because instance launched into private subnets can't communicate with the open Internet
- must be created in the public network
- be part of the private subnet's route table
- NAT instance has the same purpose as NAT Gateway, the later is AWS service, the first is EC2 instance configured to do the same job (it is a legacy feature in AWS)



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


## CloudWatch
- a service that allows to monitor various elements of the AWS account
- eg. can be used for:
	- EC2 - CPU utilization, status check, disk read/writes
	- S3 - number of objects, bucket size
	- billing
- we can set thresholds to trigger alarms based on the metrics
- CloudWatch alarms can trigger SNS topics
- three states of an alarm: ALARM, INSUFFICENT DATA, OK


## CloudTrail
- an API logging service that logs all API calls made to AWS (CLI, SDK, console)


## SNS - Simple Notification Service
- a service that allows to automate the sending of email or text message notifications based on events that happen in the AWS account
- eg. for billing and EC2 crash notifications
- TOPICS - labels for groups of different endpoints that we send messeges to
- SUBSCRIPTIONS - the endpoints that messages are sent to (eg. email or phone number of an admin)
- PUBLISHERS - the human/alarm/event that gives SNS the message that needs to be send
- eg. create a topic MyAppSNSBilling, add subscriptions (protocols: email, AWS SQS, AWS Lambda, SMS, ...)


## ELB - Elastic Load Balancer
- evenly distributes traffic between EC2 instances
- not available in Free Tier
- configuration under EC2
- allows to load balance traffic to multiple EC2 instances located across multiple availability zones
- ELB should be paired with auto scaling to enhance HA and FT, and allow for automated scalability and elasticity
- ELB has it's own DNS record set that allows for direct access from the open internet access
- ELB will automatically stop serving traffic to an instance that becomes unhealthy

### Classic Load Balancer
- simple balancing of traffic to multiple EC2 instances
- no routing rules - all instances are evenly routed
- best used when all instances contain the same data

### Application Load Balancer
- designed for complex balancing of traffic to multiple EC2 instances using content-based rules
- configured based: 
	- host-based rules - route traffic based on the host field of the HTTP header
	- path-based rules - route traffic based on the URL path of the HTTP header
- allows to structure the application to scale based on traffic to specific target groups
- this type of LB supports: ECS Containers, HTTPS, HTTP/2, WebSockets, Access Logs, Sticky Sessions, AWS WAF (Web Application Firewall)


## Autoscaling
- automates the process of adding or removing EC2 instances based on traffic demand for the application
- it is done based on chosen Cloudwatch metrics
- Launch Configuration - the EC2 template used when the auto scaling group needs to provision an additional instance (i.e. AMI, typem user data, storagem security groups)



## Route 53
- a place where we configure and manage web domains for websites or applications we host on AWS
- allows to register a new domain, eg. example.com
- DNS service
- health checking - Route 53 sends automated requests over the Internet to the app to verify that it's reachable, available, and functional
- create Record Sets - eg. www.example.com - DNS configuration
- Route 53 automatically sends DNS record information to DNS servers AND it is also where it is decided where traffic request for that domain/IP address are routed



## Lambda
- lambda is serverless computing - it is the next generation of cloud computing that will replace EC2 instances (for the most part)
- a compute service that lets run code without provisioning or managing servers
- it executes the code only when needed and scales automatically
- New function, Select blueprint (search for hello-world)
- paid for the compute time it consumes (to the milliseconds)
- lambda can be triggered based on wg an event comming from SNS, updates to DynamoDB table, changes to S3 bucket, custom events generated by applications or devices
- by default it is HA, fault-tolerant, scalable, elastic, cost efficient
- supported languages: node.js, Java, C#, Python



## IAM - Identity & Access Management

- a place where we can manage AWS users and their access to accounts and services
- we can manage: users, groups, access polices, roles, API keys
- is global to all AWS regions
- a user created when we create an AWS account is a ROOT user with FULL administrative rights and access to every part of the account
- by default any NEW user has NO access to any AWS service (except the ability to log in), permissions must be given to grant access
- for full admin access create an IAM user and attach the AdministratorAccess policy (instead of using root, which is a bad practice)

- AccountId: 179656703644
- Logging in page [https://179656703644.signin.aws.amazon.com/console]


### IAM Groups

- IAM Groups allow to assign IAM permission polices to more than one user at a time


### IAM Roles

- IAM roles are a secure way to grant permissions to entities that you trust, eg. application code running on an EC2 instance that needs to perform actions on AWS resources (policies cannot be directly attached to AWS services)
- only one rule can be assigned to an instance
- A role is something that another entity can "assume" – where the entity is another AWS resource like an EC2 instance. AWS resources cannot have permission polices directly applied to them, so to access another resource they must "assume" a role and gain permissions assigned to the role.


### IAM Policies

- IAM policy is a document that states one or more *permissions*
- IAM policies can be directly attached to IAM roles, users and groups
- IAM policies can only be applied to other AWS services (indirectly) through IAM roles
- by default, an explicit deny always overrides an explicit allow
- IAM provides pre-build policy templates, eg.
	- admin access
	- power user access (-user/group management)
	- read only access
	- AmazonS3FullAccess


### IAM STS - Security Token Service

- allows to create temporary security credentials that grant trusted users access to AWS resources
- meant for short-term use - minutes to hours
- when requested through an STS API call, credentials are returned with three components:
	- security token
	- an access key id
	- a secret access key


### IAM API keys
- required to make programmatic calls to AWS eg. CLI, SDK, or direct HTTP calls to individual AWS services
- associated with a user (NOT a role)
- in the console we can see Access Key ID, never the secret key ID (visible once only when created), we can deactivate and create a new one if needed
- never create or store API keys on an EC2 instance
- If you are logged in to the AWS console and managing AWS resources that way, you do not need API keys. API keys are needed when working "programmatically" through CLI, PowerShell, Direct HTTP calls, and SDK API access.



## VPC - Virtual Private Cloud
- a private subsection of AWS that we control, and in which we can place AWS resources (such as EC2 instances), and allow/restrict access to them
- when we create an AWS account a "default" VPC is created
- VPC spans multiple AZ's of the same region
- there are some limits, eg. 5 VPCs per region (more available upon request), 5 elastic IP addresses


### Internet Gateway - IGW - provides a route to the outside world of the VPC
- redundant and highly available - done by AWS for us
- provides NAT translation for instances that have a public IP address assigned
- only one per VPC
- cannot be dettached while there are active resources


### Route Table - contains a set of rules (routes), that are used to determine where network trafic is directed


### NACL - Network Access Control Lists
- an optional layer of security for a VPC that acts as a firewall for controlling trafic IN and OUT of one or more subnets
- Inbound Rules vs Outbound Rules
- by default all traffic is allowed in both directions
- rules are evaluated from lowest to highest by the rule number
- DENY all at the bottom
- a subnet can be associated only with one NACL at a time
- ACL are stateless meaning if we allow inbound traffic we should make sure outbound traffic is allowed as well


### Security Groups
- similar to NACL but defined for the instance level
- only allow rules are supported
- are stateful, so return traffic requests are allowed regardless of rules
- all rules are evaluated before deciding to allow traffic
- best practice is to allow only traffic that is required


### Subnets - a subsection of a network
- a VPC can contain one or more subnets in each Availability Zone
- a subnet cannot span multiple zones
- a public/private subnet - a public subnet has a route to the Internet, a private doesn't (a subnet is connected to a route table without IGW)
- a subnet must be associated with a route table


### As traffic comes into the VPC from the open Internet, it first must be passed through an IGW. A route table will then direct traffic to the subnet that the instance is located in. However, before the traffic can enter the subnet, it must pass through the NACL protecting the subnet. Once inside the subnet, the date must pass through another security layer (the Security Group) before finally reaching the EC2 instance.





