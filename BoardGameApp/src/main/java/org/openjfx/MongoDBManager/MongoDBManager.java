package org.openjfx.MongoDBManager;


import com.mongodb.client.*;
import com.mongodb.*;



public class MongoDBManager {
    private static com.mongodb.client.MongoClient mongoClient;
    private static MongoDatabase database;

    public static void createConnection(){
        com.mongodb.ConnectionString url = new ConnectionString("mongodb://localhost:27017");
        //String url = "mongodb://localhost:27017";
        mongoClient =MongoClients.create(url);
        database = mongoClient.getDatabase("Project");

    }

    public static void close(){
        mongoClient.close();
    }
}
