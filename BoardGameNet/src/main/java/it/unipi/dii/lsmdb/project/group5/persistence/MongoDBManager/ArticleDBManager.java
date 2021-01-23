package it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager;


import com.google.common.annotations.VisibleForTesting;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.lsmdb.project.group5.bean.ArticleBean;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

/** The type Article db manager. */
public class ArticleDBManager {

  /**
   * Read article article bean.
   *
   * @param id the id
   * @return the article bean
   */
  public static ArticleBean readArticle(int id) {
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Bson projection = (fields(excludeId()));
        Bson match =  (eq("id",id));

        ArticleBean a = new ArticleBean();

       try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                a = fillArticleFields(next, false);
            }
            cursor.close();
       } catch (Exception e) {
           Logger.error(e.getMessage());
       }


       return a;

    }

  /**
   * Filter by influencer list.
   *
   * @param influencer the influencer
   * @return the list
   */
  public static List<ArticleBean> filterByInfluencer(String influencer) {
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");

        Bson projection = (fields( excludeId()));
        Bson match =  (eq("author",influencer));

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()) {

            while (cursor.hasNext()) {
                Document next = (Document)cursor.next();
                ArticleBean a = fillArticleFields(next,false);
                ret.add(a);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }

      return ret;
    }

  /**
   * Filter by game list.
   *
   * @param game the game
   * @return the list
   */
  public static List<ArticleBean> filterByGame(String game) {
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");

        Bson unwind1 = unwind("$games");
        Bson projection = project(fields( excludeId()));
        Bson match =  match(and(eq("games",game)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind1,match,projection)).iterator()) {


            while (cursor.hasNext()) {
                Document next = (Document) cursor.next();
                ArticleBean a = fillArticleFields(next,true);
                ret.add(a);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }


      return ret;
    }

  /**
   * Filter by date list.
   *
   * @param date the date
   * @return the list
   */
  public static List<ArticleBean> filterByDate(String date) {
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");


        Bson projection = (fields( excludeId()));
        Bson match =  (gte("timestamp",date));

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                ArticleBean a = fillArticleFields(next,false);
                ret.add(a);

            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }


      return ret;
    }

  /**
   * Order by list.
   *
   * @param mode the mode
   * @return the list
   */
  public static List<ArticleBean> orderBy(String mode) {
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");


        Bson projection = (fields( excludeId()));
        Bson sort = null;
        Bson match = null;
        if(mode.equals("like")){
            match = (and(ne("num_likes", null), ne("num_likes", ""),ne("num_likes", "nan")));
            sort = (descending("num_likes"));
        } else if (mode.equals("dislike")){
            match = (and(ne("num_dislikes", null), ne("num_dislikes", ""), ne("num_dislikes", "nan")));
            sort = (descending("num_dislikes"));
        } else {
            match = (and(ne("num_comments", null), ne("num_comments", ""), ne("num_comments", "nan")));
            sort = (descending("num_comments"));
        }
        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).sort(sort).limit(6).iterator()) {

        while (cursor.hasNext()) {
                Document next = cursor.next();
                ArticleBean g = fillArticleFields(next,false);
                ret.add(g);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
      return ret;

    }

  /**
   * Update num like boolean.
   *
   * @param inc the inc
   * @param id the id
   * @return the boolean
   */
  public static boolean updateNumLike(int inc, int id) {
        int tot = getNumLikes(id);
        if (tot == -1){
            //Cannot get the number of likes
            return false;
        }
        tot = tot + inc;
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Document updateLike = new Document();
        updateLike.append("num_likes", tot);
        Document update = new Document();
        update.append("$set", updateLike);
        Document query = new Document();
        query.append("id", id);
        try{
            UpdateResult res = collection.updateOne(query, update);
            if (res.getModifiedCount() == 0 || !res.wasAcknowledged()){
                Logger.error("unable to reach mongoDB");
                return false;
            }

            return true;
        }
        catch (Exception ex) {
            Logger.error(ex.getMessage());
            return false;
        }

    }

  /**
   * Update num dislike boolean.
   *
   * @param inc the inc
   * @param id the id
   * @return the boolean
   */
  public static boolean updateNumDislike(int inc, int id) {
        int tot = getNumDislikes(id);
        if (tot == -1){
            //Cannot get the number of dislikes
            return false;
        }
        tot = tot + inc;
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Document updateLike = new Document();
        updateLike.append("num_dislikes", tot);
        Document update = new Document();
        update.append("$set", updateLike);
        Document query = new Document();
        query.append("id", id);

        try{
            UpdateResult res = collection.updateOne(query, update);
            if (res.getModifiedCount() == 0 || !res.wasAcknowledged()){
                Logger.error("unable to update MongoDB");
                return false;
            }collection.updateOne(query, update);

            return true;
        }
        catch (Exception ex) {
            Logger.error(ex.getMessage());
            return false;
        }
    }

  /**
   * Update num comments boolean.
   *
   * @param inc the inc
   * @param id the id
   * @return the boolean
   */
  public static boolean updateNumComments(int inc, int id) {
        int tot = getNumComments(id);
        if (tot == -1){
            //Cannot get the number of comments
            return false;
        }
        tot = tot + inc;
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Document updateLike = new Document();
        updateLike.append("num_comments", tot);
        Document update = new Document();
        update.append("$set", updateLike);
        Document query = new Document();
        query.append("id", id);
        try{
            UpdateResult res = collection.updateOne(query, update);
            if (res.getModifiedCount() == 0 || !res.wasAcknowledged()){
                Logger.error("Unable to update MongoDB");
                return false;
            }

            return true;
        }
        catch (Exception ex) {
            Logger.error(ex.getMessage());
            return false;
        }
    }

  /**
   * Get num comments int.
   *
   * @param id the id
   * @return the int
   */
  public static int getNumComments(int id) {
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Bson projection = (fields( excludeId()));
        Bson match =  (eq("id",id));
        int ret = 0;

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                ret = (next.get("num_comments") == null) ? 0 :Integer.parseInt(next.get("num_comments").toString());

            }
        }catch(Exception ex){
            Logger.error("Unable to reach MongoDB");
            return -1;
        }

        return ret;
    }

    private static int getNumLikes(int id){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Bson projection = (fields( excludeId()));
        Bson match =  (eq("id", id));
        int ret = 0;

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                ret = (next.get("num_likes") == null) ? 0 :Integer.parseInt(next.get("num_likes").toString());

            }
        }catch(Exception ex){
            Logger.error("Unable to reach MongoDB");
            return -1;
        }

        return ret;
    }

    private static int getNumDislikes(int id){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Bson projection = (fields( excludeId()));
        Bson match = eq("id",id);
        int ret = 0;

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                ret = (next.get("num_dislikes") == null) ? 0 :Integer.parseInt(next.get("num_dislikes").toString());

            }
        }catch(Exception ex){
            Logger.error("Unable to reach MongoDB");
            return -1;
        }

        return ret;
    }

    private static ArticleBean fillArticleFields (Document next, boolean unwind){
        ArticleBean a = new ArticleBean();
        a.setId(Integer.parseInt((next.get("id") == null) ? "0" : (next.get("id").toString())));
        a.setAuthor(next.get("author").toString());
        a.setTitle(next.get("title").toString());
        Timestamp t = convertStringToTimestamp(next.get("timestamp").toString());
        a.setTimestamp(t);
        a.setText((next.get("body") == null) ? "" : (next.get("body").toString()));
        a.setNumberComments(next.get("num_comments")==null ? 0: Integer.parseInt(next.get("num_comments").toString()));
        a.setNumberLikes(next.get("num_likes")==null ? 0: Integer.parseInt(next.get("num_likes").toString()));
        a.setNumberDislikes(next.get("num_dislikes")==null ? 0: Integer.parseInt(next.get("num_dislikes").toString()));
        if (unwind){
            List<String> list = new ArrayList<>();
            list.add(next.get("games").toString());
        }else{
            List<String> list = (List<String>) next.get("games");
            a.setListGame(list);
        }

        return a;
    }

  /**
   * Convert string to timestamp timestamp.
   *
   * @param time the time
   * @return the timestamp
   */
  protected static Timestamp convertStringToTimestamp(String time) {
        Timestamp timestamp = null;
        try {
            //Date format from Scraping
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
            Date parsedDate = dateFormat.parse(time);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
        } catch(Exception e) { //this generic but you can control another types of exception
            //When the format is different from the scraping one we will use this one
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = null;
            try {
                parsedDate = dateFormat.parse(time);
            } catch (ParseException parseException) {
                Logger.error(parseException.getMessage());
            }
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
        }
        return timestamp;
    }

  /**
   * Add article int.
   *
   * @param a the a
   * @return the int
   */
  public static int addArticle(ArticleBean a) {
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        List<String> games = a.getListGame();
        int id = getLastIdUsed();
        if (id==-1){
            Logger.error("Unable to reach MongoDB");
            return -1;
        }

        Document doc = new Document("id", id).append("author", a.getAuthor()).append("title", a.getTitle()).append("body", a.getText()).append("timestamp", a.getTimestamp().toString())
                .append("num_likes", a.getNumberLikes()).append("num_dislikes", a.getNumberDislike()).append("num_comments", a.getNumberComments())
                .append("games", games);

        try{
            InsertOneResult insert= collection.insertOne(doc);
            if (!insert.wasAcknowledged()){
                return -1;
            }

            return id + 1;
        }catch (Exception ex){
            Logger.error(ex.getMessage());

            return -1;
        }
    }

    private static int getLastIdUsed(){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");

        Bson sort = descending("id");
        int ret = 0;

        try(MongoCursor<Document> cursor = collection.find().sort(sort).limit(1).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                int old = (Integer.parseInt(next.get("id").toString()));
                ret = old+1;

            }

        }catch(Exception ex){
            Logger.error("unable to reach MongoDB");
            return -1;
        }
        return ret;

    }

  /**
   * Delete article boolean.
   *
   * @param id the id
   * @return the boolean
   */
  public static boolean deleteArticle(int id) {
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");

        DeleteResult dr = collection.deleteOne(eq("id", id));
        if (dr.getDeletedCount() == 0 || !dr.wasAcknowledged()){
            Logger.warning("problems on delete " + id);
            return false;
        }
        return true;
    }
}

