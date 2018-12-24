require('@tensorflow/tfjs-node');
// require('@tensorflow/tfjs-node-gpu');
const tf = require('@tensorflow/tfjs');
const loadCsv = require('./load-csv');

let {features, labels, testFeatures, testLabels} = loadCsv('kc_house_data.csv', {
    shuffle: true,
    splitTest: 10,
    dataColumns: ['lat', 'long'],
    labelColumns: ['price']
});

console.log(features, labels, testFeatures, testLabels);
