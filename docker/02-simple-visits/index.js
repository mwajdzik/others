const express = require("express");
const redis = require("redis");
const process = require("process");

const app = express();

// "redis-service" - from docker-compose.yml
const client = redis.createClient({
    host: 'redis-service',
    port: 6379
});

client.set('visits', 0);

app.get('/', (req, res) => {
    client.get('visits', (err, visits) => {
        visits = parseInt(visits) + 1;

        res.send('Number of visits is ' + visits);
        client.set('visits', visits);

        if (visits % 5 === 0) {
            console.log("Restarting...");
            process.exit(0);
        }
    });
});

app.listen(8081, () => {
    console.log("Listening...");
});
