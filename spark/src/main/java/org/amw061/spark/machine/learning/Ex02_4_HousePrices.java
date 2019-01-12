package org.amw061.spark.machine.learning;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.*;
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

// https://www.kaggle.com/harlfoxem/housesalesprediction/version/1
public class Ex02_4_HousePrices {

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        try (SparkSession spark = SparkSession.builder()
                .appName("sqlSpark")
                .master("local[*]")
                .getOrCreate()) {

            Dataset<Row> dataSet = spark.read()
                    .option("header", true)
                    .option("inferSchema", true)
                    .csv("src/main/resources/gym/kc_house_data.csv");

            dataSet = dataSet.withColumn("sqft_above_percentage", col("sqft_above").divide(col("sqft_living")))
                    .withColumnRenamed("price", "label");

            Dataset<Row>[] data = dataSet.randomSplit(new double[]{0.8, 0.2});
            Dataset<Row> trainingAndTestData = data[0];
            Dataset<Row> holdOutData = data[1];

            StringIndexer conditionIndexer = buildIndexer("condition");
            StringIndexer gradeIndexer = buildIndexer("grade");
            StringIndexer zipcodeIndexer = buildIndexer("zipcode");

            OneHotEncoderEstimator conditionEncoder = new OneHotEncoderEstimator()
                    .setInputCols(new String[]{"conditionIndex", "gradeIndex", "zipcodeIndex"})
                    .setOutputCols(new String[]{"conditionVector", "gradeVector", "zipcodeVector"});

            VectorAssembler vectorAssembler = new VectorAssembler()
                    .setOutputCol("features")
                    .setInputCols(new String[]{"bedrooms", "bathrooms", "sqft_living",
                            "waterfront", "sqft_above_percentage", "floors",
                            "conditionVector", "gradeVector", "zipcodeVector"});

            LinearRegression linearRegression = new LinearRegression();

            ParamMap[] paramMap = new ParamGridBuilder()
                    .addGrid(linearRegression.regParam(), new double[]{0.01, 0.1, 0.5})
                    .addGrid(linearRegression.elasticNetParam(), new double[]{0, 0.5, 1})
                    .build();

            TrainValidationSplit trainValidationSplit = new TrainValidationSplit()
                    .setEstimator(linearRegression)
                    .setEvaluator(new RegressionEvaluator().setMetricName("r2"))
                    .setEstimatorParamMaps(paramMap)
                    .setTrainRatio(0.8);

            PipelineModel pipelineModel = new Pipeline()
                    .setStages(new PipelineStage[]{
                            conditionIndexer,
                            gradeIndexer,
                            zipcodeIndexer,
                            conditionEncoder,
                            vectorAssembler,
                            trainValidationSplit
                    })
                    .fit(trainingAndTestData);

            TrainValidationSplitModel lastStageModel = (TrainValidationSplitModel) pipelineModel.stages()[5];
            LinearRegressionModel model = (LinearRegressionModel) lastStageModel.bestModel();

            System.out.println("intercept: " + model.intercept() + ", coefficients: " + model.coefficients());
            System.out.println("regParam: " + model.getRegParam() + ", elasticNetParam: " + model.getElasticNetParam());

            Dataset<Row> holdOutResults = pipelineModel.transform(holdOutData);
            holdOutResults.show();

            LinearRegressionTrainingSummary modelSummary = model.summary();
            System.out.println("The training data R^2 value is: " + modelSummary.r2() + " (the closer to 1, the better)");
            System.out.println("The training data RMSE value is: " + modelSummary.rootMeanSquaredError() + " (the smaller, the better)");
            System.out.println();

            holdOutResults = holdOutResults.drop("prediction");
            LinearRegressionSummary testDataSummary = model.evaluate(holdOutResults);
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
