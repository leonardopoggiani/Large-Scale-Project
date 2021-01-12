package it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBManager {

    //public static void createConnection(){
        static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        static MongoDatabase database = mongoClient.getDatabase("Project");
        //static MongoDatabase database = mongoClient.getDatabase("boardgamesApp");
    //}

    public static MongoCollection<Document> getCollection(String coll){
        //System.out.println("Dentro alla getCollection");
        MongoCollection<Document> collection = database.getCollection(coll);
        return collection;
    }

    public static void dropCollection(MongoCollection<Document> collection){
        collection.drop();
    }

    public static void close(){
        mongoClient.close();
    }

}
