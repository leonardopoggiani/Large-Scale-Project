package org.openjfx.DBManager.MongoDBManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.openjfx.Entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class GameDBManager {
    public static InfoGame readGame(String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson unwind = unwind("$game_type");
        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("name",game));

        InfoGame g = new InfoGame();

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                 System.out.println(next.toJson());
                g.setAvgRating(Double.parseDouble(next.get("avg_rating").toString()));
                g.setImageUrl(next.get("image_url").toString());
                g.setMaxAge(Integer.parseInt(next.get("max_age").toString()));
                g.setMaxPlayers(Integer.parseInt(next.get("max_player").toString()));
                g.setMaxTime(next.get("max_time").toString());
                g.setMinAge(Integer.parseInt(next.get("min_age").toString()));
                g.setMinPlayers(Integer.parseInt(next.get("min_player").toString()));
                g.setMinTime(next.get("min_time").toString());
                g.setPublisher(next.get("publisher").toString());
                g.setRules(next.get("rules").toString());
                g.setUrl(next.get("url").toString());
                g.setYear(Integer.parseInt(next.get("year").toString()));
                g.setNumReviews(Integer.parseInt(next.get("num_reviews").toString()));
                g.setName(next.get("name").toString());
                g.setAvgRating(Double.parseDouble(next.get("avg_rating").toString()));
                g.setCategory1(next.get("game_type").toString());
                Document d = cursor.next();
                g.setCategory2(d.get("game_type").toString());




            }
            cursor.close();
        }

        return g;

    }

    public static List<Document> filterByName(String game){
        List<Document> ret = new ArrayList<Document>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");


        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("name",game));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                ret.add(cursor.next());
            }
        }

        return ret;
    }

    public static List<Document> filterByCategory(String category){
        List<Document> ret = new ArrayList<Document>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson unwind = unwind("$game_type");
        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("game_type",category));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind, match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                ret.add(cursor.next());
            }
        }

        return ret;
    }

    public static List<Document> filterByPlayers(int players){
        List<Document> ret = new ArrayList<Document>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson projection = project(fields( excludeId()));
        Bson match =  match(and(gte("max_player",players),lte("min_player", players)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                ret.add(cursor.next());
            }
        }

        return ret;
    }

    public static List<Document> filterByYear(int year){
        List<Document> ret = new ArrayList<Document>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("year",year));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                ret.add(cursor.next());
            }
        }

        return ret;
    }

    public static List<Document> orderBy (String mode){
        List<Document> ret = new ArrayList<Document>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson projection = project(fields( excludeId(), include("name", "num_reviews")));
        Bson limit = limit(10);
        Bson sort = null;
        Bson match = null;
        if(mode.equals("reviews")){
            match = match(and(ne("num_reviews", null), ne("num_reviews", "")));
            sort = sort(descending("num_reviews"));
        }else {
            match = match(and(ne("avg_rating", null), ne("avg_rating", "")));
            sort = sort(descending("avg_rating"));
        }
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, sort, limit, projection)).iterator()) {

            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
                //ret.add(cursor.next());
            }
        }

        return ret;

    }

    public static void updateAvgRating(double avg, String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Document updateAvg = new Document();
        updateAvg.append("avg_rating", avg);
        Document update = new Document();
        update.append("$set", updateAvg);
        collection.updateOne(eq("name", game), update);

    }

    public static void updateNumReviews(double tot, String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Document reviews = new Document();
        reviews.append("num_reviews", tot);
        Document update = new Document();
        update.append("$set", reviews);
        collection.updateOne(eq("name", game), update);

    }

}
