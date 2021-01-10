package org.openjfx.DBManager.MongoDBManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;


import org.openjfx.Entities.InfoGame;

import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.ascending;
import static org.openjfx.DBManager.MongoDBManager.GameDBManager.fillInfoGameFields;


public class AnalyticsDBManager {
    public List<InfoGame> showLeastRatedGames (String mode, String value){
        List<InfoGame> ret = new ArrayList<InfoGame>();

        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson unwind = null;
        Bson projection = null;
        Bson sort = null;
        Bson match =  null;
        Bson limit = null;

        if (mode.equals("category")){
            //ON CATEGORY
            unwind = unwind("$category");
            projection = project(fields( excludeId(), include("name", "num_votes")));
            sort = ascending("num_votes");
            match =  match(eq("category",value));
            limit = limit(10);

        } else {
            //ON YEAR
            projection = project(fields( excludeId(), include("name", "num_votes")));
            sort = ascending("num_votes");
            match =  match(eq("year",value));
            limit = limit(10);

        }

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, sort, limit, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());

                Document next = cursor.next();
                InfoGame g = fillInfoGameFields(next, false);
                System.out.println(next.toJson());
                ret.add(g);
            }
        }
        return ret;
    }

    /*public int getUsersFromCountry (String country){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson unwind = null;
        Bson projection = null;
        Bson sort = null;
        Bson match =  null;
        Bson limit = null;
    }*/
}
