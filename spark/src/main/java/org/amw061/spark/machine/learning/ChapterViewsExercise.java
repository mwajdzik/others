package org.amw061.spark.machine.learning;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.OneHotEncoderEstimator;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.LinearRegressionSummary;
import org.apache.spark.ml.regression.LinearRegressionTrainingSummary;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.ml.tuning.TrainValidationSplit;
import org.apache.spark.ml.tuning.TrainValidationSplitModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.when;

public class ChapterViewsExercise {

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        try (SparkSession spark = SparkSession.builder()
                .appName("sqlSpark")
                .master("local[*]")
                .getOrCreate()) {

            Dataset<Row> csvData = spark.read()
                    .option("header", true)
                    .option("inferSchema", true)
                    .csv("src/main/resources/vppChapterViews/*.csv");

            // filter out rows with is_cancelled==false, drop some irrelevant columns, replace nulls with 0
            csvData = csvData.filter(col("is_cancelled").equalTo("false"))
                    .drop("observation_date", "is_cancelled")
                    .withColumn("firstSub", when(col("firstSub").isNull(), 0).otherwise(col("firstSub")))
                    .withColumn("all_time_views", when(col("all_time_views").isNull(), 0).otherwise(col("all_time_views")))
                    .withColumn("last_month_views", when(col("last_month_views").isNull(), 0).otherwise(col("last_month_views")))
                    .withColumn("next_month_views", when(col("next_month_views").isNull(), 0).otherwise(col("next_month_views")))
                    .withColumnRenamed("next_month_views", "label");

            // transform discrete columns
            StringIndexer periodIndexer = buildIndexer("rebill_period_in_months");
            StringIndexer payMethodIndexer = buildIndexer("payment_method_type");
            StringIndexer countryIndexer = buildIndexer("country");

            csvData = periodIndexer.fit(csvData).transform(csvData);
            csvData = payMethodIndexer.fit(csvData).transform(csvData);
            csvData = countryIndexer.fit(csvData).transform(csvData);

            OneHotEncoderEstimator encoder = new OneHotEncoderEstimator()
                    .setInputCols(new String[]{"rebill_period_in_months", "payment_method_typeIndex", "countryIndex"})
                    .setOutputCols(new String[]{"periodVector", "paymentVector", "countryVector"});

            csvData = encoder.fit(csvData).transform(csvData);
            csvData.show();

            System.out.println("----------------------------------------");

            Dataset<Row> inputData = new VectorAssembler()
                    .setOutputCol("features")
                    .setInputCols(new String[]{"countryVector", "paymentVector", "periodVector",
                            "firstSub", "age", "all_time_views", "last_month_views"})
                    .transform(csvData)
                    .select("label", "features");

            inputData.show();

            System.out.println("----------------------------------------");

            Dataset<Row>[] data = inputData.randomSplit(new double[]{0.9, 0.1});
            Dataset<Row> trainingAndTestData = data[0];
            Dataset<Row> holdOutData = data[1];

            LinearRegression linearRegression = new LinearRegression();

            ParamMap[] paramMap = new ParamGridBuilder()
                    .addGrid(linearRegression.regParam(), new double[]{0.01, 0.1, 0.3, 0.5, 0.7, 1.0})
                    .addGrid(linearRegression.elasticNetParam(), new double[]{0, 0.5, 1})
                    .build();

            TrainValidationSplit trainValidationSplit = new TrainValidationSplit()
                    .setEstimator(linearRegression)
                    .setEvaluator(new RegressionEvaluator().setMetricName("r2"))
                    .setEstimatorParamMaps(paramMap)
                    .setTrainRatio(0.9);

            TrainValidationSplitModel model = trainValidationSplit.fit(trainingAndTestData);
            LinearRegressionModel bestModel = (LinearRegressionModel) model.bestModel();

            LinearRegressionTrainingSummary modelSummary = bestModel.summary();
            System.out.println("The training data R^2 value is: " + modelSummary.r2() + " (the closer to 1, the better)");
            System.out.println("The training data RMSE value is: " + modelSummary.rootMeanSquaredError() + " (the smaller, the better)");
            System.out.println("intercept: " + bestModel.intercept() + ", coefficients: " + bestModel.coefficients());
            System.out.println("regParam: " + bestModel.getRegParam() + ", elasticNetParam: " + bestModel.getElasticNetParam());
            System.out.println();

            Dataset<Row> holdOutResults = bestModel.transform(holdOutData);
            holdOutResults.show();

            holdOutResults = holdOutResults.drop("prediction");
            LinearRegressionSummary testDataSummary = bestModel.evaluate(holdOutResults);
            System.out.println("The test data R^2 value is: " + testDataSummary.r2() + " (the closer to 1, the better)");
            System.out.println("The test data RMSE value is: " + testDataSummary.rootMeanSquaredError() + " (the smaller, the better)");
            System.out.println();
        }
    }

    private static StringIndexer buildIndexer(String column) {
        StringIndexer indexer = new StringIndexer();
        indexer.setInputCol(column);
        indexer.setOutputCol(column + "Index");
        return indexer;
    }
}
