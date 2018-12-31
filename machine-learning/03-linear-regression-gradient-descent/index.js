require('@tensorflow/tfjs-node');

const loadCsv = require('./load-csv');
const LinearRegressionManualCalc = require('./LinearRegressionManualCalc');
const LinearRegressionTensorFlow = require('./LinearRegressionTensorFlow');

let { features, labels, testFeatures, testLabels } = loadCsv('./cars.csv', {
    shuffle: true,
    splitTest: 50,
    dataColumns: ['horsepower', 'weight', 'displacement'],
    labelColumns: ['mpg'],          // miles per galon
});

// --------------------------------------------------------------------------------------------------------

console.log('--------------------------------------------------------------------------------------------------------');

const lr = new LinearRegressionManualCalc(features, labels, { learningRate: 0.00001, iterations: 100 });
lr.train();

console.log('LinearRegressionManualCalc:\n');
console.log('m=', lr.m, 'b=', lr.b);
console.log('Prediction for', 130, 'is', lr.m * 130 + lr.b);
console.log('');

// --------------------------------------------------------------------------------------------------------

function printResults(lrtf) {
    console.log('--------------------------------------------------------------------------------------------------------');

    console.log('LinearRegressionTensorFlow:\n');
    console.log('options =', lrtf.options, '\n');
    console.log('weights (b, m1, m2, ...) =', lrtf.weights.toString());
    console.log('mean =', lrtf.mean.toString());
    console.log('variance =', lrtf.variance.toString());
    console.log('standardDeviation =', lrtf.variance.pow(0.5).toString());
    console.log('Coefficient of Determination:', lrtf.test(testFeatures, testLabels));

    const testData = [
        [130, 1.752, 307],
        [120, 2, 380],
        [135, 2.1, 420],
    ]; 

    const predictions = lrtf.predict(testData);
    console.log('Prediction for', testData, 'is', predictions.toString());
    console.log('');
}

console.time('Gradient Descent');
const lrtfGradientDescent = new LinearRegressionTensorFlow(features, labels, { learningRate: 0.1, iterations: 100 });
lrtfGradientDescent.train();
printResults(lrtfGradientDescent);
console.timeEnd('Gradient Descent');

console.time('Batch Gradient Descent');
const lrtfBatchGradientDescent = new LinearRegressionTensorFlow(features, labels, { learningRate: 0.1, iterations: 5, batchSize: 10 });
lrtfBatchGradientDescent.trainUsingBatches();
printResults(lrtfBatchGradientDescent);
console.timeEnd('Batch Gradient Descent');

console.time('Stochastic Gradient Descent');
const lrtfStochasticGradientDescent = new LinearRegressionTensorFlow(features, labels, { learningRate: 0.1, iterations: 5, batchSize: 1 });
lrtfStochasticGradientDescent.trainUsingBatches();
printResults(lrtfStochasticGradientDescent);
console.timeEnd('Stochastic Gradient Descent');
