package org.openjfx.DBManager.MongoDBManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.openjfx.Entities.*;

import java.sql.*;
import java.util.Arrays;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;

public class GameDBManager {
    public static InfoGame readGame(String game){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        Bson unwind = unwind("$game_type");
        Bson projection = project(fields( excludeId(), include("name", "game_type", "publisher", "url", "image_url", "rules", "min_players", "max_player",
                "min_age", "max_age", "min_time", "max_time", "num_reviews", "avg_rating", "year")));
        Bson match =  match(and(eq("name",game)));

        InfoGame g = new InfoGame();

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()){
            System.out.println("Dentro");
            while(cursor.hasNext()){
                Document next = cursor.next();
                //System.out.println(next.toJson());
                g.setAvgRating(Double.parseDouble(next.get("avg_rating").toString()));
                g.setImageUrl(next.get("image_url").toString());
                g.setMaxAge(Integer.parseInt(next.get("max_age").toString()));
                g.setMaxPlayers(Integer.parseInt(next.get("max_player").toString()));
                g.setMaxTime(next.get("max_time").toString());
                g.setMinAge(Integer.parseInt(next.get("min_age").toString()));
                g.setMinPlayers(Integer.parseInt(next.get("min_players").toString()));
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
}
