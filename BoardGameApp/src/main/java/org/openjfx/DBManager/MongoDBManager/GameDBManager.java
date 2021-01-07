package org.openjfx.DBManager.MongoDBManager;

import com.mongodb.*;
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

        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("name",game));

        InfoGame g = new InfoGame();

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(match,projection)).iterator()){

            while(cursor.hasNext()){
                // controlli su valori null
                Document next = cursor.next();
                g = fillInfoGameFields(next,false);

            }
            cursor.close();
        }

        return g;

    }

    public static List<InfoGame> filterByName(String game){
        List<InfoGame> ret = new ArrayList<InfoGame>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");


        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("name",game));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(match, projection)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                //System.out.println(next.toJson());
                InfoGame g = fillInfoGameFields(next, false);
                ret.add(g);
            }
        }

        return ret;
    }

    public static List<InfoGame> filterByCategory(String category){
        List<InfoGame> ret = new ArrayList<InfoGame>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson unwind = unwind("$category");
        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("category",category));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind, match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                InfoGame g = fillInfoGameFields(next, true);
                ret.add(g);
            }
        }

        return ret;
    }

    public static List<InfoGame> filterByPlayers(int players){
        List<InfoGame> ret = new ArrayList<InfoGame>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson projection = project(fields( excludeId()));
        Bson match =  match(and(gte("max_players",players),lte("min_players", players)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                InfoGame g = fillInfoGameFields(next, false);
                ret.add(g);
            }
        }

        return ret;
    }

    public static List<InfoGame> filterByYear(int year){
        List<InfoGame> ret = new ArrayList<InfoGame>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("year",year));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                InfoGame g = fillInfoGameFields(next, false);
                ret.add(g);
            }
        }

        return ret;
    }

    public static List<InfoGame> orderBy (String mode){
        List<InfoGame> ret = new ArrayList<InfoGame>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson projection = project(fields( excludeId()));
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
                //System.out.println(cursor.next().toJson());
                
                Document next = cursor.next();
                InfoGame g = fillInfoGameFields(next, false);
                ret.add(g);
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

    private static InfoGame fillInfoGameFields (Document next, boolean unwindCategory){
        InfoGame g = new InfoGame();
        g.setName((next.get("name") == null) ? "" :next.get("name").toString());
        g.setAlternativeName((next.get("alt_name") == null) ? "" :next.get("alt_name").toString());
        g.setYear((next.get("year") == null) ? 0 :Integer.parseInt(next.get("year").toString()));
        g.setDescription((next.get("description") == null) ? "" :(next.get("description").toString()));
        g.setPublisher((next.get("publisher") == null) ? "" :next.get("publisher").toString());
        g.setUrl((next.get("url") == null) ? "" : next.get("url").toString());
        g.setImageUrl((next.get("image_url") == null) ? "" : next.get("image_url").toString());
        g.setRules((next.get("rules") == null) ? "" : next.get("rules").toString());
        //g.setPrice((next.get("list_price") == null) ? 0.0 : Double.parseDouble(next.get("list_price").toString()));
        g.setMinPlayers((next.get("min_players") == null) ? 1 :Integer.parseInt(next.get("min_players").toString()));
        g.setMaxPlayers((next.get("max_players") == null) ? 1000 : Integer.parseInt(next.get("max_players").toString()));
        g.setMinAge((next.get("min_age") == null) ? 3 :Integer.parseInt(next.get("min_age").toString()));
        g.setMaxAge((next.get("max_age") == null) ? 99 : Integer.parseInt(next.get("max_age").toString()));
        g.setMinTime((next.get("min_time") == null) ? "" :next.get("min_time").toString());
        g.setMaxTime((next.get("max_time") == null) ? "" : next.get("max_time").toString());
        g.setCooperative(next.get("cooperative") != null && Boolean.parseBoolean(next.get("cooperative").toString()));
        g.setFamily((next.get("family") == null) ? "" : next.get("family").toString());
        g.setExpansion((next.get("expansion") == null) ? "" : next.get("expansion").toString());
        g.setNumVotes((next.get("num_votes") == null) ? 0 : Integer.parseInt(next.get("num_votes").toString()));
        g.setAvgRating((next.get("avg_rating") == null) ? 0.0: Double.parseDouble(next.get("avg_rating").toString()));
        g.setNumReviews((next.get("num_reviews") == null) ? 0 :Integer.parseInt(next.get("num_reviews").toString()));
        g.setComplexity((next.get("complexity") == null) ? 0.0 :Double.parseDouble(next.get("complexity").toString()));
        if (unwindCategory){
            g.setCategory1(next.get("category")==null? "": next.get("category").toString());
            g.setCategory2("");

        }
        else {
            List<String> list = (List<String>) next.get("category");
            g.setCategory1("");
            g.setCategory2("");
            if(list == null){
                g.setCategory1("");
                g.setCategory2("");
            }else{
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        g.setCategory1((list.get(i)));
                    } else g.setCategory2((list.get(i)));
                }
            }

        }

        return g;
    }


}
