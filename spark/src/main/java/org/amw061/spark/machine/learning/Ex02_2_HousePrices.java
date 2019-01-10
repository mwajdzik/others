package org.amw061.spark.machine.learning;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
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

// https://www.kaggle.com/harlfoxem/housesalesprediction/version/1
public class Ex02_2_HousePrices {

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

            VectorAssembler featuresVector = new VectorAssembler();
            featuresVector.setInputCols(new String[]{"bedrooms", "bathrooms", "sqft_living", "sqft_lot", "floors", "grade"});
            featuresVector.setOutputCol("features");

            Dataset<Row> modelInput = featuresVector.transform(dataSet)
                    .select("price", "features")
                    .withColumnRenamed("price", "label");

            modelInput.show();

            Dataset<Row>[] data = modelInput.randomSplit(new double[]{0.8, 0.2});
            Dataset<Row> trainingAndTestData = data[0];
            Dataset<Row> holdOutData = data[1];

            LinearRegression linearRegression = new LinearRegression();
            ParamGridBuilder paramGridBuilder = new ParamGridBuilder();

            // use a matrix of different params to find out the best set of params
            ParamMap[] paramMap = paramGridBuilder
                    .addGrid(linearRegression.regParam(), new double[] {0.01, 0.1, 0.5})
                    .addGrid(linearRegression.elasticNetParam(), new double[] {0, 0.5, 1})
                    .build();

            TrainValidationSplit trainValidationSplit = new TrainValidationSplit()
                    .setEstimator(linearRegression)
                    .setEvaluator(new RegressionEvaluator().setMetricName("r2"))
                    .setEstimatorParamMaps(paramMap)
                    .setTrainRatio(0.8);

            TrainValidationSplitModel validationModel = trainValidationSplit.fit(trainingAndTestData);

            LinearRegressionModel model = (LinearRegressionModel) validationModel.bestModel();
            System.out.println("intercept: " + model.intercept() + ", coefficients: " + model.coefficients());

            Dataset<Row> predictions = model.transform(holdOutData);
            predictions.show();

            LinearRegressionTrainingSummary modelSummary = model.summary();
            System.out.println("The training data R^2 value is: " + modelSummary.r2() + " (the closer to 1, the better)");
            System.out.println("The training data RMSE value is: " + modelSummary.rootMeanSquaredError() + " (the smaller, the better)");
            System.out.println();

            LinearRegressionSummary testDataSummary = model.evaluate(holdOutData);
            System.out.println("The test data R^2 value is: " + testDataSummary.r2() + " (the closer to 1, the better)");
            System.out.println("The test data RMSE value is: " + testDataSummary.rootMeanSquaredError() + " (the smaller, the better)");
            System.out.println();
        }
    }
}
