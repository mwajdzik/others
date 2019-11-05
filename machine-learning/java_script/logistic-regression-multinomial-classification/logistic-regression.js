const tf = require('@tensorflow/tfjs');

class LogisticRegressionTensorFlow {

    constructor(features, labels, options) {
        this.costHistory = [];
        this.features = this.processFeatures(features);
        this.labels = tf.tensor(labels);
        this.weights = tf.zeros([
            LogisticRegressionTensorFlow.numberOfCols(this.features),
            LogisticRegressionTensorFlow.numberOfCols(this.labels)
        ]);

        this.options = Object.assign({
            learningRate: 0.1,
            iterations: 1000,
            batchSize: 10,
            decisionBoundary: 0.5
        }, options);

        this.currentLearningRate = this.options.learningRate;
    }

    train() {
        for (let i = 0; i < this.options.iterations; i++) {
            this.weights = tf.tidy(() => {
                return this.gradientDescent(this.features, this.labels);
            });

            this.recordCrossEntropy();
            this.updateLearningRate();
        }
    }

    trainUsingBatches() {
        const {batchSize} = this.options;
        const numberOfRows = LogisticRegressionTensorFlow.numberOfRows(this.features);
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

            this.recordCrossEntropy();
            this.updateLearningRate();
        }
    }

    gradientDescent(features, labels) {
        const currentGuesses = features.matMul(this.weights).softmax();
        const differences = currentGuesses.sub(labels);

        const slopes = features
            .transpose()
            .matMul(differences)
            .div(LogisticRegressionTensorFlow.numberOfRows(features))
            .mul(2);

        return this.weights
            .sub(slopes.mul(this.currentLearningRate));
    }

    predict(observations) {
        return this.processFeatures(observations)
            .matMul(this.weights)
            .softmax()
            // uncomment to see probability values
            // .greater(this.options.decisionBoundary)
            // .cast('float32')
            .argMax(1);
    }

    test(testFeatures, testLabels) {
        const allRowsCount = testFeatures.length;

        const incorrect = this.predict(testFeatures)
            .notEqual(tf.tensor(testLabels).argMax(1))
            .sum()
            .get();

        return (allRowsCount - incorrect) / allRowsCount;
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

        const ones = tf.ones([LogisticRegressionTensorFlow.numberOfRows(features), 1]);       // generate a matrix with one column of ones
        features = ones.concat(features, 1);                // concat ones with features

        return features;
    }

    recordCrossEntropy() {
        const cost = tf.tidy(() => {
            const guesses = this.features.matMul(this.weights).softmax();

            const term1 = this.labels.transpose()
                .matMul(guesses.add(1e-7).log());

            const term2 = this.labels.mul(-1).add(1).transpose()
                .matMul(guesses.mul(-1).add(1).add(1e-7).log());    // to avoid log(0)

            return term1.add(term2)
                .div(LogisticRegressionTensorFlow.numberOfRows(this.features))
                .mul(-1)
                .get(0, 0);
        });

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

    static numberOfRows(tensor) {
        return tensor.shape[0];
    }

    static numberOfCols(tensor) {
        return tensor.shape[1];
    }
}

module.exports = LogisticRegressionTensorFlow;
