package it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager;

import com.google.common.annotations.VisibleForTesting;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;
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

/** The type Login signup db manager. */
public class LoginSignupDBManager {

  /**
   * Signup boolean.
   *
   * @param u the u
   * @return the boolean
   */
  public static boolean signup(UserBean u) {
        MongoCollection<Document> collection = getCollection("Users");

        if(userExist(u.getUsername())) {
            Logger.warning("user not exist " + u.getUsername() );
            return false;
        }

        String passEncrypted = passwordEncryption(u.getPassword());

        Document doc = new Document("username", u.getUsername()).append("password", passEncrypted).append("name", u.getName()).append("surname", u.getSurname())
                .append("age", u.getAge()).append("registered", u.getRegistered()).append("last_login", u.getLastLogin())
                .append("country", u.getCountry()).append("role", u.getRole()).append("updated", u.getUpdated());

        try {
            collection.insertOne(doc);
            return true;
        }
        catch (Exception ex){
            Logger.error("user signup exception " + u.getUsername() );
            return false;
        }
    }

  /**
   * User exist boolean.
   *
   * @param username the username
   * @return the boolean
   */
  public static boolean userExist(String username) {
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");

        Bson projection = (fields( excludeId()));
        Bson match =  (eq("username",username));
        boolean exist = false;

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                exist = true;

            }
            cursor.close();
        }
        catch (Exception ex)
        {
            Logger.warning("signup failed " + username + "/ " + ex.getMessage() );
            return  false;
        }
        return exist;
    }

  /**
   * Login user string.
   *
   * @param username the username
   * @param password the password
   * @return the string
   */
  public static String loginUser(String username, String password) {
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");

        String passEncrypted = passwordEncryption(password);

        Bson projection = (fields( excludeId()));
        Bson match =  and(eq("username",username),eq("password", passEncrypted));

        String role = null;


        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                role = next.get("role").toString();
            }
            cursor.close();
        }
        catch (Exception ex)
        {
            Logger.warning("login failed " + username + "/ " + ex.getMessage() );
            return  role;
        }
        return role;
    }

  /**
   * Update login boolean.
   *
   * @param username the username
   * @return the boolean
   */
  public static boolean updateLogin(String username) {
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
            Logger.warning("updateLogin failed " + username + "/ " + ex.getMessage() );
            return false;
        }
    }

  /**
   * Password encryption string.
   *
   * @param passToEncrypt the pass to encrypt
   * @return the string
   */
  @VisibleForTesting
  public static String passwordEncryption(String passToEncrypt) {
        String salt = "randomSalt";
        String encryptedPassword = DigestUtils.sha256Hex(passToEncrypt+salt);
        return encryptedPassword;
    }
}
