package it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBManager {
    static MongoClient mongoClient;
    static MongoDatabase database;

    public static boolean createConnection(){
        try{
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("Project");
            //database = mongoClient.getDatabase("boardgamesApp");
            return true;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }


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
