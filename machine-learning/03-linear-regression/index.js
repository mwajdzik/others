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
console.log('LinearRegressionManualCalc:\n');
console.log('m=', lr.m, 'b=', lr.b);
console.log('Prediction for', 130, 'is', lr.m * 130 + lr.b);
console.log('');

// --------------------------------------------------------------------------------------------------------

const lrtf = new LinearRegressionTensorFlow(features, labels, { learningRate: 0.0075, iterations: 100 });

lrtf.train();

console.log('--------------------------------------------------------------------------------------------------------');
console.log('LinearRegressionTensorFlow:\n');
console.log('weights (b, m1, m2, ...) =', lrtf.weights.toString());
console.log('mean =', lrtf.mean.toString());
console.log('variance =', lrtf.variance.toString());
console.log('standardDeviation =', lrtf.variance.pow(0.5).toString());

const cd = lrtf.test(testFeatures, testLabels);
console.log('Coefficient of Determination:', cd);

// ---

const testData = [130, 1.752, 307];
const stdTest = tf.ones([1]).concat(lrtf.standardize(tf.tensor(testData))).expandDims(0, 1);
console.log('Prediction for', testData, 'is', stdTest.matMul(lrtf.weights).get(0, 0));
console.log('');
