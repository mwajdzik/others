
console.log('-----------------------');

let t1 = tf.tensor([1, 2, 3]);
let t2 = tf.tensor([4, 5, 6]);

t1.add(t2).print()
t1.sub(t2).print();
t1.mul(t2).print();
t1.div(t2).print();

let t3 = tf.tensor([
    [1, 2, 3],
    [4, 5, 6]
]);

console.log(t3.shape);

let t4 = tf.tensor([
    [5],
    [5]
]);

let t5 = tf.tensor([
    [5]
]);

t3.add(t1).print();
t3.add(t4).print();
t3.add(t5).print();

const t12 = tf.tensor([1, 1]);
const t11 = tf.tensor([
    [1, 1, 1],
    [2, 2, 2]
]);

try {
    t11.sub(t12);
} catch (e) {
    console.error(e);
}

console.log(t1.get(0));
console.log(t5.get(0, 0));

// ---

let t21 = tf.tensor([
    [1, 2, 3],
    [4, 5, 6],
    [7, 8, 9],
    [1, 2, 3],
    [4, 5, 6],
    [7, 8, 9]
]);

t21.slice([0, 1], [6, 1]).print();
t21.slice([0, 1], [-1, 1]).print();
t21.slice([2, 2], [3, 1]).print();
t21.slice([2, 2], [-1, 1]).print();

// ---

let t22 = tf.tensor([
    [3, 2, 1],
    [6, 5, 4],
    [9, 8, 7],
    [3, 2, 1],
    [6, 5, 4],
    [9, 8, 7]
]);

t21.concat(t22).print();
t21.concat(t22, 0).print();
t21.concat(t22, 1).print();

// ---

const jd1 = tf.tensor([
    [70, 70, 70],
    [70, 70, 70],
    [70, 70, 70],
    [70, 70, 70],
]);

const jd2 = tf.tensor([
    [1, 160],
    [2, 160],
    [3, 160],
    [4, 160],
]);

// sum all numbers in rows (1 - axis Y)
jd1.sum(1).print();
jd1.sum(1, true).print();
jd1.sum(1).expandDims().print();
jd1.sum(1).expandDims(1).print();

jd1.sum(1, true).concat(jd2, 1).print();
jd1.sum(1).expandDims(1).concat(jd2, 1).print();
