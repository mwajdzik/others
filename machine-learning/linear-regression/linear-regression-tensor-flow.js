const tf = require('@tensorflow/tfjs');

class LinearRegressionTensorFlow {

    constructor(features, labels, options) {
        this.mseHistory = [];
        this.features = this.processFeatures(features);
        this.labels = tf.tensor(labels);

        // create a tensor (matrix) for weights (m, b)
        this.weights = tf.zeros([
            LinearRegressionTensorFlow.numberOfCols(this.features),
            LinearRegressionTensorFlow.numberOfCols(this.labels)
        ]);

        this.options = Object.assign({
            learningRate: 0.1,
            iterations: 1000,
            batchSize: 10
        }, options);

        this.currentLearningRate = this.options.learningRate;
    }

    train() {
        for (let i = 0; i < this.options.iterations; i++) {
            this.weights = tf.tidy(() => {
                return this.gradientDescent(this.features, this.labels);
            });

            this.recordMeanSquaredError();
            this.updateLearningRate();
        }
    }

    trainUsingBatches() {
        const {batchSize} = this.options;
        const numberOfRows = LinearRegressionTensorFlow.numberOfRows(this.features);
        const batchQuantity = Math.floor(numberOfRows / batchSize);

        for (let i = 0; i < this.options.iterations; i++) {
            for (let j = 0; j < batchQuantity; j++) {
                const startIndex = j * batchSize;

                this.weights = tf.tidy(() => {
                    const featureSlice = this.features.slice([startIndex, 0], [batchSize, -1]);
                    const labelSlice = this.labels.slice([startIndex, 0], [batchSize, -1]);
                    return this.gradientDescent(featureSlice, labelSlice);
                });
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
            .div(LinearRegressionTensorFlow.numberOfRows(features))
            .mul(2);

        return this.weights
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
            const {mean, variance} = tf.moments(features, 0);

            // used to overcome the problem with columns with only 0 and a variance of 0
            const filler = variance.cast('bool').logicalNot().cast('float32');

            this.mean = mean;
            this.variance = variance.add(filler);
        }

        features = features.sub(this.mean).div(this.variance.pow(0.5));

        const ones = tf.ones([LinearRegressionTensorFlow.numberOfRows(features), 1]);       // generate a matrix with one column of ones
        features = ones.concat(features, 1);                // concat ones with features

        return features;
    }

    recordMeanSquaredError() {
        const mse = tf.tidy(() => {
            return this.features.matMul(this.weights)
                .sub(this.labels)
                .pow(2)
                .sum()
                .div(LinearRegressionTensorFlow.numberOfRows(this.features))
                .get();
        });

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

    static numberOfRows(tensor) {
        return tensor.shape[0];
    }

    static numberOfCols(tensor) {
        return tensor.shape[1];
    }
}

module.exports = LinearRegressionTensorFlow;
