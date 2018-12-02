
cd client 
docker build -f Dockerfile.dev .
docker run ID

cd server 
docker build -f Dockerfile.dev .

cd worker 
docker build -f Dockerfile.dev .

docker ps

---

docker-compose up
