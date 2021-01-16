package it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import it.unipi.dii.LSMDB.project.group5.bean.*;

import java.sql.Timestamp;
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
                a = fillArticleFields(next);
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
                ArticleBean a = fillArticleFields(next);
                ret.add(a);
            }
        }

        return ret;
    }

    public static List<ArticleBean> filterByGame(String game){
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");

        Bson unwind1 = unwind("$articles.games");
        Bson projection = project(fields( excludeId()));
        Bson match =  match(and(eq("articles.games",game)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind1,match,projection)).iterator()) {


            while (cursor.hasNext()) {
                Document next = (Document) cursor.next();
                //System.out.println(next.toJson());
                ArticleBean a = fillArticleFields(next);
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
                ArticleBean a = fillArticleFields(next);
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
            match = (and(ne("articles.num_like", null), ne("articles.num_like", "")));
            sort = (descending("num_like"));
        } else if (mode.equals("dislike")){
            match = (and(ne("articles.num_dislike", null), ne("articles.num_dislike", "")));
            sort = (descending("articles.num_dislike"));
        } else {
            match = (and(ne("articles.num_comments", null), ne("articles.num_comments", "")));
            sort = (descending("articles.num_comments"));
        }
        //try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( unwind, match, sort, limit, projection)).iterator()) {
        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).sort(sort).limit(6).iterator()) {

        while (cursor.hasNext()) {
                Document next = cursor.next();
                System.out.println(next.toJson());
                ArticleBean g = fillArticleFields(next);
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
        }

        return ret;
    }

    private static ArticleBean fillArticleFields (Document next){
        ArticleBean a = new ArticleBean();
        a.setId(Integer.parseInt(next.get("id").toString()));
        a.setAuthor(next.get("author").toString());
        a.setTitle(next.get("title").toString());
        System.out.println("Titolo " + next.get("title").toString());
        Timestamp t = convertStringToTimestamp(next.get("timestamp").toString());
        a.setTimestamp(t);
        a.setText(next.get("body").toString());
        a.setNumberComments(next.get("num_comments")==null ? 0: Integer.parseInt(next.get("num_comments").toString()));
        a.setNumberLikes(next.get("num_like")==null ? 0: Integer.parseInt(next.get("num_like").toString()));
        a.setNumberDislikes(next.get("num_dislike")==null ? 0: Integer.parseInt(next.get("num_dislike").toString()));
        List<String> list = (List<String>) next.get("games");
        a.setListGame(list);
        return a;
    }

    protected static Timestamp convertStringToTimestamp(String time){
        Timestamp timestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(time);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
        } catch(Exception e) { //this generic but you can control another types of exception
            System.out.println(e.getMessage());
        }
        return timestamp;
    }

    public static boolean addArticle (ArticleBean a){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        List<String> games = a.getListGame();

        Document doc = new Document("author", a.getAuthor()).append("title", a.getTitle()).append("body", a.getText()).append("timestamp", "2019-11-20 00:00:00" /*a.getTimestamp().toString()*/)
                .append("num_likes", a.getNumberLikes()).append("num_dislikes", a.getNumberDislike()).append("num_comments", a.getNumberComments())
                .append("games", games);


        try{
            collection.insertOne(doc);

            return true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());

            return false;
        }
    }
}

