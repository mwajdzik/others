package org.amw061.spark.mongo.connector;

import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.ReadConfig;
import com.mongodb.spark.config.WriteConfig;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.primitives.Longs.asList;

public class MongoLearning {

    public static void main(final String[] args) {
        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("MongoSparkConnector")
                .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/db.collection")
                .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/db.collection")
                .getOrCreate();

        try (JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext())) {
            Map<String, String> writeOverrides = new HashMap<>();
            writeOverrides.put("collection", "spark");
            writeOverrides.put("writeConcern.w", "majority");
            WriteConfig writeConfig = WriteConfig.create(jsc).withOptions(writeOverrides);

            JavaRDD<Document> documents = jsc.parallelize(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
                    .map(i -> Document.parse("{test: " + i + "}"));

            MongoSpark.save(documents, writeConfig);

            // ---

            Map<String, String> readOverrides = new HashMap<>();
            readOverrides.put("collection", "spark");
            readOverrides.put("readPreference.name", "secondaryPreferred");
            ReadConfig readConfig = ReadConfig.create(jsc).withOptions(readOverrides);

            JavaMongoRDD<Document> rdd = MongoSpark.load(jsc, readConfig);
            System.out.println(rdd.count());
            System.out.println(rdd.first().toJson());
        }
    }
}
