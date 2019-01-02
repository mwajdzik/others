const _ = require('lodash');

function runAnalysis(outputs) {
    const testSetSize = Math.floor(0.2 * outputs.length);
    const [testSet, trainingSet] = splitDataset(outputs, testSetSize);
    console.log('TestSet: [', testSet.toString(), ']');

    _.range(1, 5).forEach(k => {
        const accuracy = _.chain(testSet)
            .filter(testPoint => knn(trainingSet, _.initial(testPoint), k) === _.last(testPoint))
            .size()
            .divide(testSetSize)
            .value();

        console.log('For k of', k, 'accuracy is', accuracy);
    });
}

function normalize(data, featureCount) {
    const clonedData = _.cloneDeep(data);

    for (let i = 0; i < featureCount; i++) {
        const column = clonedData.map(row => row[i]);
        const min = _.min(column);
        const max = _.max(column);

        for (let j = 0; j < clonedData.length; j++) {
            clonedData[j][i] = (clonedData[j][i] - min) / (max - min);
        }
    }

    return clonedData;
}

function splitDataset(data, testCount) {
    const shuffled = _.shuffle(data);
    const testSet = _.slice(shuffled, 0, testCount);
    const trainingSet = _.slice(shuffled, testCount);

    return [testSet, trainingSet];
}

function knn(data, point, k) {
    return _.chain(data)
        .map(row => [distance(_.initial(row), point), _.last(row)])
        .sortBy(([dist, bucket]) => dist)
        .slice(0, k)    // take 'k' that are closest
        .countBy(([dist, bucket]) => bucket)
        .toPairs()
        .sortBy(([bucket, count]) => count)
        .last()
        .first()
        .parseInt()
        .value();
}

function distance(pointA, pointB) {
    return _.chain(pointA)
        .zip(pointB)
        .map(([a, b]) => (a - b) ** 2)
        .sum()
        .value() ** 0.5;
}

const outputs = [
    [300, 0.50, 16, 5],
    [310, 0.52, 16, 5],
    [290, 0.53, 16, 4],
    [340, 0.52, 16, 6],
    [230, 0.51, 16, 3]
];

console.log('-----------------------');
console.log(distance([1, 1], [4, 5]));
console.log(splitDataset(outputs, 1));
console.log(knn(outputs, [305, 0.51, 16], 2));

runAnalysis(normalize(outputs));
