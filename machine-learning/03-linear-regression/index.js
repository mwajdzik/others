require('@tensorflow/tfjs-node');
const tf = require('@tensorflow/tfjs');

const csv = require('./load-csv');
const LinearRegression = require('./LinearRegression');

let { features, labels, testFeatures, testLabels } = csv('./cars.csv', {
    shuffle: true,
    splitTest: 50,
    dataColumns: ['horsepower'],
    labelColumns: ['mpg'],          // miles per galon
});

const lr = new LinearRegression(features, labels, {
    learningRate: 0.00001,
    iterations: 10000
});

lr.train();

console.log('m=', lr.m);
console.log('b=', lr.b);