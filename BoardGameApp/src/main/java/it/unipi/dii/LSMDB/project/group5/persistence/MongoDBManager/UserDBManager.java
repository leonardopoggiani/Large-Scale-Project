package it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import org.bson.Document;
import it.unipi.dii.LSMDB.project.group5.bean.UserBean;
import org.bson.conversions.Bson;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.ArticleDBManager.*;


public class UserDBManager extends MongoDBManager {
    public static boolean signup(UserBean u){
        MongoCollection<Document> collection = getCollection("Users");
        Document doc = new Document("username", u.getUsername()).append("name", u.getName()).append("surname", u.getSurname())
                .append("age", u.getAge()).append("registered", u.getRegistered()).append("last_login", u.getLastLogin())
                .append("country", u.getCountry()).append("role", u.getRole()).append("updated", u.getUpdated());
        
        try {
            collection.insertOne(doc);
            return true;
        }
        catch (Exception ex){

            return false;
        }
    }

    public static boolean updateLogin(String username) {
        //System.out.println("Nella update login");
        MongoCollection<Document> collection = getCollection("Users");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Document setLastLogin = new Document();
        setLastLogin.append("last_login", dateFormat.format(Calendar.getInstance().getTime()));
        Document update = new Document();
        update.append("$set", setLastLogin);
        try {
            collection.updateOne(eq("username", username), update);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    protected static UserBean fillUserFields(Document next) {
        UserBean u = new UserBean();
        u.setName((next.get("name")==null)?"": next.get("name").toString());
        u.setSurname((next.get("surname")==null)?"":next.get("surname").toString());
        u.setAge((next.get("age")==null) ? 18: Integer.parseInt(next.get("age").toString()));
        u.setRegistered((next.get("registered") == null) ? 2020 : Integer.parseInt(next.get("registered").toString()) );
        u.setLastLogin((next.get("last_login")==null)? new Timestamp(System.currentTimeMillis()) : convertStringToTimestamp(next.get("last_login").toString()));
        u.setCountry(next.get("country")==null? "Non specificato" : next.get("country").toString());
        u.setUpdated((next.get("updated")==null)? new Timestamp(System.currentTimeMillis()) : convertStringToTimestamp(next.get("updated").toString()));;
        return u;
    }

    public static boolean deleteUser (String username){
        MongoCollection<Document> collection = getCollection("Users");

        DeleteResult dr = collection.deleteOne(eq("username", username));
        if (dr.getDeletedCount() == 0 || !dr.wasAcknowledged()){
            return false;
        }
        return true;
    }

    public static UserBean showUser(String username) {
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");

        Bson projection = (fields( excludeId()));
        Bson match =  (eq("username",username));

        UserBean u = new UserBean();

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){
            while(cursor.hasNext()){
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                u = fillUserFields(next);
            }
            cursor.close();
        }
        return u;
    }

}
