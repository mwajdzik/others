const tf = require('@tensorflow/tfjs');
const _ = require('lodash');

class LogisticRegressionTensorFlow {

    constructor(features, labels, options) {
        this.features = this.processFeatures(features);
        this.labels = tf.tensor(labels);
        this.weights = tf.zeros([this.features.shape[1], 1]);
        this.costHistory = [];

        this.options = Object.assign({
            learningRate: 0.1,
            iterations: 1000,
            batchSize: 10,
            decisionBoundry: 0.5
        }, options);

        this.currentLearningRate = this.options.learningRate;
    }

    train() {
        for (let i = 0; i < this.options.iterations; i++) {
            this.gradientDescent(this.features, this.labels);
            this.recordCrossEntropy();
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

            this.recordCrossEntropy();
            this.updateLearningRate();
        }
    }

    gradientDescent(features, labels) {
        const currentGuesses = features.matMul(this.weights).sigmoid();
        const differences = currentGuesses.sub(labels);

        const slopes = features
            .transpose()
            .matMul(differences)
            .div(this.numberOfRows(features))
            .mul(2);

        this.weights = this.weights
            .sub(slopes.mul(this.currentLearningRate));
    }

    predict(observations) {
        return this.processFeatures(observations)
            .matMul(this.weights)
            .sigmoid()
            .greater(this.options.decisionBoundry)
            .cast('float32');
    }

    test(testFeatures, testLabels) {
        const allRowsCount = testFeatures.length;

        const incorrect = this.predict(testFeatures)
            .sub(tf.tensor(testLabels))
            .abs()
            .sum()
            .get();
        
        return (allRowsCount - incorrect) / allRowsCount;
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

    recordCrossEntropy() {
        const guesses = this.features.matMul(this.weights).sigmoid();

        const term1 = this.labels.transpose()
            .matMul(guesses.log());

        const term2 = this.labels.mul(-1).add(1).transpose()
            .matMul(guesses.mul(-1).add(1).log());

        const cost = term1.add(term2)
            .div(this.numberOfRows(this.features))
            .mul(-1)
            .get(0, 0);

        this.costHistory.push(cost);
    }

    updateLearningRate() {
        if (this.costHistory.length < 2) {
            return;
        }

        const len = this.costHistory.length;
        const lastValue = this.costHistory[len - 1];
        const secondLastValue = this.costHistory[len - 2];

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

module.exports = LogisticRegressionTensorFlow;
