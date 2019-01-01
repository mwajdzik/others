require('@tensorflow/tfjs-node');

const _ = require('lodash');
const mnist = require('mnist-data');
const LogisticRegression = require('./logistic-regression-multinomial-classification/logistic-regression');

function loadData() {
    const mnistData = mnist.training(0, 60000);
    const features = mnistData.images.values.map(image => _.flatMap(image));
    const labels = mnistData.labels.values.map(label => {
        const row = new Array(10).fill(0);
        row[label] = 1;
        return row;
    });

    return {features, labels};
}

const {features, labels} = loadData();

const lr = new LogisticRegression(features, labels, {
    learningRate: 0.1,
    iterations: 20,
    batchSize: 100
});

lr.trainUsingBatches();

const mnistTestData = mnist.training(0, 1000);
const testFeatures = mnistTestData.images.values.map(image => _.flatMap(image));
const testLabels = mnistTestData.labels.values.map(label => {
    const row = new Array(10).fill(0);
    row[label] = 1;
    return row;
});

const accuracy = lr.test(testFeatures, testLabels);
console.log('Accuracy is:', accuracy);
console.log('Cost history:', lr.costHistory);

