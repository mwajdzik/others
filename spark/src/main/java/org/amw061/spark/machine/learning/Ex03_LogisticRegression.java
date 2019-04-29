package org.amw061.spark.machine.learning;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.classification.LogisticRegressionSummary;
import org.apache.spark.ml.classification.LogisticRegressionTrainingSummary;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.OneHotEncoderEstimator;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.ml.tuning.TrainValidationSplit;
import org.apache.spark.ml.tuning.TrainValidationSplitModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.when;

public class Ex03_LogisticRegression {

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
                    // 1 - customer watched no videos, 0 - customer watched some videos - it will be our prediction
                    .withColumn("next_month_views", when(col("next_month_views").$greater(0), 0).otherwise(1))
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

            LogisticRegression logisticRegression = new LogisticRegression();

            ParamMap[] paramMap = new ParamGridBuilder()
                    .addGrid(logisticRegression.regParam(), new double[]{0.01, 0.1, 0.3, 0.5, 0.7, 1.0})
                    .addGrid(logisticRegression.elasticNetParam(), new double[]{0, 0.5, 1})
                    .build();

            TrainValidationSplit trainValidationSplit = new TrainValidationSplit()
                    .setEstimator(logisticRegression)
                    .setEvaluator(new RegressionEvaluator().setMetricName("r2"))
                    .setEstimatorParamMaps(paramMap)
                    .setTrainRatio(0.9);

            TrainValidationSplitModel model = trainValidationSplit.fit(trainingAndTestData);
            LogisticRegressionModel bestModel = (LogisticRegressionModel) model.bestModel();

            LogisticRegressionTrainingSummary modelSummary = bestModel.summary();
            System.out.println("The training data accuracy score is: " + modelSummary.accuracy());
            System.out.println("intercept: " + bestModel.intercept() + ", coefficients: " + bestModel.coefficients());
            System.out.println("regParam: " + bestModel.getRegParam() + ", elasticNetParam: " + bestModel.getElasticNetParam());
            System.out.println();

            LogisticRegressionSummary regressionSummary = bestModel.evaluate(holdOutData);
            double[] truePositiveNegative = regressionSummary.truePositiveRateByLabel();
            double[] falsePositiveNegative = regressionSummary.falsePositiveRateByLabel();

            double truePositives = truePositiveNegative[1];
            double falsePositives = falsePositiveNegative[0];
            double probability = truePositives / (truePositives + falsePositives);

            System.out.println("For the holdout data, the likelihood of a positive being correct is " + probability);
            System.out.println("The holdout data accuracy score is: " + regressionSummary.accuracy());

            bestModel.transform(holdOutData)
                    .groupBy("label", "prediction")
                    .count()
                    .show();
        }
    }

    private static StringIndexer buildIndexer(String column) {
        StringIndexer indexer = new StringIndexer();
        indexer.setInputCol(column);
        indexer.setOutputCol(column + "Index");
        return indexer;
    }
}
