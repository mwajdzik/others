const _ = require('lodash');

class LinearRegressionManualCalc {

    constructor(features, labels, options) {
        this.features = features;
        this.labels = labels;
        this.m = 0;
        this.b = 0;

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
}

module.exports = LinearRegressionManualCalc;
