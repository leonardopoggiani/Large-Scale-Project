package it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class GameDBManager {
    public static GameBean readGame(String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson projection = (fields( excludeId()));
        Bson match =  (eq("name",game));

        GameBean g = new GameBean();


        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){
            while(cursor.hasNext()){

                //System.out.println(cursor.next().toJson());
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


        Bson projection = (fields( excludeId()));
        Bson match =  (eq("name",game));

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).limit(6).iterator()){

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
        Bson limit = limit(6);

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind, match, projection, limit)).iterator()) {

            while (cursor.hasNext()) {
                // System.out.println(cursor.next().toJson());
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

        Bson projection = (fields( excludeId()));
        Bson match =  (and(gte("max_players",players),lte("min_players", players)));

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).limit(6).iterator()){
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

        Bson projection = (fields( excludeId()));
        Bson match =  (eq("year",year));

        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).limit(6).iterator()){

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

        Bson projection = (fields( excludeId()));
        Bson sort = null;
        Bson match = null;
        if(mode.equals("reviews")){
            match = (and(ne("num_reviews", null), ne("num_reviews", "")));
            sort = (descending("num_reviews"));
        } else if (mode.equals("numVotes")){
            match = (and(ne("num_votes", null), ne("num_votes", "")));
            sort = (descending("num_votes"));
        } else {
            match = (and(ne("avg_rating", null), ne("avg_rating", "")));
            sort = (descending("avg_rating"));
        }
        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).sort(sort).limit(6).iterator()){

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

    public static boolean updateRating(double rate, String game){
        int votes = getNumVotes(game);
        double avg = getAvgRating(game);
        double newAvg = (avg*votes + rate)/(votes+1);
        if(!updateNumVotes(votes+1, game)){
            return false;
        };
        if(!updateAvgRating(newAvg, game)){
            updateNumVotes(votes, game);
            return false;
        }
        return true;
    }

    private static boolean updateAvgRating (double avg,  String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Document updateAvg = new Document();
        updateAvg.append("avg_rating", avg);
        Document update = new Document();
        update.append("$set", updateAvg);
        try{
            collection.updateOne(eq("name", game), update);

            return true;
        }
        catch (Exception ex){

            return false;
        }

    }

    public static boolean updateNumReviews(int inc, String game){
        int tot = getNumReviews(game) + inc;
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Document reviews = new Document();
        reviews.append("num_reviews", tot);
        Document update = new Document();
        update.append("$set", reviews);
        try{
            collection.updateOne(eq("name", game), update);

            return true;
        }
        catch (Exception ex){

            return false;
        }

    }

    public static int getNumReviews(String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson projection = (fields( excludeId(), include("name", "num_reviews")));
        Bson match =  (eq("name",game));
        int ret = 0;
        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                ret = (next.get("num_reviews") == null) ? 0 : Integer.parseInt(next.get("num_reviews").toString());
            }
        }
        return ret;
    }

    private static boolean updateNumVotes(int tot, String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Document votes = new Document();
        votes.append("num_votes", tot);
        Document update = new Document();
        update.append("$set", votes);
        try{
            collection.updateOne(eq("name", game), update);

            return true;
        }
        catch (Exception ex){

            return false;
        }

    }

    public static double getAvgRating(String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson projection = (fields( excludeId(), include("name", "avg_rating")));
        Bson match =  (eq("name",game));
        double ret = 0.0;
        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                ret = (next.get("avg_rating") == null || next.get("avg_rating").equals("nan")) ? 0.0 : Double.parseDouble(next.get("avg_rating").toString());
            }
        }

        return ret;
    }

    public static boolean doesGameExists (String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson match =  (eq("name",game));
        boolean ret = false;
        try(MongoCursor<Document> cursor = collection.find(match).iterator()){

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                ret = true;
            }
        }
        return ret;
    }
    
    public static int getNumVotes (String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson projection = (fields( excludeId(), include("name", "num_votes")));
        Bson match =  (eq("name",game));
        int ret = 0;
        try(MongoCursor<Document> cursor = collection.find(match).projection(projection).iterator()){

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
        g.setYear((next.get("year") == null) ? 0 :Integer.parseInt(next.get("year").toString()));

        g.setDescription((next.get("description") == null) ? "" :(next.get("description").toString()));

        List<String> publisher = (List<String>) next.get("publisher");
        g.setPublisher(publisher);
        g.setUrl((next.get("url") == null) ? "" : next.get("url").toString());

        // gli url delle immagini vengono attraverso un array, prendo la prima immagini
        List<String> image_url = (List<String>) next.get("image_url");
        g.setImageUrl((next.get("image_url") == null) ? "" : image_url.get(0).toString());
        g.setMinPlayers((next.get("min_players") == null) ? 1 :Integer.parseInt(next.get("min_players").toString()));
        g.setMaxPlayers((next.get("max_players") == null) ? 1000 : Integer.parseInt(next.get("max_players").toString()));
        g.setMinAge((next.get("min_age") == null) ? 3 :Integer.parseInt(next.get("min_age").toString()));
        g.setMaxAge((next.get("max_age") == null) ? 99 : Integer.parseInt(next.get("max_age").toString()));
        g.setMinTime((next.get("min_time") == null) ? "There's no limit!" :next.get("min_time").toString());
        g.setMaxTime((next.get("max_time") == null) ? "There's no limit!" : next.get("max_time").toString());
        g.setCooperative(next.get("cooperative") != null && Boolean.parseBoolean(next.get("cooperative").toString()));
        List<String> expansion = (List<String>) next.get("expansion");
        g.setExpansion(expansion);
        g.setNumVotes((next.get("num_votes") == null) ? 0 : Integer.parseInt(next.get("num_votes").toString()));
        g.setAvgRating((next.get("avg_rating") == null || next.get("avg_rating").equals("nan") ) ? 0.0: Double.parseDouble(next.get("avg_rating").toString()));
        g.setNumReviews((next.get("num_reviews") == null) ? 0 : Integer.parseInt(next.get("num_reviews").toString()));
        g.setComplexity((next.get("complexity") == null) ? 0.0 : Double.parseDouble(next.get("complexity").toString()));

        StringBuilder regole = new StringBuilder("Regole: \n");
        regole.append("-minimum age: " + g.getMinAge() + "\n");
        regole.append("-maximum age: " + g.getMaxAge() + "\n");
        regole.append("-minimum time: " + g.getMinTime() + "\n");
        regole.append("-maximum time: " + g.getMaxTime() + "\n");
        regole.append("-complexity: " + g.getComplexity() + "\n");
        regole.append("-minimum players: " + g.getMinPlayers() + "\n");
        regole.append("-maximum players: " + g.getMaxPlayers() + "\n");
        regole.append("-cooperative: " + (g.isCooperative() ? "Yes! Play all togheter!" : "No, all against all!") + "\n");
        regole.append("-expansion: " +  g.getExpansion() + "\n");
        regole.append("-year: " +  g.getYear() + "\n");

        g.setRules(regole.toString());

        if (unwindCategory){
            g.setCategory1(next.get("category")==null? "": next.get("category").toString());
            g.setCategory2("");

        }
        else {
            List<String> list = (List<String>) next.get("category");
            g.setListCategory(list);
        }

        return g;
    }

    public static boolean addGame (GameBean g){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        List<String> categories = g.getListCategory();

        //System.out.println("add " + a);
        Document doc = new Document("name", g.getName()).append("year", g.getYear()).append("category", categories ).append("description", g.getDescription())
                .append("publisher", g.getPublisher()).append("url", g.getUrl()).append("image_url", g.getImageUrl())
                .append("rules_url", g.getRules()).append("min_players", g.getMinPlayers()).append("max_players", g.getMaxPlayers())
                .append("min_age", g.getMinAge()).append("max_age", g.getMaxAge()).append("min_time", g.getMinTime()).append("max_time", g.getMaxTime())
                .append("cooperative", g.isCooperative()).append("expansion", g.getExpansion()).append("num_votes", g.getNumVotes())
                .append("avg_rating", g.getAvgRating()).append("num_reviews", g.getNumReviews()).append("complexity", g.getComplexity());

        try{
            collection.insertOne(doc);

            return true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());

            return false;
        }
    }

    public static boolean deleteGame (String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        DeleteResult dr = collection.deleteOne(eq("name", game));
        if (dr.getDeletedCount() == 0 || !dr.wasAcknowledged()){
            return false;
        }
        return true;
    }


}
