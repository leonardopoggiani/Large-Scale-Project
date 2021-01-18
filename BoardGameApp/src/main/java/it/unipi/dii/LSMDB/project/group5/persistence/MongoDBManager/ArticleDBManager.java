package it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import it.unipi.dii.LSMDB.project.group5.bean.*;

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

public class ArticleDBManager {

    public static ArticleBean readArticle(int id){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Bson projection = (fields(excludeId()));
        Bson match =  (eq("id",id));

        ArticleBean a = new ArticleBean();

       try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                //System.out.println(next.toJson());
                a = fillArticleFields(next, false);
            }
            cursor.close();
       }


       return a;

    }

    public static List<ArticleBean> filterByInfluencer(String influencer){
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");

        Bson projection = (fields( excludeId()));
        Bson match =  (eq("author",influencer));

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()) {

            while (cursor.hasNext()) {
                Document next = (Document)cursor.next();
                //System.out.println(next.toJson());
                ArticleBean a = fillArticleFields(next,false);
                ret.add(a);
            }
        }

        return ret;
    }

    public static List<ArticleBean> filterByGame(String game){
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");

        Bson unwind1 = unwind("$games");
        Bson projection = project(fields( excludeId()));
        Bson match =  match(and(eq("games",game)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind1,match,projection)).iterator()) {


            while (cursor.hasNext()) {
                Document next = (Document) cursor.next();
                //System.out.println(next.toJson());
                ArticleBean a = fillArticleFields(next,true);
                ret.add(a);
            }
        }

        return ret;
    }

    public static List<ArticleBean> filterByDate(String date){
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");


        Bson projection = (fields( excludeId()));
        Bson match =  (gte("timestamp",date));

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                //System.out.println(next.toJson());
                ArticleBean a = fillArticleFields(next,false);
                ret.add(a);

            }
        }

        return ret;
    }

    public static List<ArticleBean> orderBy (String mode){
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");


        Bson projection = (fields( excludeId()));
        Bson sort = null;
        Bson match = null;
        if(mode.equals("like")){
            match = (and(ne("num_like", null), ne("num_like", "")));
            sort = (descending("num_like"));
        } else if (mode.equals("dislike")){
            match = (and(ne("num_dislike", null), ne("num_dislike", "")));
            sort = (descending("num_dislike"));
        } else {
            match = (and(ne("num_comments", null), ne("num_comments", "")));
            sort = (descending("num_comments"));
        }
        //try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( unwind, match, sort, limit, projection)).iterator()) {
        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).sort(sort).limit(6).iterator()) {

        while (cursor.hasNext()) {
                Document next = cursor.next();
                //System.out.println(next.toJson());
                ArticleBean g = fillArticleFields(next,false);
                ret.add(g);
            }
        }

        return ret;

    }

    public static boolean updateNumLike(int inc, int id){
        int tot = getNumLikes(id) + inc;
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Document updateLike = new Document();
        updateLike.append("num_like", tot);
        Document update = new Document();
        update.append("$set", updateLike);
        Document query = new Document();
        query.append("id", id);
        try{
            collection.updateOne(query, update);

            return true;
        }
        catch (Exception ex) {

            return false;
        }

    }

    public static boolean updateNumDislike(int inc, int id){
        int tot = getNumDislikes(id) + inc;
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Document updateLike = new Document();
        updateLike.append("num_dislike", tot);
        Document update = new Document();
        update.append("$set", updateLike);
        Document query = new Document();
        query.append("id", id);

        try{
            collection.updateOne(query, update);

            return true;
        }
        catch (Exception ex) {

            return false;
        }
    }

    public static boolean updateNumComments(int inc , int id){
        int tot = getNumComments(id) + inc;
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Document updateLike = new Document();
        updateLike.append("num_comments", tot);
        Document update = new Document();
        update.append("$set", updateLike);
        Document query = new Document();
        query.append("id", id);
        try{
            collection.updateOne(query, update);

            return true;
        }
        catch (Exception ex) {

            return false;
        }
    }

    public static int getNumComments(int id){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Bson projection = (fields( excludeId()));
        Bson match =  (eq("id",id));
        int ret = 0;

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                //System.out.println(next.toJson());
                ret = (next.get("num_comments") == null) ? 0 :Integer.parseInt(next.get("num_comments").toString());

            }
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
                //System.out.println(next.toJson());
                ret = (next.get("num_like") == null) ? 0 :Integer.parseInt(next.get("num_like").toString());

            }
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
                //System.out.println(next.toJson());
                Document articles = (Document) next.get("articles");
                ret = (next.get("num_dislike") == null) ? 0 :Integer.parseInt(articles.get("num_dislike").toString());

            }
        }catch(Exception ex){
            return -1;
        }

        return ret;
    }

    private static ArticleBean fillArticleFields (Document next, boolean unwind){
        ArticleBean a = new ArticleBean();
        a.setId(Integer.parseInt((next.get("id") == null) ? "0" : (next.get("id").toString())));
        a.setAuthor(next.get("author").toString());
        a.setTitle(next.get("title").toString());
        //System.out.println("Titolo " + next.get("title").toString());
        Timestamp t = convertStringToTimestamp(next.get("timestamp").toString());
        a.setTimestamp(t);
        a.setText((next.get("body") == null) ? "" : (next.get("body").toString()));
        a.setNumberComments(next.get("num_comments")==null ? 0: Integer.parseInt(next.get("num_comments").toString()));
        a.setNumberLikes(next.get("num_like")==null ? 0: Integer.parseInt(next.get("num_like").toString()));
        a.setNumberDislikes(next.get("num_dislike")==null ? 0: Integer.parseInt(next.get("num_dislike").toString()));
        if (unwind){
            List<String> list = new ArrayList<>();
            list.add(next.get("games").toString());
        }else{
            List<String> list = (List<String>) next.get("games");
            a.setListGame(list);
        }

        return a;
    }

    public static Timestamp convertStringToTimestamp(String time){
        Timestamp timestamp = null;
        try {
            //Date format from Scraping
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
            Date parsedDate = dateFormat.parse(time);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
            System.out.println(timestamp);
        } catch(Exception e) { //this generic but you can control another types of exception
            //When the format is different from the scraping one we will use this one
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = null;
            try {
                parsedDate = dateFormat.parse(time);
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
            System.out.println(timestamp);
        }
        return timestamp;
    }

    public static int addArticle (ArticleBean a){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        List<String> games = a.getListGame();
        int id = getLastIdUsed();
        if (id==-1){
            System.err.println("Unable to reach MongoDB");
            return -1;
        }


        System.out.println("add " + a);
        Document doc = new Document("id", id).append("author", a.getAuthor()).append("title", a.getTitle()).append("body", a.getText()).append("timestamp", a.getTimestamp().toString())
                .append("num_likes", a.getNumberLikes()).append("num_dislikes", a.getNumberDislike()).append("num_comments", a.getNumberComments())
                .append("games", games);

        try{
            collection.insertOne(doc);

            return id;
        }catch (Exception ex){
            System.err.println(ex.getMessage());

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
                //System.out.println(next.toJson());
                ret = (Integer.parseInt(next.get("id").toString()));

            }
        }catch(Exception ex){
            System.err.println("Unable to reach MongoDB");
            return -1;
        }

        return ret;

    }

    public static boolean deleteArticle (int id){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");

        DeleteResult dr = collection.deleteOne(eq("id", id));
        if (dr.getDeletedCount() == 0 || !dr.wasAcknowledged()){
            return false;
        }
        return true;
    }
}

