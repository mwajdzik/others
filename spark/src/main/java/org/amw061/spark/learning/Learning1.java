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

public class Learning1 {

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        List<String> logData = asList(
                "WARN: Tuesday 4 September 0405",
                "WARN: Tuesday 4 September 0406",
                "ERROR: Tuesday 4 September 0408",
                "FATAL: Wednesday 5 September 1632",
                "ERROR: Friday 7 September 1854",
                "WARN: Saturday 8 September 1942"
        );

        SparkConf conf = new SparkConf()
                .setAppName("startingSpark")
                .setMaster("local[*]");       // remove for AWS EMR (contains Spark, we don't attach it)

        // load a file (* means combine all matching files to one RDD)
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.textFile("src/main/resources/subtitles/input*.txt")   // s3n://amw061-spark-demos/input.txt
                    .take(7)
                    .forEach(System.out::println);
        }

        System.out.println("--------------------------------");

        // map, reduce, collect
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            List<Integer> inputData = asList(36, 25, 16, 9, 4, 1);
            JavaRDD<Integer> rdd = sc.parallelize(inputData);

            JavaRDD<Double> sqrtRdd = rdd.map(Math::sqrt);
            Double value1 = sqrtRdd.reduce((a, b) -> a + b);
            Integer value2 = rdd.reduce((a, b) -> a + b);
            long count = sqrtRdd.count();

            Long countWithMapReduce = sqrtRdd.map(a -> 1L)
                    .reduce((a, b) -> a + 1);

            rdd.map(value -> new Tuple2<>(value, Math.sqrt(value)));

            System.out.println(value1);
            System.out.println(value2);
            System.out.println(count);
            System.out.println(countWithMapReduce);

            sqrtRdd.collect().forEach(System.out::println);
        }

        System.out.println("--------------------------------");

        // reduceByKey, groupByKey
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            // better because of performance reasons
            sc.parallelize(logData)
                    .mapToPair(value -> new Tuple2<>(value.split(":")[0], 1))
                    .reduceByKey((a, b) -> a + b)
                    .collect()
                    .forEach(System.out::println);

            sc.parallelize(logData)
                    .mapToPair(value -> new Tuple2<>(value.split(":")[0], 1))
                    .groupByKey()
                    .collect()
                    .forEach(System.out::println);
        }

        System.out.println("--------------------------------");

        // flatMap, filter
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.parallelize(logData)
                    .flatMap(line -> asList(line.split("\\s+")).iterator())
                    .filter(word -> word.length() > 1)
                    .collect()
                    .forEach(System.out::println);
        }

        System.out.println("--------------------------------");

        // use .collect() if the expected output can be handled on one machine (respects eg. sort)
        // use .coalesce(n) to enforce using n (eg. 1) partitions - should not be overused (only if we know the number of data will be reduced and there is no need to be distributed so widely)
        // be careful with .foreach()

        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            sc.textFile("src/main/resources/subtitles/input.txt")
                    .flatMap(row -> asList(row.split("[^a-zA-Z]")).iterator())
                    .filter(word -> !word.isEmpty())
                    .filter(word -> word.length() > 5)
                    .map(String::toLowerCase)
                    .mapToPair(word -> new Tuple2<>(word, 1))
                    .reduceByKey((a, b) -> a + b)
                    .mapToPair(tuple -> new Tuple2<>(tuple._2, tuple._1))
                    .sortByKey(false)
                    .take(10)
                    .forEach(System.out::println);
        }

        System.out.println("--------------------------------");

        // joins
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            JavaPairRDD<Integer, String> words = sc.parallelize(logData)
                    .flatMap(line -> asList(line.split("\\s+")).iterator())
                    .mapToPair(word -> new Tuple2<>(word.length(), word));

            JavaPairRDD<Integer, String> dict = sc.parallelizePairs(asList(
                    new Tuple2<>(1, "one"),
                    new Tuple2<>(2, "two"),
                    new Tuple2<>(3, "three")
            ));

            System.out.println(words.join(dict).collect());
            System.out.println(words.leftOuterJoin(dict).collect());
            System.out.println(words.rightOuterJoin(dict).collect());
            System.out.println(words.fullOuterJoin(dict).collect());
            System.out.println(words.cartesian(dict).collect());
        }
    }
}
