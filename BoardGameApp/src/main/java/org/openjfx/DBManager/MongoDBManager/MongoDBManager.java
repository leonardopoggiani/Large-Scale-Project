package org.openjfx.DBManager.MongoDBManager;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.eq;

import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

import com.mongodb.internal.connection.Connection;
import org.bson.Document;
import jdk.net.ExtendedSocketOptions;

public class MongoDBManager {
    static MongoClient mongoClient;
    static MongoDatabase database;

    public static void createConnection(){
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("Project");
    }

    public static MongoCollection<Document> getCollection(String coll){
        System.out.println("Dentro alla getCollection");
        MongoCollection<Document> collection = database.getCollection(coll);
        return collection;
    }

    public static void close(){
        mongoClient.close();
    }

}
