package org.amw061.spark.machine.learning;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

// https://www.kaggle.com/harlfoxem/housesalesprediction/version/1
public class Ex02_3_HousePrices {

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        try (SparkSession spark = SparkSession.builder()
                .appName("sqlSpark")
                .master("local[*]")
                .getOrCreate()) {

            Dataset<Row> dataSet = spark.read()
                    .option("header", true)
                    .option("inferSchema", true)    // spoils performance
                    .csv("src/main/resources/gym/kc_house_data.csv");

            dataSet.printSchema();
            dataSet = dataSet.drop("id", "view", "waterfront", "grade", "condition", "zipcode", "date", "yr_renovated", "lat", "long");

            // provide data statistics
            dataSet.describe().show();

            // correlation (0 - no correlation, +1/-1 no correlation)
            for (String col : dataSet.columns()) {
                System.out.printf("price and %s correlation is: %.2f%n", col, dataSet.stat().corr("price", col));
            }

            System.out.println();
            dataSet = dataSet.drop("sqft_lot", "sqft_lot15", "sqft_living15", "yr_built");

            // build a correlation matrix
            for (String col1 : dataSet.columns()) {
                for (String col2 : dataSet.columns()) {
                    System.out.printf("%s and %s correlation is: %.2f%n", col1, col2, dataSet.stat().corr(col1, col2));
                }
            }
        }
    }
}
