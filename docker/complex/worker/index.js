const keys = require("./keys");
const redis = require("redis");

const redisClient = redis.createClient({
    host: keys.redisHost,
    port: keys.redisPort,
    retry_strategy: () => 1000
});

const subscription = redisClient.duplicate();

function fib(index) {
    if (index < 2) {
        return 1;
    } else {
        return fib(index - 1) + fib(index - 2);
    }
}

subscription.on('message', (channel, message) => {
    const value = fib(parseInt(message));
    redisClient.hset('values', message, value);
    console.info('Calculated fib value for ' + message + ' to be ' + value);
});

subscription.subscribe('insert');