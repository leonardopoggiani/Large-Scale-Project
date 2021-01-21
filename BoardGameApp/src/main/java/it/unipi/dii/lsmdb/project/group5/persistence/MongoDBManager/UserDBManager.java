package it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager;


import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.sql.Timestamp;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.ArticleDBManager.convertStringToTimestamp;

/** The type User db manager. */
public class UserDBManager extends MongoDBManager {

  /**
   * Fill user fields user bean.
   *
   * @param next the next
   * @return the user bean
   */
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

  /**
   * Delete user boolean.
   *
   * @param username the username
   * @return the boolean
   */
  public static boolean deleteUser(String username) {
        MongoCollection<Document> collection = getCollection("Users");

        DeleteResult dr = collection.deleteOne(eq("username", username));
        if (dr.getDeletedCount() == 0 || !dr.wasAcknowledged()){
            return false;
        }
        return true;
    }

  /**
   * Show user user bean.
   *
   * @param username the username
   * @return the user bean
   */
  public static UserBean showUser(String username) {
        MongoCollection<Document> collection = getCollection("Users");

        Bson projection = (fields( excludeId()));
        Bson match =  (eq("username",username));

        UserBean u = new UserBean();

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                u = fillUserFields(next);
            }
            cursor.close();
        }
        return u;
    }

  /**
   * Promote demote user boolean.
   *
   * @param username the username
   * @param role the role
   * @return the boolean
   */
  public static boolean promoteDemoteUser(String username, String role) {
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

  /**
   * Show all users list.
   *
   * @return the list
   */
  public static List<UserBean> showAllUsers() {
        MongoCollection<Document> collection = getCollection("Users");

        Bson projection = (fields( excludeId()));

        List<UserBean> b = Lists.newArrayList();
        UserBean u = new UserBean();

        try(MongoCursor<Document> cursor = collection.find().projection(projection).iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                u = fillUserFields(next);
                b.add(u);
            }
            cursor.close();
        }
        return b;
    }

  /**
   * Show all influencer list.
   *
   * @return the list
   */
  public static List<UserBean> showAllInfluencer() {
        MongoCollection<Document> collection = getCollection("Users");

        Bson projection = (fields( excludeId()));
        Bson match = (eq("role","influencer"));

        List<UserBean> b = Lists.newArrayList();
        UserBean u = new UserBean();

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                u = fillUserFields(next);
                b.add(u);
            }
            cursor.close();
        }
        return b;
    }

  /**
   * Modify profile boolean.
   *
   * @param username the username
   * @param name the name
   * @param surname the surname
   * @param password the password
   * @param age the age
   * @param categoria1 the categoria 1
   * @param categoria2 the categoria 2
   * @return the boolean
   */
  public static boolean modifyProfile(
      String username,
      String name,
      String surname,
      String password,
      String age,
      String categoria1,
      String categoria2) {

        MongoCollection<Document> collection = getCollection("Users");
        Document updateProfile = new Document();

        if( name != null && !name.equals("")){
            updateProfile.append("name", name);
        }

        if( surname != null && !surname.equals("")) {
            updateProfile.append("surname", surname);
        }

        if( password != null && !password.equals("")) {
            updateProfile.append("password", surname);
        }

        if( age != null && !age.equals("")) {
            updateProfile.append("age", age);
        }

        if( categoria1 != null && !categoria1.equals("")) {
            updateProfile.append("age", age);
        }

        if( categoria2 != null && !categoria2.equals("")) {
            updateProfile.append("age", age);
        }

        Document update = new Document();
        update.append("$set", updateProfile);

        try {
            collection.updateOne(eq("username", username), update);

            return true;
        } catch (Exception ex) {
                return false;
        }
    }

}
