
cd client 
docker build -f Dockerfile.dev .
docker run ID

cd server 
docker build -f Dockerfile.dev .

cd worker 
docker build -f Dockerfile.dev .

docker ps


---------------------------------------------------------------------------------------------------------------------------


docker-compose up --build

http://localhost:3050/


---------------------------------------------------------------------------------------------------------------------------

Travis + AWS Elastic Beanstalk deployment

A Dockerrun.aws.json file is an Elastic Beanstalk–specific JSON file that describes how to deploy a set of Docker containers as an Elastic Beanstalk application. You can use a Dockerrun.aws.json file for a multicontainer Docker environment.

---------------------------------------------------------------------------------------------------------------------------