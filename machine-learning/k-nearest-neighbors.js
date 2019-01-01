require('@tensorflow/tfjs-node');               // tfjs-node-gpu

const tf = require('@tensorflow/tfjs');
const loadCsv = require('./data/load-csv');

function knn(features, labels, predictionPoint, k) {
    const {mean, variance} = tf.moments(features, 0);
    const scaledPredictionPoint = predictionPoint.sub(mean).div(variance.pow(0.5));

    return features
        .sub(mean).div(variance.pow(0.5))   // standardisation
        .sub(scaledPredictionPoint)
        .pow(2)
        .sum(1)
        .sqrt()
        .expandDims(1)
        .concat(labels, 1)
        .unstack()          // create an array of tensors
        .sort((a, b) => a.get(0) > b.get(0) ? 1 : -1)
        .slice(0, k)
        .reduce((acc, a) => acc + a.get(1), 0) / k;
}

let { features, labels, testFeatures, testLabels } = loadCsv('./data/kc_house_data.csv', {
    shuffle: true,
    splitTest: 10,
    dataColumns: ['lat', 'long', 'sqft_lot', 'sqft_living'],
    labelColumns: ['price']
});

testFeatures.forEach((testPoint, i) => {
    const result = knn(
        tf.tensor(features), 
        tf.tensor(labels), 
        tf.tensor(testPoint), 
        10
    );
    
    const currentLabel = testLabels[i][0];
    const err = 100 * (currentLabel - result) / currentLabel;
    
    console.log('Guess', result, currentLabel);
    console.log('Error', err);    
});
