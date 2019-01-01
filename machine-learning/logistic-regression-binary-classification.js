require('@tensorflow/tfjs-node');

const loadCsv = require('./data/load-csv');
const LogisticRegression = require('./logistic-regression-binary-classification/logistic-regression');

let { features, labels, testFeatures, testLabels } = loadCsv('./data/cars.csv', {
    shuffle: true,
    splitTest: 50,
    dataColumns: ['horsepower', 'weight', 'displacement'],
    labelColumns: ['passedemissions'],
    converters: {
        passedemissions: (value) => {
            return value === 'TRUE' ? 1 : 0;
        }
    }
});

// --------------------------------------------------------------------------------------------------------

const lr = new LogisticRegression(features, labels, {
    learningRate: 0.1,
    iterations: 20,
    batchSize: 10,
    decisionBoundry: 0.5
});

lr.trainUsingBatches();

const predictions = lr.predict([
    [130, 1.752, 307],
    [88, 1.065, 97],
]);

predictions.print();

console.log('Number of correct guesses:', lr.test(testFeatures, testLabels));

