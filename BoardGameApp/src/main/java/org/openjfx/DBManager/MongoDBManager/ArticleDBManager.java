package org.openjfx.DBManager.MongoDBManager;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.openjfx.Entities.Article;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class ArticleDBManager {

    public static Article readArticle(String user, String title){
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");

        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("username",user), eq("articles.title", title)));

        Article a = new Article();

       try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                //System.out.println(next.toJson());
                a.setAuthor(next.get("username").toString());
                Document article = (Document)next.get("articles");
                a.setTitle(article.get("title").toString());
                Timestamp t = convertStringToTimestamp(article.get("timestamp").toString());
                a.setTimestamp(t);
                a.setText(article.get("body").toString());
            }
            cursor.close();
       }

       return a;

    }

    public static List<Article> filterByInfluencer(String influencer){
        List<Article> ret = new ArrayList<Article>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");

        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("username",influencer)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()) {

            while (cursor.hasNext()) {
                Article a = new Article();
                Document next = (Document)cursor.next().get("articles");
                //System.out.println(next.toJson());
                a.setAuthor(influencer);
                a.setTitle(next.get("title").toString());
                a.setText(next.get("body").toString());
                String timestamp = next.get("timestamp").toString();
                //System.out.println(timestamp);
                Timestamp t = convertStringToTimestamp(timestamp);
                a.setTimestamp(t);
                ret.add(a);
            }
        }

        return ret;
    }

    public static List<Article> filterByGame(String game){
        List<Article> ret = new ArrayList<Article>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");

        Bson unwind = unwind("$articles");
        Bson unwind1 = unwind("$articles.games");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("articles.games",game)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,unwind1,match,projection)).iterator()) {

            while (cursor.hasNext()) {
                Article a = new Article();
                Document next = (Document) cursor.next();
                //System.out.println(next.toJson());
                a.setAuthor(next.get("username").toString());
                Document article = (Document)next.get("articles");
                a.setText(article.get("body").toString());
                a.setTitle(article.get("title").toString());
                a.setTimestamp(convertStringToTimestamp(article.get("timestamp").toString()));


                ret.add(a);
            }
        }

        return ret;
    }

    public static List<Article> filterByDate(String date){
        List<Article> ret = new ArrayList<Article>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");

        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(gte("articles.timestamp",date)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()) {

            while (cursor.hasNext()) {
                Article a = new Article();
                Document next = cursor.next();
                //System.out.println(next.toJson());
                a.setAuthor(next.get("username").toString());
                Document article = (Document)next.get("articles");
                a.setTitle(article.get("title").toString());
                a.setText(article.get("body").toString());
                a.setTimestamp(convertStringToTimestamp(article.get("timestamp").toString()));
                ret.add(a);

            }
        }

        return ret;
    }

    private static Timestamp convertStringToTimestamp(String time){
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
}

