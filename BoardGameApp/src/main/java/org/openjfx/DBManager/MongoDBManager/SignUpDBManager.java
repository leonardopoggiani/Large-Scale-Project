package org.openjfx.DBManager.MongoDBManager;

import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.openjfx.Entities.*;

import java.util.Calendar;


public class SignUpDBManager extends MongoDBManager {
    public static void signup(User u){
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");
        Document doc = new Document("username", u.getUsername()).append("name", u.getName()).append("surname", u.getSurname())
                .append("age", u.getAge()).append("registered", u.getRegistered()).append("last_login", u.getLastLogin())
                .append("country", u.getCountry()).append("role", u.getRole()).append("updated", u.getUpdated());
        

        collection.insertOne(doc);
    }

}
