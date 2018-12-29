const tf = require('@tensorflow/tfjs');
const _ = require('lodash');

class LinearRegressionTensorFlow {

    constructor(features, labels, options) {
        this.features = this.processFeatures(features);
        this.labels = tf.tensor(labels);
        this.weights = tf.zeros([this.features.shape[1], 1]);    // create a tensor (matrix) for weights (m, b)
        this.mseHistory = [];

        this.options = Object.assign({
            learningRate: 0.1,
            iterations: 1000,
            batchSize: 10
        }, options);

        this.currentLearningRate = this.options.learningRate;
    }

    train() {
        for (let i = 0; i < this.options.iterations; i++) {
            this.gradientDescent(this.features, this.labels);
            this.recordMeanSquaredError();
            this.updateLearningRate();
        }
    }

    trainUsingBatches() {
        const { batchSize } = this.options;
        const numberOfRows = this.numberOfRows(this.features);
        const batchQuantity = Math.floor(numberOfRows / batchSize);

        for (let i = 0; i < this.options.iterations; i++) {
            for (let j = 0; j < batchQuantity; j++) {
                const startIndex = j * batchSize
                const featureSlice = this.features.slice([startIndex, 0], [batchSize, -1]);
                const labelSlice = this.labels.slice([startIndex, 0], [batchSize, -1]);
                this.gradientDescent(featureSlice, labelSlice);
            }

            this.recordMeanSquaredError();
            this.updateLearningRate();
        }
    }

    gradientDescent(features, labels) {
        // features * weights
        const currentGuesses = features.matMul(this.weights);

        // (features * weights) - labels
        const differences = currentGuesses.sub(labels);

        // features * [(features * weights) - labels] / n
        const slopes = features
            .transpose()
            .matMul(differences)
            .div(this.numberOfRows(features))
            .mul(2);

        this.weights = this.weights
            .sub(slopes.mul(this.currentLearningRate));
    }

    predict(observations) {
        return this.processFeatures(observations).matMul(this.weights);
    }

    test(testFeatures, testLabels) {
        testFeatures = this.processFeatures(testFeatures);
        testLabels = tf.tensor(testLabels);

        const predictions = testFeatures.matMul(this.weights);

        const ssTot = testLabels.sub(testLabels.mean())
            .pow(2)
            .sum()
            .get();

        const ssRes = testLabels.sub(predictions)
            .pow(2)
            .sum()
            .get();

        return 1 - ssRes / ssTot;
    }

    processFeatures(features) {
        features = tf.tensor(features);

        if (!(this.mean && this.variance)) {
            const { mean, variance } = tf.moments(features, 0);
            this.mean = mean;
            this.variance = variance;
        }

        features = this.standardize(features);

        const ones = tf.ones([this.numberOfRows(features), 1]);       // generate a matrix with one column of ones
        features = ones.concat(features, 1);                // concat ones with features

        return features;
    }

    standardize(features) {
        return features.sub(this.mean).div(this.variance.pow(0.5));
    }

    recordMeanSquaredError() {
        const mse = this.features.matMul(this.weights)
            .sub(this.labels)
            .pow(2)
            .sum()
            .div(this.numberOfRows(this.features))
            .get();

        this.mseHistory.push(mse);
    }

    updateLearningRate() {
        if (this.mseHistory.length < 2) {
            return;
        }

        const len = this.mseHistory.length;
        const lastValue = this.mseHistory[len - 1];
        const secondLastValue = this.mseHistory[len - 2];

        if (lastValue > secondLastValue) {
            this.currentLearningRate /= 2;
        } else {
            this.currentLearningRate *= 1.05;
        }
    }

    numberOfRows(tensor) {
        return tensor.shape[0];
    }
}

module.exports = LinearRegressionTensorFlow;
