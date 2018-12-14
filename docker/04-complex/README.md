----------------------------------------------------------

cd client 
docker build -f Dockerfile.dev .
docker run ID

cd server 
docker build -f Dockerfile.dev .
docker run ID

cd worker 
docker build -f Dockerfile.dev .
docker run ID

docker ps

----------------------------------------------------------

docker-compose up --build
http://localhost:3050/

----------------------------------------------------------

Travis + AWS Elastic Beanstalk deployment

A Dockerrun.aws.json file is an Elastic Beanstalk–specific JSON file that describes how to deploy a set of Docker containers as an Elastic Beanstalk application. You can use a Dockerrun.aws.json file for a multicontainer Docker environment.

----------------------------------------------------------

AWS:

1. Elastic Beanstalk
    - create a new application
    - create a new environment (web server)
    - platform - multi-container docker, sample application

2. Inspect network settings
    - VPC
    - Security Groups

3. Set up ElastiCache (Redis)
    - Redis, create
    - set name (amw061-docker-redis), node type (t2-micro), number of replicas 0
    - create a new subnet group - name: redis-group, select all subnets

4. Set up Postgres (RDS)
    - create database - Postgres
    - enter DB instance ID, username and password
    - specify database name, eg. fibvalues

5. Security group
    - create security group, name eg. amw061-complex-docker
    - edit inbound rule, port range 5432-6379, source - sg-00cf3dd80df70b9eb group id of our security group
    - apply to Redis - select, modify, VPC security groups
    - apply to Postgres - select, modify, security groups
    - apply to Elastic Beanstalk - configuration, modify instances

6. Enter params
    - Elastic Beanstalk - configuration, modify software
    - REDIS_HOST: amw061-docker-redis.hzqsbi.0001.euc1.cache.amazonaws.com
    - REDIS_PORT: 6379
    - PGUSER: amw061
    - PGPASSWORD: phuMHRZBES7nTZp
    - PGHOST: amw061-complex-docker.cn402bkyioq8.eu-central-1.rds.amazonaws.com
    - PGPORT: 5432
    - PGDATABASE: fibvalues

7. IAM 
    - add user: amw061-complex-app-deployer (programmatic access)
    - attach existing policy
    - select all with Elastic Beanstalk
    - note: access key ID, secret access key

8. Travis
    - set AWS_ACCESS_KEY and AWS_SECRET_KEY

----------------------------------------------------------