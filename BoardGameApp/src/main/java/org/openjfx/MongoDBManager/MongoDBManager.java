package org.openjfx.MongoDBManager;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import org.bson.Document;

public class MongoDBManager {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static void createConnection(){
        ConnectionString url = new ConnectionString("mongodb://localhost:27017");
        //String url = "mongodb://localhost:27017";
        mongoClient = MongoClients.create(url);
        database = mongoClient.getDatabase("Project");
    }

    public static void close(){
        mongoClient.close();
    }
}
