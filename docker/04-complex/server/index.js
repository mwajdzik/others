const keys = require('./keys');
const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

// ----------------------------------------------------------------------------------------------------
// Express

const app = express();
app.use(cors());
app.use(bodyParser.json());

// ----------------------------------------------------------------------------------------------------
// Postgress

const { Pool } = require('pg')

const pgClient = new Pool({
    user: keys.pgUser,
    host: keys.pgHost,
    port: keys.pgPort,
    database: keys.pgDatabase,
    password: keys.pgPassword
});

pgClient.on('error', () =>
        console.error('Lost Postgress connection'));

pgClient.query('CREATE TABLE IF NOT EXISTS numbers(number INT);')
    .then(() => console.info('Table "numbers" created!'))
    .catch(err => console.error(`Postgres error: ${err}`));

// ----------------------------------------------------------------------------------------------------
// Redis Client

const redis = require('redis');
const redisClient = redis.createClient({
    host: keys.redisHost,
    port: keys.redisPort,
    retry_strategy: () => 1000
});

const redisPublisher = redisClient.duplicate();

// ----------------------------------------------------------------------------------------------------
// Express route handlers

app.get('/', (req, res) => {
    res.send('Hi');
});

app.get('/values/all', async (req, res) => {
    console.info(`/values/all:     got request to return all seen values`)
    const values = await pgClient.query('SELECT * FROM numbers;');
    console.info(`/values/all:     returning ${JSON.stringify(values.rows)}`)
    res.send(values.rows);
});

app.get('/values/current', async (req, res) => {
    console.info(`/values/current: got request to return all current values`)

    // no support for promises (await) in Redis
    redisClient.hgetall('values', (err, values) => {
        console.info(`/values/current: returning ${JSON.stringify(values)}`)
        res.send(values);
    });
});

app.post('/values', async (req, res) => {
    const index = req.body.index;
    
    if (parseInt(index) > 40) {
        return res.status(422).send('Index too high');
    }

    console.info(`Got request to calculate fib value for ${index}`);
    redisClient.hset('values', index, 'Nothing yet!');
    redisPublisher.publish('insert', index);
    pgClient.query('INSERT INTO numbers(number) VALUES($1);', [index]);
    console.info(`Done with calculations for ${index}`);

    res.send({working: true});
});

app.listen(5000, (err => {
    console.log("Listening...");
}));
