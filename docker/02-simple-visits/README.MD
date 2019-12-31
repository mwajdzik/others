--------------------------

docker build -t dornick/simple-visits .

docker run -d redis
docker run -d dornick/simple-visits

--------------------------

### run
docker-compose up

### build and run
docker-compose up --build

### examine
```
docker-compose ps
docker ps
docker exec -it e5abe4a5fee0 sh
```

### close
docker-compose down

--------------------------
