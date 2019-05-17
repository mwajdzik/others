package org.amw061.spark.sql;

import com.google.common.collect.Lists;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.text.SimpleDateFormat;
import java.util.List;

import static java.lang.Integer.parseInt;
import static org.apache.spark.sql.functions.*;

public class SqlLearning {

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        if (true)
            try (SparkSession spark = SparkSession.builder()
                    .appName("sqlSpark")
                    .master("local[*]")
                    .getOrCreate()) {

                Dataset<Row> dataSet = spark.read()
                        .option("header", true)
                        .csv("src/main/resources/exams/students.csv");

                dataSet.show();

                System.out.println("Rows count: " + dataSet.count());

                Row firstRow = dataSet.first();
                System.out.println("First row subject: " + firstRow.get(2));
                System.out.println("First row subject: " + firstRow.getAs("subject").toString());

                // ---

                Dataset<Row> filtered1 = dataSet.filter("subject = 'Modern Art' AND year >= 2000");
                System.out.println("Rows count: " + filtered1.count());

                Dataset<Row> filtered2 = dataSet.filter(row ->
                        row.getAs("subject").equals("Modern Art") &&
                                parseInt(row.getAs("year")) >= 2000);

                System.out.println("Rows count: " + filtered2.count());

                Dataset<Row> filtered3 = dataSet.filter(
                        col("subject").equalTo("Modern Art")
                                .and(col("year").geq(2000)));

                System.out.println("Rows count: " + filtered3.count());

                // ---

                dataSet.createOrReplaceTempView("students_view");

                Dataset<Row> results = spark.sql("SELECT * " +
                        "FROM students_view " +
                        "WHERE subject = 'Modern Art' AND year >= 2000 " +
                        "ORDER BY year DESC, score");

                results.show();

                Dataset<Row> maxScore = spark.sql("SELECT MAX(score), AVG(score) " +
                        "FROM students_view " +
                        "WHERE subject = 'Modern Art' AND year >= 2000");

                maxScore.show();
            }


        System.out.println("--------------------------------");


        if (false)
            try (SparkSession spark = SparkSession.builder()
                    .appName("sqlSpark")
                    .master("local[*]")
                    .getOrCreate()) {

                StructField[] fields = new StructField[]{
                        new StructField("level", DataTypes.StringType, false, Metadata.empty()),
                        new StructField("datetime", DataTypes.StringType, false, Metadata.empty())
                };

                List<Row> inMemory = Lists.newArrayList();
                inMemory.add(RowFactory.create("WARN", "2016-12-31 04:19:32"));
                inMemory.add(RowFactory.create("FATAL", "2016-12-31 03:22:34"));
                inMemory.add(RowFactory.create("WARN", "2016-12-31 03:21:21"));
                inMemory.add(RowFactory.create("INFO", "2015-4-21 14:32:21"));
                inMemory.add(RowFactory.create("FATAL", "2015-4-21 19:23:20"));

                Dataset<Row> dataSet = spark.createDataFrame(inMemory, new StructType(fields));
                dataSet.show();

                dataSet.createOrReplaceTempView("logging_view");
                Dataset<Row> results1 = spark.sql("SELECT level, COUNT(level), COLLECT_LIST(datetime) " +
                        "FROM logging_view " +
                        "GROUP BY level ORDER BY level");

                results1.show();

                dataSet.createOrReplaceTempView("logging_view");
                Dataset<Row> results2 = spark.sql("SELECT level, DATE_FORMAT(datetime, 'MMMM') AS month, COUNT(*) AS total " +
                        "FROM logging_view " +
                        "GROUP BY month, level");

                results2.show();
            }


        System.out.println("--------------------------------");


