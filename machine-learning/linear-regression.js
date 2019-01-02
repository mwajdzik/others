require('@tensorflow/tfjs-node');

const loadCsv = require('./data/load-csv');
const LinearRegressionManualCalc = require('./linear-regression/linear-regression-manual-calc');
const LinearRegressionTensorFlow = require('./linear-regression/linear-regression-tensor-flow.js');

let {features, labels, testFeatures, testLabels} = loadCsv('./data/cars.csv', {
    shuffle: true,
    splitTest: 50,
    dataColumns: ['horsepower', 'weight', 'displacement'],
    labelColumns: ['mpg']
});

// --------------------------------------------------------------------------------------------------------

console.log('--------------------------------------------------------------------------------------------------------');

const lr = new LinearRegressionManualCalc(features, labels, {learningRate: 0.00001, iterations: 100});
lr.train();

console.log('LinearRegression - manual calculations:\n');
console.log('m=', lr.m, 'b=', lr.b);
console.log('prediction for', 130, 'is', lr.m * 130 + lr.b);
console.log('');

// --------------------------------------------------------------------------------------------------------

function printResults(lrtf) {
    console.log('--------------------------------------------------------------------------------------------------------');

    console.log('LinearRegression - with TensorFlow:\n');
    console.log('options =', lrtf.options, '\n');
    console.log('weights (b, m1, m2, ...) =', lrtf.weights.toString());
    console.log('mean =', lrtf.mean.toString());
    console.log('variance =', lrtf.variance.toString());
    console.log('standardDeviation =', lrtf.variance.pow(0.5).toString());
    console.log('coefficient of determination:', lrtf.test(testFeatures, testLabels));

    const testData = [
        [130, 1.752, 307],
        [120, 2, 380],
        [135, 2.1, 420],
    ];

    const predictions = lrtf.predict(testData);
    console.log('prediction for', testData, 'is', predictions.toString());
    console.log('');
}

console.time('Gradient Descent');
const gradientDescent = new LinearRegressionTensorFlow(features, labels, {learningRate: 0.1, iterations: 100});
gradientDescent.train();
printResults(gradientDescent);
console.timeEnd('Gradient Descent');

console.time('Batch Gradient Descent');
const batchGradientDescent = new LinearRegressionTensorFlow(features, labels, {
    learningRate: 0.1,
    iterations: 5,
    batchSize: 10
});
batchGradientDescent.trainUsingBatches();
printResults(batchGradientDescent);
console.timeEnd('Batch Gradient Descent');

console.time('Stochastic Gradient Descent');
const stochasticGradientDescent = new LinearRegressionTensorFlow(features, labels, {
    learningRate: 0.1,
    iterations: 5,
    batchSize: 1
});
stochasticGradientDescent.trainUsingBatches();
printResults(stochasticGradientDescent);
console.timeEnd('Stochastic Gradient Descent');
