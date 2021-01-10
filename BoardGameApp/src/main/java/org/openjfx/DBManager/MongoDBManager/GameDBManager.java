package org.openjfx.DBManager.MongoDBManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.openjfx.Entities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class GameDBManager {
    public static GameBean readGame(String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("name",game));

        GameBean g = new GameBean();

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

    public static List<GameBean> filterByName(String game){
        List<GameBean> ret = new ArrayList<GameBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");


        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("name",game));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(match, projection)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                //System.out.println(next.toJson());
                GameBean g = fillInfoGameFields(next, false);
                ret.add(g);
            }
        }

        return ret;
    }

    public static List<GameBean> filterByCategory(String category){
        List<GameBean> ret = new ArrayList<GameBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson unwind = unwind("$category");
        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("category",category));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind, match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                GameBean g = fillInfoGameFields(next, true);
                ret.add(g);
            }
        }

        return ret;
    }

    public static List<GameBean> filterByPlayers(int players){
        List<GameBean> ret = new ArrayList<GameBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson projection = project(fields( excludeId()));
        Bson match =  match(and(gte("max_players",players),lte("min_players", players)));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                GameBean g = fillInfoGameFields(next, false);
                ret.add(g);
            }
        }

        return ret;
    }

    public static List<GameBean> filterByYear(int year){
        List<GameBean> ret = new ArrayList<GameBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson projection = project(fields( excludeId()));
        Bson match =  match(eq("year",year));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                GameBean g = fillInfoGameFields(next, false);
                ret.add(g);
            }
        }

        return ret;
    }

    public static List<GameBean> orderBy (String mode){
        List<GameBean> ret = new ArrayList<GameBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson projection = project(fields( excludeId()));
        Bson limit = limit(10);
        Bson sort = null;
        Bson match = null;
        if(mode.equals("reviews")){
            match = match(and(ne("num_reviews", null), ne("num_reviews", "")));
            sort = sort(descending("num_reviews"));
        } else if (mode.equals("numVotes")){
            match = match(and(ne("num_votes", null), ne("num_votes", "")));
            sort = sort(descending("num_votes"));
        } else {
            match = match(and(ne("avg_rating", null), ne("avg_rating", "")));
            sort = sort(descending("avg_rating"));
        }
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, sort, limit, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());

                Document next = cursor.next();
                GameBean g = fillInfoGameFields(next, false);
                System.out.println(next.toJson());
                ret.add(g);
            }
        }

        return ret;

    }

    public static double updateRating(double rate, String game){
        int votes = getNumVotes(game);
        double avg = getAvgRating(game);
        double newAvg = (avg*votes + rate)/(votes+1);
        updateAvgRating(newAvg, game);
        updateNumVotes(votes+1, game);
        return getAvgRating(game);
    }

    private static void updateAvgRating (double avg,  String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Document updateAvg = new Document();
        updateAvg.append("avg_rating", avg);
        Document update = new Document();
        update.append("$set", updateAvg);
        collection.updateOne(eq("name", game), update);

    }

    public static void updateNumReviews(int inc, String game){
        int tot = getNumReviews(game) + inc;
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Document reviews = new Document();
        reviews.append("num_reviews", tot);
        Document update = new Document();
        update.append("$set", reviews);
        collection.updateOne(eq("name", game), update);

    }

    public static int getNumReviews(String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson projection = project(fields( excludeId(), include("name", "num_reviews")));
        Bson match =  match(eq("name",game));
        int ret = 0;
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                ret = (next.get("num_reviews") == null) ? 0 : Integer.parseInt(next.get("num_reviews").toString());
            }
        }

        return ret;
    }

    private static void updateNumVotes(int tot, String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Document votes = new Document();
        votes.append("num_votes", tot);
        Document update = new Document();
        update.append("$set", votes);
        collection.updateOne(eq("name", game), update);
    }

    public static double getAvgRating(String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson projection = project(fields( excludeId(), include("name", "avg_rating")));
        Bson match =  match(eq("name",game));
        double ret = 0.0;
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                ret = (next.get("avg_rating") == null) ? 0.0 : Double.parseDouble(next.get("avg_rating").toString());
            }
        }

        return ret;
    }

    public static int getNumVotes (String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson projection = project(fields( excludeId(), include("name", "num_votes")));
        Bson match =  match(eq("name",game));
        int ret = 0;
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                ret = (next.get("num_votes") == null) ? 0 : Integer.parseInt(next.get("num_votes").toString());
            }
        }

        return ret;

    }

    protected static GameBean fillInfoGameFields(Document next, boolean unwindCategory){
        GameBean g = new GameBean();
        g.setName((next.get("name") == null) ? "" :next.get("name").toString());
        g.setAlternativeName((next.get("alt_name") == null) ? "" :next.get("alt_name").toString());
        g.setYear((next.get("year") == null) ? 0 :Integer.parseInt(next.get("year").toString()));

        g.setDescription((next.get("description") == null) ? "" :(next.get("description").toString()));

        List<String> publisher = (List<String>) next.get("publisher");
        g.setPublisher(publisher);
        g.setUrl((next.get("url") == null) ? "" : next.get("url").toString());

        // gli url delle immagini vengono attraverso un array, prendo la prima immagini
        List<String> image_url = (List<String>) next.get("image_url");
        g.setImageUrl((next.get("image_url") == null) ? "" : image_url.get(0).toString());
        g.setLanguageDependency((next.get("language_dependency")==null)? 0: Integer.parseInt(next.get("language_dependency").toString()));


        g.setPrice((next.get("list_price") == null) ? "Not specified" :(next.get("list_price").toString()));
        g.setMinPlayers((next.get("min_players") == null) ? 1 :Integer.parseInt(next.get("min_players").toString()));
        g.setMaxPlayers((next.get("max_players") == null) ? 1000 : Integer.parseInt(next.get("max_players").toString()));
        g.setMinAge((next.get("min_age") == null) ? 3 :Integer.parseInt(next.get("min_age").toString()));
        g.setMaxAge((next.get("max_age") == null) ? 99 : Integer.parseInt(next.get("max_age").toString()));
        g.setMinTime((next.get("min_time") == null) ? "There's no limit!" :next.get("min_time").toString());
        g.setMaxTime((next.get("max_time") == null) ? "There's no limit!" : next.get("max_time").toString());
        g.setCooperative(next.get("cooperative") != null && Boolean.parseBoolean(next.get("cooperative").toString()));
        List<String> family = (List<String>) next.get("family");
        g.setFamily(family);
        List<String> expansion = (List<String>) next.get("expansion");
        g.setExpansion(expansion);
        g.setNumVotes((next.get("num_votes") == null) ? 0 : Integer.parseInt(next.get("num_votes").toString()));
        g.setAvgRating((next.get("avg_rating") == null) ? 0.0: Double.parseDouble(next.get("avg_rating").toString()));
        g.setNumReviews((next.get("num_reviews") == null) ? 0 : Integer.parseInt(next.get("num_reviews").toString()));
        g.setComplexity((next.get("complexity") == null) ? 0.0 : Double.parseDouble(next.get("complexity").toString()));

        g.setRules("Regole: \n" + "-minimum age: " + g.getMinAge() + "\n -maximum age: " + g.getMaxAge() + "\n" +
                "-minimum time: " + g.getMinTime() + "\n -maximum time: " + g.getMaxTime() +
                "\n -complexity: " + g.getComplexity() + "\n -minimum players: " + g.getMinPlayers() +
                "\n -cooperative: " + ((g.isCooperative()) ? "Yes! Play all togheter!" : "No, all against all!") +
                "\n -family and expansion: " + g.getFamily() + ", " + g.getExpansion());

        if (unwindCategory){
            g.setCategory1(next.get("category")==null? "": next.get("category").toString());
            g.setCategory2("");

        }
        else {
            List<String> list = (List<String>) next.get("category");
            System.out.println(list);
            g.setListCategory(list);

        }

        return g;
    }


}
