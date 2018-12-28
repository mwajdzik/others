require('@tensorflow/tfjs-node');
const tf = require('@tensorflow/tfjs');

const csv = require('./load-csv');
const LinearRegressionManualCalc = require('./LinearRegressionManualCalc');
const LinearRegressionTensorFlow = require('./LinearRegressionTensorFlow');

let { features, labels, testFeatures, testLabels } = csv('./cars.csv', {
    shuffle: true,
    splitTest: 50,
    dataColumns: ['horsepower', 'weight', 'displacement'],
    labelColumns: ['mpg'],          // miles per galon
});

// --------------------------------------------------------------------------------------------------------

const lr = new LinearRegressionManualCalc(features, labels, { learningRate: 0.00001, iterations: 100 });

lr.train();

console.log('--------------------------------------------------------------------------------------------------------');
console.log('LinearRegressionManualCalc:');
console.log('m=', lr.m, 'b=', lr.b);
console.log('prediction(130)=', lr.m * 130 + lr.b);
console.log('');

// --------------------------------------------------------------------------------------------------------

const lrtf = new LinearRegressionTensorFlow(features, labels, { learningRate: 0.01, iterations: 100 });

lrtf.train();

const m = lrtf.weights.get(1, 0);
const b = lrtf.weights.get(0, 0);
const mean = lrtf.mean.get(0)
const variance = lrtf.variance.get(0);
const standardDeviation = variance ** 0.5

console.log('--------------------------------------------------------------------------------------------------------');
console.log('LinearRegressionTensorFlow:');
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
