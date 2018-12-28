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

// --------------------------------------------------------------------------------------------------------

const lr = new LinearRegression(features, labels, { learningRate: 0.00001, iterations: 100 });

lr.train();

console.log('m=', lr.m, 'b=', lr.b);
console.log('prediction(130)=', lr.m * 130 + lr.b);
console.log('');

// --------------------------------------------------------------------------------------------------------

const lrtf = new LinearRegression(features, labels, { learningRate: 0.01, iterations: 100 });

lrtf.trainWithTensorFlow();

const m = lrtf.weights.get(1, 0);
const b = lrtf.weights.get(0, 0);
const mean = lrtf.mean.get(0)
const variance = lrtf.variance.get(0);
const standardDeviation = variance ** 0.5

console.log('m=', m, 'b=', b, 'mean=', mean, 'variance=', variance, 'standardDeviation=', standardDeviation);

const x = 130;
const stdX = (x - mean) / standardDeviation

const newM = m / standardDeviation;
const newB = b - (m * mean) / standardDeviation;

console.log('Standardized', x, 'is', stdX);
console.log('Prediction for', x, 'is', m * stdX + b);
console.log('Prediction for', x, 'is', newM * x + newB);

const cd = lrtf.test(testFeatures, testLabels);
console.log('Coefficient of Determination:', cd);
