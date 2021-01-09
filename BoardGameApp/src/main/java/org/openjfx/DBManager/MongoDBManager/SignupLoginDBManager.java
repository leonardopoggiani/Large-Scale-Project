package org.openjfx.DBManager.MongoDBManager;


import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.openjfx.Entities.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.mongodb.client.model.Filters.eq;


public class SignupLoginDBManager extends MongoDBManager {
    public static void signup(User u){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Document doc = new Document("username", u.getUsername()).append("name", u.getName()).append("surname", u.getSurname())
                .append("age", u.getAge()).append("registered", u.getRegistered()).append("last_login", u.getLastLogin())
                .append("country", u.getCountry()).append("role", u.getRole()).append("updated", u.getUpdated());
        

        collection.insertOne(doc);
    }

    public static void updateLogin(String username){
        System.out.println("Nella update login");
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Document setLastLogin = new Document();
        setLastLogin.append("last_login", dateFormat.format(Calendar.getInstance().getTime()));
        Document update = new Document();
        update.append("$set", setLastLogin);
        collection.updateOne(eq("username", username), update);
    }

}
