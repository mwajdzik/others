
```
npm install -g create-react-app
```

```
docker build -f Dockerfile.dev .
docker run -p 3000:3000 ID
docker run -p 3000:3000 -v /app/node_modules -v $(pwd):/app ID
```

```
docker-compose up --build 
http://localhost:3000/
docker-compose down
``` 

### Running tests in DEV env:
```
docker run ID
docker run -it ID npm run test
```

### Live run tests with docker-compose

- solution 1

    ```
    docker ps 
    docker exec -it ID npm run test
    ```

- solution 2

    Add a new Dockerfile service with a command: ["npm", "run", "test"]


### Prod build:

```
docker build .
docker run -p 8080:80 672f4fc14812
```
