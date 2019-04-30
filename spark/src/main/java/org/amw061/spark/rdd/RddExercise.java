package org.amw061.spark.rdd;

import org.amw061.spark.Utils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Scanner;

import static java.util.Arrays.asList;
import static org.amw061.spark.Utils.toCounterTuple;
import static org.apache.spark.storage.StorageLevel.MEMORY_AND_DISK;

public class RddExercise {

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkConf conf = new SparkConf()
                .setAppName("startingSpark")
                .setMaster("local[*]");

        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            // userId watched chapterId
            JavaRDD<Tuple2<Integer, Integer>> viewData = sc.parallelize(asList(
                    new Tuple2<>(14, 96),
                    new Tuple2<>(14, 97),
                    new Tuple2<>(13, 96),
                    new Tuple2<>(13, 96),
                    new Tuple2<>(13, 96),
                    new Tuple2<>(14, 99),
                    new Tuple2<>(13, 100)
            ));

            // chapterId belongs to courseId
            JavaPairRDD<Integer, Integer> chapterData = sc.parallelizePairs(asList(
                    new Tuple2<>(96, 1),
                    new Tuple2<>(97, 1),
                    new Tuple2<>(98, 1),
                    new Tuple2<>(99, 2),
                    new Tuple2<>(100, 3),
                    new Tuple2<>(101, 3),
                    new Tuple2<>(102, 3),
                    new Tuple2<>(103, 3),
                    new Tuple2<>(104, 3),
                    new Tuple2<>(105, 3),
                    new Tuple2<>(106, 3),
                    new Tuple2<>(107, 3),
                    new Tuple2<>(108, 3),
                    new Tuple2<>(108, 3)
            ));

            JavaPairRDD<Integer, String> titlesData = sc.parallelizePairs(asList(
                    new Tuple2<>(1, "How to find a better job"),
                    new Tuple2<>(2, "Work faster harder smarter until you drop"),
                    new Tuple2<>(3, "Content Creation is a Mug's Game")
            ));

            // Question - what courses are popular?

            // exercise: comment out and analyze DAG
            chapterData.persist(MEMORY_AND_DISK());

            // RDD containing a key of courseId and number of chapters on the course
            JavaPairRDD<Integer, Long> chaptersInCourse = chapterData.mapToPair(row -> toCounterTuple(row._2))
                    .reduceByKey(Long::sum);                                    // [(1,3), (2,1), (3,10)]

            JavaPairRDD<Integer, Tuple2<Integer, Integer>> joinedRdd = viewData.distinct()
                    .mapToPair(Utils::reverseTuple)                             // (chapterId, (userId, courseId))
                    .join(chapterData);                                         // [(96,(14,1)), (96,(13,1)), (97,(14,1)), (99,(14,2)), (100,(13,3))]

            JavaPairRDD<Tuple2<Integer, Integer>, Long> userCourseViewsCount = joinedRdd
                    .mapToPair(t -> toCounterTuple(t._2))                       // ((userId, courseId), numOfWatchedChaptersBySomeUser)
                    .reduceByKey(Long::sum);                                    // [((13,1),1), ((13,3),1), ((14,2),1), ((14,1),2)]

            JavaPairRDD<Integer, Long> courseViewCount = userCourseViewsCount   // (courseId, numOfWatchedChaptersBySomeUser)
                    .mapToPair(t -> new Tuple2<>(t._1._2, t._2));               // [(1,1), (3,1), (2,1), (1,2)]

            JavaPairRDD<Integer, Tuple2<Long, Long>> courseViewStatistics =     // (courseId, (numOfWatchedChaptersBySomeUser, numberOfChaptersInCourse))
                    courseViewCount.join(chaptersInCourse);                     // [(1,(1,3)), (1,(2,3)), (2,(1,1)), (3,(1,10))]

            JavaPairRDD<Integer, Double> courseViewPercentageStats =            // (courseId, numOfWatchedChaptersBySomeUser / numberOfChaptersInCourse)
                    courseViewStatistics.mapValues(t -> 1.0 * t._1 / t._2);     // [(1,0.333), (1,0.667), (2,1.0), (3,0.1)]

            // > 90% of course watched - 10 points, ...
            JavaPairRDD<Integer, Long> courseScores = courseViewPercentageStats // assign scores
                    .mapValues(RddExercise::courseWatchedToScore);              // [(1,2), (1,4), (2,10), (3,0)]

            JavaPairRDD<String, Long> results = courseScores
                    .reduceByKey(Long::sum)                                     // [(1,6), (2,10), (3,0)]
                    .join(titlesData)                                           // [(1,(6,How to find a better job)), (2,(10,Work faster harder smarter until you drop)), (3,(0,Content Creation is a Mug's Game))]
                    .mapToPair(t -> t._2)                                       // [(6,How to find a better job), (10,Work faster harder smarter until you drop), (0,Content Creation is a Mug's Game)]
                    .sortByKey(false)
                    .mapToPair(Utils::reverseTuple);                            // [(Work faster harder smarter until you drop,10), (How to find a better job,6), (Content Creation is a Mug's Game,0)]

            System.out.println(results.collect());
            System.out.println("Open: http://localhost:4040");
            System.out.println("Press ENTER to exit");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        }
    }

    private static Long courseWatchedToScore(Double v) {
        if (v > 0.9) {
            return 10L;
        } else if (v > 0.5) {
            return 4L;
        } else if (v > 0.25) {
            return 2L;
        } else {
            return 0L;
        }
    }
}
