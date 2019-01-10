package org.amw061.spark.learning;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Scanner;

import static org.apache.spark.sql.functions.*;

public class SqlExercise {

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        try (SparkSession spark = SparkSession.builder()
                .appName("sqlSpark")
                .master("local[*]")
                .getOrCreate()) {

            Dataset<Row> dataSet = spark.read()
                    .option("header", true)
                    .csv("src/main/resources/exams/students.csv");

            Column avgAgr = round(avg("score"), 2).alias("avg");
            Column stdDevAgr = round(stddev("score"), 2).alias("stddev");

            Dataset<Row> pivotTable = dataSet.groupBy(col("subject"))
                    .pivot("year")
                    .agg(avgAgr, stdDevAgr);

            pivotTable.show();

            System.out.println("Open: http://localhost:4040");
            System.out.println("Press ENTER to exit");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        }
    }
}
