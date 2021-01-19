package it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.Timestamp;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.ArticleDBManager.convertStringToTimestamp;


public class UserDBManager extends MongoDBManager {


    protected static UserBean fillUserFields(Document next) {
        UserBean u = new UserBean();
        u.setUsername((next.get("username")==null)?"": next.get("username").toString());
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
        MongoCollection<Document> collection = getCollection("Users");

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

    public static boolean promoteDemoteUser(String username, String role) {
        //System.out.println("Nella update login");
        MongoCollection<Document> collection = getCollection("Users");
        Document setRole = new Document();
        setRole.append("role",role);
        Document update = new Document();
        update.append("$set", setRole);
        try {
            collection.updateOne(eq("username", username), update);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
