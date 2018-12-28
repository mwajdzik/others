const tf = require('@tensorflow/tfjs');
const _ = require('lodash');

class LinearRegressionTensorFlow {

    constructor(features, labels, options) {
        this.features = this.processFeatures(features);
        this.labels = tf.tensor(labels);
        this.weights = tf.zeros([this.features.shape[1], 1]);    // create a tensor (matrix) for weights (m, b)

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

    gradientDescent() {
        // features * weights
        const currentGuesses = this.features.matMul(this.weights);

        // (features * weights) - labels
        const differences = currentGuesses.sub(this.labels);

        // features * [(features * weights) - labels] / n
        const slopes = this.features
            .transpose()
            .matMul(differences)
            .div(this.features.shape[0])
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

        if (!(this.mean && this.variance)) {
            const {mean, variance} = tf.moments(features, 0);
            this.mean = mean;
            this.variance = variance;
        }
        
        features = this.standardize(features);

        const ones = tf.ones([features.shape[0], 1]);       // generate a matrix with one column of ones
        features = ones.concat(features, 1);                // concat ones with features

        return features;
    }

    standardize(features) {
        return features.sub(this.mean).div(this.variance.pow(0.5));
    }
}

module.exports = LinearRegressionTensorFlow;
