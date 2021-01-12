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

    public static ArticleBean readArticle(String user, String title){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");

        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("username",user), eq("articles.title", title)));

        ArticleBean a = new ArticleBean();

       try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                //System.out.println(next.toJson());
                a = fillArticleFields(next);
            }
            cursor.close();
       }

       MongoDBManager.dropCollection(collection);
       return a;

    }

    public static List<ArticleBean> filterByInfluencer(String influencer){
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");

        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("username",influencer)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()) {

            while (cursor.hasNext()) {
                Document next = (Document)cursor.next();
                //System.out.println(next.toJson());
                ArticleBean a = fillArticleFields(next);
                ret.add(a);
            }
        }
        MongoDBManager.dropCollection(collection);
        return ret;
    }

    public static List<ArticleBean> filterByGame(String game){
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");

        Bson unwind = unwind("$articles");
        Bson unwind1 = unwind("$articles.games");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("articles.games",game)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,unwind1,match,projection)).iterator()) {


            while (cursor.hasNext()) {
                Document next = (Document) cursor.next();
                //System.out.println(next.toJson());
                ArticleBean a = fillArticleFields(next);
                ret.add(a);
            }
        }
        MongoDBManager.dropCollection(collection);
        return ret;
    }

    public static List<ArticleBean> filterByDate(String date){
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");

        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(gte("articles.timestamp",date)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                //System.out.println(next.toJson());
                ArticleBean a = fillArticleFields(next);
                ret.add(a);

            }
        }
        MongoDBManager.dropCollection(collection);
        return ret;
    }

    public static List<ArticleBean> orderBy (String mode){
        List<ArticleBean> ret = new ArrayList<ArticleBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");

        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username","articles")));
        Bson limit = limit(10);
        Bson sort = null;
        Bson match = null;
        if(mode.equals("like")){
            match = match(and(ne("articles.num_like", null), ne("articles.num_like", "")));
            sort = sort(descending("num_like"));
        } else if (mode.equals("dislike")){
            match = match(and(ne("articles.num_dislike", null), ne("articles.num_dislike", "")));
            sort = sort(descending("articles.num_dislike"));
        } else {
            match = match(and(ne("articles.num_comments", null), ne("articles.num_comments", "")));
            sort = sort(descending("articles.num_comments"));
        }
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( unwind, match, sort, limit, projection)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                System.out.println(next.toJson());
                ArticleBean g = fillArticleFields(next);
                ret.add(g);
            }
        }
        MongoDBManager.dropCollection(collection);
        return ret;

    }

    public static boolean updateNumLike(int inc, String author, String title){
        int tot = getNumLikes(author, title) + inc;
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Document updateLike = new Document();
        updateLike.append("articles.$.num_like", tot);
        Document update = new Document();
        update.append("$set", updateLike);
        Document query = new Document();
        query.append("username", author);
        query.append("articles.title", title);
        try{
            collection.updateOne(query, update);
            MongoDBManager.dropCollection(collection);
            return true;
        }
        catch (Exception ex) {
            MongoDBManager.dropCollection(collection);
            return false;
        }

    }

    public static boolean updateNumDislike(int inc, String author, String title){
        int tot = getNumLikes(author, title) + inc;
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Document updateLike = new Document();
        updateLike.append("articles.$.num_dislike", tot);
        Document update = new Document();
        update.append("$set", updateLike);
        Document query = new Document();
        query.append("username", author);
        query.append("articles.title", title);
        try{
            collection.updateOne(query, update);
            MongoDBManager.dropCollection(collection);
            return true;
        }
        catch (Exception ex) {
            MongoDBManager.dropCollection(collection);
            return false;
        }
    }

    public static boolean updateNumComments(int inc , String author, String title){
        int tot = getNumComments(author, title) + inc;
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Document updateLike = new Document();
        updateLike.append("articles.$.num_comments", tot);
        Document update = new Document();
        update.append("$set", updateLike);
        Document query = new Document();
        query.append("username", author);
        query.append("articles.title", title);
        try{
            collection.updateOne(query, update);
            MongoDBManager.dropCollection(collection);
            return true;
        }
        catch (Exception ex) {
            MongoDBManager.dropCollection(collection);
            return false;
        }
    }

    public static int getNumComments(String author, String title){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("articles.title",title), eq("username", author)));
        int ret = 0;

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                //System.out.println(next.toJson());
                Document articles = (Document) next.get("articles");
                ret = (next.get("num_comments") == null) ? 0 :Integer.parseInt(articles.get("num_comments").toString());

            }
        }
        MongoDBManager.dropCollection(collection);
        return ret;
    }

    public static int getNumLikes(String author, String title){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("articles.title",title), eq("username", author)));
        int ret = 0;

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                //System.out.println(next.toJson());
                Document articles = (Document) next.get("articles");
                ret = (next.get("num_like") == null) ? 0 :Integer.parseInt(articles.get("num_like").toString());

            }
        }
        MongoDBManager.dropCollection(collection);
        return ret;
    }

    public static int getNumDislikes(String author, String title){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("articles.title",title), eq("username", author)));
        int ret = 0;

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                //System.out.println(next.toJson());
                Document articles = (Document) next.get("articles");
                ret = (next.get("num_dislike") == null) ? 0 :Integer.parseInt(articles.get("num_dislike").toString());

            }
        }
        MongoDBManager.dropCollection(collection);
        return ret;
    }

    private static ArticleBean fillArticleFields (Document next){
        ArticleBean a = new ArticleBean();
        a.setAuthor(next.get("username").toString());
        Document article = (Document)next.get("articles");
        a.setTitle(article.get("title").toString());
        System.out.println("Titolo " + article.get("title").toString());
        Timestamp t = convertStringToTimestamp(article.get("timestamp").toString());
        a.setTimestamp(t);
        a.setText(article.get("body").toString());
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
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Bson match = (eq("username", a.getAuthor()));
        List<String> games = a.getListGame();

        Document doc = new Document("title", a.getTitle()).append("body", a.getText()).append("timestamp", a.getTimestamp())
                .append("num_likes", a.getNumberLikes()).append("num_dislikes", a.getNumberDislike()).append("num_comments", a.getNumberComments())
                .append("games", games);

        Bson query = new Document("username", a.getAuthor());
        Bson update = new Document("$push", new Document("articles", doc));


        try{
            collection.findOneAndUpdate(query, update);
            MongoDBManager.dropCollection(collection);
            return true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            MongoDBManager.dropCollection(collection);
            return false;
        }
    }
}

