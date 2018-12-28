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
    iterations: 100
});

lr.train();

console.log('m=', lr.m);
console.log('b=', lr.b);

// ---

const lrtf = new LinearRegression(features, labels, {
    learningRate: 0.00001,
    iterations: 100
});

lrtf.trainWithTensorFlow();

console.log('m=', lrtf.weights.get(1, 0));
console.log('b=', lrtf.weights.get(0, 0));

const cd = lrtf.test(testFeatures, testLabels);
console.log('Coefficient of Determination:', cd);
