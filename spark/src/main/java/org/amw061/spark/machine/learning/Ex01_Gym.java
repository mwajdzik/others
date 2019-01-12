package org.amw061.spark.machine.learning;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.feature.OneHotEncoderEstimator;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Ex01_Gym {

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        try (SparkSession spark = SparkSession.builder()
                .appName("sqlSpark")
                .master("local[*]")
                .getOrCreate()) {

            Dataset<Row> dataSet = spark.read()
                    .option("header", true)
                    .option("inferSchema", true)    // spoils performance
                    .csv("src/main/resources/gym/GymCompetition.csv");

            dataSet.printSchema();

            // create a new column with gender vectors (used for discrete values)
            StringIndexer genderIndexer = new StringIndexer();
            genderIndexer.setInputCol("Gender");
            genderIndexer.setOutputCol("GenderIndex");
            dataSet = genderIndexer.fit(dataSet).transform(dataSet);
            dataSet.show();

            OneHotEncoderEstimator genderEncoder = new OneHotEncoderEstimator();
            genderEncoder.setInputCols(new String[] {"GenderIndex"});
            genderEncoder.setOutputCols(new String[] {"GenderVector"});
            dataSet = genderEncoder.fit(dataSet).transform(dataSet);
            dataSet.show();

            // (6,[2],[1]) == [0, 0, 1, 0, 0, 0]

            VectorAssembler featuresVector = new VectorAssembler();
            featuresVector.setInputCols(new String[]{"Age", "Height", "Weight", "GenderVector"});
            featuresVector.setOutputCol("features");

            Dataset<Row> modelInput = featuresVector.transform(dataSet)
                    .select("NoOfReps", "features")
                    .withColumnRenamed("NoOfReps", "label");

            modelInput.show();

            LinearRegression linearRegression = new LinearRegression();
            LinearRegressionModel model = linearRegression.fit(modelInput);
            System.out.println("intercept: " + model.intercept() + ", coefficients: " + model.coefficients());
            model.transform(modelInput).show();
        }
    }
}
