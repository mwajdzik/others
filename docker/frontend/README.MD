http://localhost:3000/

docker-compose up --build

docker-compose up
docker-compose down 

docker run d528f7ba3bac
docker run -it d528f7ba3bac npm run test

Live run tests with docker-compose (solution 1):

docker-compose up .
docker ps 
docker exec -it IDIDIDID npm run test

Second solution is to add a new service with command: ["npm", "run", "test"]


----

docker build .

docker run -p 8080:80 672f4fc14812

