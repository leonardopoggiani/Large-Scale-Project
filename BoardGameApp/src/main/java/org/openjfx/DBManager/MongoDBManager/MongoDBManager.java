package org.openjfx.DBManager.MongoDBManager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBManager {
    static MongoClient mongoClient;
    static MongoDatabase database;

    public static void createConnection(){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("Project");
    }

    public static MongoCollection<Document> getCollection(String coll){
        //System.out.println("Dentro alla getCollection");
        MongoCollection<Document> collection = database.getCollection(coll);
        return collection;
    }

    public static void close(){
        mongoClient.close();
    }

}
