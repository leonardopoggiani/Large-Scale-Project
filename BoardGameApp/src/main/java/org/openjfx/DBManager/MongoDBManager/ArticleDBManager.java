package org.openjfx.DBManager.MongoDBManager;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.openjfx.Entities.Article;

import java.util.ArrayList;
import java.util.Arrays;
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
                //a.setTimestamp(article.get("timestamp").toString());
                a.setText(article.get("body").toString());
            }
            cursor.close();
       }

       return a;

    }

    public static List<Document> filterByInfluencer(String influencer){
        List<Document> ret = new ArrayList<Document>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");

        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("username",influencer)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()) {

            while (cursor.hasNext()) {
                ret.add(cursor.next());
            }
        }

        return ret;
    }

    public static List<Document> filterByGame(String game){
        List<Document> ret = new ArrayList<Document>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");

        Bson unwind = unwind("$articles");
        Bson unwind1 = unwind("$articles.games");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("articles.games",game)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,unwind1,match,projection)).iterator()) {

            while (cursor.hasNext()) {
                ret.add(cursor.next());
            }
        }

        return ret;
    }

    public static List<Document> filterByDate(String date){
        List<Document> ret = new ArrayList<Document>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");

        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(gte("articles.timestamp",date)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()) {

            while (cursor.hasNext()) {
                ret.add(cursor.next());

            }
        }

        return ret;
    }
}

