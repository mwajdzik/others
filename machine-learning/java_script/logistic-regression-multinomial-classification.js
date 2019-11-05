require('@tensorflow/tfjs-node');

const _ = require('lodash');
const loadCsv = require('./data/load-csv');
const LogisticRegression = require('./logistic-regression-multinomial-classification/logistic-regression');

let {features, labels, testFeatures, testLabels} = loadCsv('./data/cars.csv', {
    shuffle: true,
    splitTest: 50,
    dataColumns: ['horsepower', 'weight', 'displacement'],
    labelColumns: ['mpg'],
    converters: {
        mpg: (value) => {
            const mpg = parseFloat(value);

            if (mpg < 15) {
                return [1, 0, 0];           // HIGH fuel consumption
            } else if (mpg < 30) {
                return [0, 1, 0];           // MEDIUM fuel consumption
            } else {
                return [0, 0, 1];           // LOW fuel consumption
            }
        }
    }
});

// --------------------------------------------------------------------------------------------------------

labels = _.flatMap(labels);

const lr = new LogisticRegression(features, labels, {
    learningRate: 0.1,
    iterations: 10,
    batchSize: 10,
    decisionBoundry: 0.5
});

lr.trainUsingBatches();

const predictions = lr.predict([
    [215, 2.16, 440],
    [150, 2.223, 200],
    [69, 0.8065, 72],
]);

predictions.print();

console.log('Number of correct guesses:', lr.test(testFeatures, _.flatMap(testLabels)));

