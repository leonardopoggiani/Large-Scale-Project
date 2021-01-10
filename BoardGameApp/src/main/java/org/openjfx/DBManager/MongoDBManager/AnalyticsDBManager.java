package org.openjfx.DBManager.MongoDBManager;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;


import org.openjfx.Entities.InfoGame;

import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.ascending;


public class AnalyticsDBManager {
    public List<InfoGame> showLeastRatedGames (String mode, String value){
        List<InfoGame> ret = new ArrayList<InfoGame>();

        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");

        if (mode.equals("category")){
            //ON CATEGORY
            Bson unwind = unwind("$category");
            Bson projection = project(fields( excludeId(), include("name", "num_rate")));
            Bson sort = ascending("num_votes");
            Bson match =  match(eq("category",value));
            Bson limit = limit(10);

        } else {
            //ON YEAR

        }


        return ret;
    }
}
