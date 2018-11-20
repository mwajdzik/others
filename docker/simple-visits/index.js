const express = require("express");
const redis = require("redis");
const process = require("process");

const app = express();

// redis-service - from docker-compose.yml
const client = redis.createClient({
    host: 'redis-service',
    port: 6379
});

client.set('visits', 1);

app.get('/', (req, res) => {
    client.get('visits', (err, visits) => {
        res.send('Number of visits is ' + visits);
        client.set('visits', parseInt(visits) + 1);

        if (visits % 5 === 0) {
            process.exit(0);
        }
    });
});

app.listen(8081, () => {
    console.log("Listening...");
});
