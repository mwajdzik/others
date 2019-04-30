package org.amw061.spark.learning;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.List;

import static java.util.Arrays.asList;
import static org.amw061.spark.learning.Utils.toCounterTuple;

public class RddLearning {

    private static final List<String> LOG_DATA = asList(
            "WARN: Tuesday 4 September 0405",
            "WARN: Tuesday 4 September 0406",
            "ERROR: Tuesday 4 September 0408",
            "FATAL: Wednesday 5 September 1632",
            "ERROR: Friday 7 September 1854",
            "WARN: Saturday 8 September 1942"
    );

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.ERROR);

        SparkConf conf = new SparkConf()
                .setAppName("my spark app")
                .setMaster("local[*]");                                     // remove for AWS EMR (contains Spark, we don't attach it)

        System.out.println("\n--------------------------------\n");


        // load a file (* means combine all matching files to one RDD)
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.textFile("src/main/resources/subtitles/input*.txt")          // for S3: s3n://amw061-spark-demos/input.txt
                    .take(3)
                    .forEach(System.out::println);
        }

        System.out.println("\n--------------------------------\n");


        // map              - transformation
        // reduce           - action
        // count            - action
        // collect          - action
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            JavaRDD<Integer> rdd = sc.parallelize(asList(36, 25, 16, 9, 4, 1));

            JavaRDD<Double> sqrtRdd = rdd.map(Math::sqrt);
            Double value1 = sqrtRdd.reduce(Double::sum);
            Integer value2 = rdd.reduce(Integer::sum);
            long count = sqrtRdd.count();

            Long countWithMapReduce = sqrtRdd
                    .map(a -> 1L)
                    .reduce((a, b) -> a + 1);

            JavaRDD<Tuple2<Integer, Double>> valueAndSquare =
                    rdd.map(value -> new Tuple2<>(value, Math.sqrt(value)));

            System.out.println("Sum of squares: " + value1);
            System.out.println("Sum of numbers: " + value2);
            System.out.println("Count of numbers: " + count);
            System.out.println("Count of numbers: " + countWithMapReduce);
            System.out.println("All squares collected to a list: " + sqrtRdd.collect());
            System.out.println("Tuples: " + valueAndSquare.collect());
        }

        System.out.println("\n--------------------------------\n");


        // mapToPair        - transformation
        // reduceByKey
        // groupByKey
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            System.out.println("reduceByKey:");
            sc.parallelize(LOG_DATA)
                    .mapToPair(value -> toCounterTuple(logLevel(value)))
                    .reduceByKey(Long::sum)                                 // better because of performance reasons (no shuffling)
                    .collect()
                    .forEach(System.out::println);

            System.out.println("\ngroupByKey:");
            sc.parallelize(LOG_DATA)
                    .mapToPair(value -> toCounterTuple(logLevel(value)))
                    .groupByKey()                                           // enforces shuffling (wide transformation)
                    .collect()
                    .forEach(System.out::println);
        }

        System.out.println("\n--------------------------------\n");


        // flatMap          - transformation
        // filter           - transformation
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.parallelize(LOG_DATA)
                    .flatMap(line -> asList(line.split("\\s+")).iterator())
                    .filter(word -> word.length() > 1)
                    .filter(word -> !word.endsWith(":"))
                    .collect()
                    .forEach(System.out::println);
        }

        System.out.println("\n--------------------------------\n");


        // use .collect() if the expected output can be handled on one machine (respects eg. sort)
        // use .coalesce(n) to enforce using n (eg. 1) partitions - should not be overused
        // (only if we know the number of data will be reduced and there is no need to be distributed so widely)
        // be careful with .foreach() - it does not behave as .take

        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.textFile("src/main/resources/subtitles/input*.txt")
                    .flatMap(row -> asList(row.split("[^a-zA-Z]")).iterator())
                    .filter(word -> word.length() > 5)
                    .map(String::toLowerCase)
                    .mapToPair(Utils::toCounterTuple)
                    .reduceByKey(Long::sum)
                    .mapToPair(Utils::reverseTuple)
                    .sortByKey(false)
                    .mapToPair(Utils::reverseTuple)
                    .take(10)
                    .forEach(System.out::println);
        }

        System.out.println("\n--------------------------------\n");


        // Joins:
        //      join
        //      leftOuterJoin
        //      rightOuterJoin
        //      fullOuterJoin
        //      cartesian
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            JavaPairRDD<Integer, String> words = sc.parallelizePairs(asList(
                    new Tuple2<>(1, "jeden"),
                    new Tuple2<>(2, "dwa"),
                    new Tuple2<>(3, "trzy"),
                    new Tuple2<>(5, "pięć")
            ));

            JavaPairRDD<Integer, String> dict = sc.parallelizePairs(asList(
                    new Tuple2<>(1, "one"),
                    new Tuple2<>(2, "two"),
                    new Tuple2<>(3, "three"),
                    new Tuple2<>(4, "four")
            ));

            System.out.println(words.join(dict).collect());
            System.out.println(words.leftOuterJoin(dict).collect());
            System.out.println(words.rightOuterJoin(dict).collect());
            System.out.println(words.fullOuterJoin(dict).collect());
            System.out.println(words.cartesian(dict).collect());
        }
    }

    private static String logLevel(String log) {
        return log.split(":")[0];
    }
}
