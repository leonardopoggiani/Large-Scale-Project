package it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import org.bson.Document;

/** The type Mongo db manager. */
public class MongoDBManager {
  /** The Mongo client. */
  static MongoClient mongoClient;

  /** The Database. */
  static MongoDatabase database;

  /**
   * Create connection boolean.
   *
   * @return the boolean
   */
  public static boolean createConnection() {
        try{
            // local connection
            //mongoClient = MongoClients.create("mongodb://localhost:27017");

            // local replicas connection
            //mongoClient = MongoClients.create("mongodb://localhost:27018,localhost:27019, localhost:27020/" + "?retryWrites=true&w=majority&timeout=1000&readPreference=primaryPreferred&maxStalenessSeconds=120");

            // replicas in VM
            mongoClient = MongoClients.create("mongodb://172.16.3.97:27020,172.16.3.98:27020,172.16.3.144:27020/" + "?retryWrites=true&w=majority&timeout=1000&readPreference=primaryPreferred&maxStalenessSeconds=120");

            database = mongoClient.getDatabase("Project");
            // database = mongoClient.getDatabase("project");
            return true;
        }
        catch (Exception ex){
            Logger.error(ex.getMessage());
            return false;
        }


    }

  /**
   * Get collection mongo collection.
   *
   * @param coll the coll
   * @return the mongo collection
   */
  public static MongoCollection<Document> getCollection(String coll) {
        MongoCollection<Document> collection = database.getCollection(coll);
        return collection;
    }

  /** Close. */
  public static void close() {
        mongoClient.close();
    }

}