        if (false)
            try (SparkSession spark = SparkSession.builder()
                    .appName("sqlSpark")
                    .master("local[*]")
                    .getOrCreate()) {

                SimpleDateFormat input = new SimpleDateFormat("MMMM");
                SimpleDateFormat output = new SimpleDateFormat("M");

                spark.udf().register("monthNum", (String month) ->
                        parseInt(output.format(input.parse(month))), DataTypes.IntegerType);

                Dataset<Row> dataSet = spark.read()
                        .option("header", true)
                        .csv("src/main/resources/logs/biglog.txt");

                dataSet.createOrReplaceTempView("logging_table");
                Dataset<Row> results = spark.sql("" +
                        "SELECT " +
                        "   level, " +
                        "   DATE_FORMAT(datetime, 'MMMM') AS month, " +
                        "   CAST(FIRST(DATE_FORMAT(datetime, 'M')) AS INT) AS monthNum, " +
                        "   COUNT(*) AS total " +
                        "FROM logging_table " +
                        "GROUP BY month, level " +
                        "ORDER BY monthNum, level, total DESC");

                results = results.drop(col("monthNum"));
                results.show(100);

                Dataset<Row> resultsImproved = spark.sql("" +
                        "SELECT " +
                        "   level, " +
                        "   DATE_FORMAT(datetime, 'MMMM') AS month, " +
                        "   COUNT(*) AS total " +
                        "FROM logging_table " +
                        "GROUP BY month, level " +
                        "ORDER BY monthNum(month), level, total DESC");

                resultsImproved.show(100);
            }


        System.out.println("--------------------------------");


        if (false)
            try (SparkSession spark = SparkSession.builder()
                    .appName("sqlSpark")
                    .master("local[*]")
                    .getOrCreate()) {

                Dataset<Row> dataSet = spark.read()
                        .option("header", true)
                        .csv("src/main/resources/logs/biglog.txt");

                Dataset<Row> result = dataSet
                        .select(
                                col("level"),
                                date_format(col("datetime"), "MMMM")
                                        .as("month"),
                                date_format(col("datetime"), "M")
                                        .as("monthnum")
                                        .cast(DataTypes.IntegerType))
                        .groupBy(col("level"), col("month"), col("monthnum"))
                        .count()
                        .orderBy(col("monthnum"), col("level"))
                        .drop(col("monthnum"));

                result.show();
            }


        System.out.println("--------------------------------");


        if (false)
            try (SparkSession spark = SparkSession.builder()
                    .appName("sqlSpark")
                    .master("local[*]")
                    .getOrCreate()) {

                Dataset<Row> dataSet = spark.read()
                        .option("header", true)
                        .csv("src/main/resources/logs/biglog.txt");

                Dataset<Row> result = dataSet.select(
                        col("level"),
                        date_format(col("datetime"), "MMMM")
                                .as("month"),
                        date_format(col("datetime"), "M")
                                .as("monthnum")
                                .cast(DataTypes.IntegerType))
                        .groupBy(col("level"))
                        .pivot("month")
                        .count()
                        .na().fill(0);

                result.show();
            }


        System.out.println("--------------------------------");


        if (false)
            try (SparkSession spark = SparkSession.builder()
                    .appName("sqlSpark")
                    .master("local[*]")
                    .getOrCreate()) {

                Dataset<Row> dataSet = spark.read()
                        .option("header", true)
                        .csv("src/main/resources/exams/students.csv");

                Dataset<Row> result = dataSet.groupBy(col("subject")).agg(
                        max(col("score").cast(DataTypes.IntegerType)).as("max score"),
                        min(col("score").cast(DataTypes.IntegerType)).as("min score"));

                result.show();
            }


        System.out.println("--------------------------------");


        // User defined functions
        if (false)
            try (SparkSession spark = SparkSession.builder()
                    .appName("sqlSpark")
                    .master("local[*]")
                    .getOrCreate()) {

                spark.udf().register("hasPassed",
                        (String grade, String subject) -> subject.equals("Biology") ? grade.matches("[A](\\+)?") : grade.matches("[ABC](\\+)?"),
                        DataTypes.BooleanType);

                // Java API
                Dataset<Row> dataSet = spark.read()
                        .option("header", true)
                        .csv("src/main/resources/exams/students.csv");

                Dataset<Row> withCustomColumn = dataSet.withColumn("pass", callUDF("hasPassed", col("grade"), col("subject")));

                withCustomColumn.show();
            }
    }
}
