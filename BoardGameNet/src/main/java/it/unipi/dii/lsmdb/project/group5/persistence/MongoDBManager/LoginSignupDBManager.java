package it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.MongoDBManager.getCollection;

public class LoginSignupDBManager {

    public static boolean signup(UserBean u){
        MongoCollection<Document> collection = getCollection("Users");

        if(userExist(u.getUsername()))
            return false;

        String passEncrypted = passwordEncryption(u.getPassword());


        Document doc = new Document("username", u.getUsername()).append("password", passEncrypted).append("name", u.getName()).append("surname", u.getSurname())
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

    public static boolean userExist(String username) {
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");

        Bson projection = (fields( excludeId()));
        Bson match =  (eq("username",username));
        boolean exist = false;

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){
            while(cursor.hasNext()){
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                exist = true;

            }
            cursor.close();
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return  false;
        }
        return exist;
    }

    public static String loginUser(String username, String password) {
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");

        String passEncrypted = passwordEncryption(password);



        Bson projection = (fields( excludeId()));
        Bson match =  and(eq("username",username),eq("password", passEncrypted));

        String role = null;


        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){
            while(cursor.hasNext()){
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                role = next.get("role").toString();
            }
            cursor.close();
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return  role;
        }
        return role;
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

    public static String passwordEncryption(String passToEncrypt)
    {
        String salt = "randomSalt";
        String encryptedPassword = DigestUtils.sha256Hex(passToEncrypt+salt);
        System.out.println("ENCRYPTION | encrypt-pw: " + encryptedPassword);
        return encryptedPassword;
    }
}
