package it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager;


import com.mongodb.client.MongoCollection;
import org.bson.Document;
import it.unipi.dii.LSMDB.project.group5.bean.UserBean;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.mongodb.client.model.Filters.eq;
import static it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.ArticleDBManager.*;


public class UserDBManager extends MongoDBManager {
    public static boolean signup(UserBean u){
        MongoCollection<Document> collection = getCollection("Users");
        Document doc = new Document("username", u.getUsername()).append("name", u.getName()).append("surname", u.getSurname())
                .append("age", u.getAge()).append("registered", u.getRegistered()).append("last_login", u.getLastLogin())
                .append("country", u.getCountry()).append("role", u.getRole()).append("updated", u.getUpdated());
        
        try {
            collection.insertOne(doc);
            //MongoDBManager.dropCollection(collection);
            return true;
        }
        catch (Exception ex){
            //MongoDBManager.dropCollection(collection);
            return false;
        }
    }

    public static boolean updateLogin(String username){
        System.out.println("Nella update login");
        MongoCollection<Document> collection = getCollection("Users");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Document setLastLogin = new Document();
        setLastLogin.append("last_login", dateFormat.format(Calendar.getInstance().getTime()));
        Document update = new Document();
        update.append("$set", setLastLogin);
        try{
            collection.updateOne(eq("username", username), update);
            //MongoDBManager.dropCollection(collection);
            return true;
        }
        catch (Exception ex){
            //MongoDBManager.dropCollection(collection);
            return false;
        }

    }
	
	 protected static UserBean fillUserFields (Document next){
        UserBean u = new UserBean();
        u.setUsername((next.get("username") == null)? "":next.get("username").toString());
        u.setName((next.get("name")==null)?"": next.get("name").toString());
        u.setSurname((next.get("surname")==null)?"":next.get("surname").toString());
        u.setAge((next.get("age")==null) ? 18: Integer.parseInt(next.get("age").toString()));
        u.setRegistered((next.get("registered") == null) ? 2020 : Integer.parseInt(next.get("registered").toString()) );
        u.setLastLogin((next.get("last_login")==null)? new Timestamp(System.currentTimeMillis()) : convertStringToTimestamp(next.get("last_login").toString()));
        u.setCountry(next.get("country")==null? "Non specificato" : next.get("country").toString());
        u.setUpdated((next.get("updated")==null)? new Timestamp(System.currentTimeMillis()) : convertStringToTimestamp(next.get("updated").toString()));;
        return u;
    }


}
