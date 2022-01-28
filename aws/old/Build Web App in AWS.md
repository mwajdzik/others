
[https://www.youtube.com/watch?time_continue=3&v=ZGCHelJ5GWI]

** CloudFormation
	- allows to create a whole system (a stack) based on a input JSON file (network topology, instances, ...)


** Cloud 9
	- [https://aws.amazon.com/cloud9/]
	- a cloud-based IDE that lets you write, run, and debug your code with just a browser


** Cloud Front
	- CDN
	- we create a distribution
	- put before S3 to serve the static content
	- we can also use CF to serve the dynamic content - thanks to the backbone network the response will be much faster then in case of using the Internet - the destribution can be configured to have different sources (path matching) witho or without caching
	- on our page we use URLs pointing to our distribution 


** RDS - Rational Database Service
	- supports multi AZ


** ElastiCache
	- cache for RDS - Redis or MemCache


** Route 53
	- allows to create our own domain


** Certificate Manager
	- allows to create an SSL certificate for free


** AWS good practices
	- static content should be served by S3
	- use CDN (Cloud Front), use pingdom.com to simulate
	- servers and DBs in a private network, a load balancer in a public network
	- use AutoScaling groups for addding/removing EC2


** Notes
	- we can create AMI based on running instance
	- Load Ballancer passes only TCP traffic, not UDP (DDoS attacks)
