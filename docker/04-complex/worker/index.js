const keys = require("./keys");
const redis = require("redis");

// ----------------------------------------------------------------------------------------------------
// Redis Client

const redisClient = redis.createClient({
    host: keys.redisHost,
    port: keys.redisPort,
    retry_strategy: () => 1000
});

// ----------------------------------------------------------------------------------------------------

function fib(index) {
    if (index < 2) {
        return 1;
    } else {
        return fib(index - 1) + fib(index - 2);
    }
}

// ----------------------------------------------------------------------------------------------------

const subscription = redisClient.duplicate();

subscription.on('message', (channel, index) => {
    console.info(`Got ${index} on the channel ${channel}`)
    const value = fib(parseInt(index));
    redisClient.hset('values', index, value);
    console.info(`Calculated fib value for ${index} to be ${value}`);
});

subscription.subscribe('insert');

console.info("Worker started!");
