const tf = require('@tensorflow/tfjs');
const _ = require('lodash');

class LinearRegression {

    constructor(features, labels, options) {
        this.features = features;
        this.labels = labels;
        this.m = 0;
        this.b = 0;

        this.featuresTensor = this.processFeatures(features);
        this.labelsTensor = tf.tensor(labels);
        this.weights = tf.zeros([2, 1]);    // create a tensor (matrix) for weights (m, b)

        this.options = Object.assign({
            learningRate: 0.1,
            iterations: 1000,
        }, options);
    }

    train() {
        for (let i = 0; i < this.options.iterations; i++) {
            this.gradientDescent();
        }
    }

    trainWithTensorFlow() {
        for (let i = 0; i < this.options.iterations; i++) {
            this.gradientDescentWithTensorFlow();
        }
    }

    gradientDescent() {
        const currentGuesses = this.features.map(row => {
            return this.m * row[0] + this.b;
        });

        const bSlope = _.sum(currentGuesses.map((guess, i) => {
            return guess - this.labels[i][0];
        })) * 2 / currentGuesses.length;

        const mSlope = _.sum(currentGuesses.map((guess, i) => {
            return this.features[i][0] * (guess - this.labels[i][0]);
        })) * 2 / currentGuesses.length;

        this.m -= mSlope * this.options.learningRate;
        this.b -= bSlope * this.options.learningRate
    }

    gradientDescentWithTensorFlow() {
        // features * weights
        const currentGuesses = this.featuresTensor.matMul(this.weights);

        // (features * weights) - labels
        const differences = currentGuesses.sub(this.labels);

        // features * [(features * weights) - labels] / n
        const slopes = this.featuresTensor
            .transpose()
            .matMul(differences)
            .div(this.featuresTensor.shape[0])
            .mul(2);

        this.weights = this.weights
            .sub(slopes.mul(this.options.learningRate))
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
        const ones = tf.ones([features.shape[0], 1]);       // generate a matrix with one column of ones
        features = ones.concat(features, 1);                // concat ones with features

        return features;
    }
}

module.exports = LinearRegression;
