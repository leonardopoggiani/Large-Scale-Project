package it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import org.bson.Document;
import org.bson.conversions.Bson;


import java.util.*;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;


public class AnalyticsDBManager {
    public static List<GameBean> showLeastRatedGames (String mode, String value){
        List<GameBean> ret = new ArrayList<GameBean>();

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
            sort = sort(ascending("num_votes"));
            match =  match(and(eq("category",value), ne("num_votes","") , ne("num_votes", null)));
            limit = limit(10);

        } else {
            //ON YEAR
            int year = Integer.parseInt(value);
            projection = project(fields( excludeId(), include("name", "num_votes")));
            sort = sort(ascending("num_votes"));
            match =  match(and(eq("year",year), ne("num_votes","") , ne("num_votes", null)));
            limit = limit(10);

        }
        GameBean g = new GameBean();

        try{
            MongoCursor<Document> cursor;
            if(value.equals("category")) {
                cursor = collection.aggregate(Arrays.asList(unwind, match, projection, sort, limit)).iterator();
            }

            else {
                cursor = collection.aggregate(Arrays.asList( match, projection, sort, limit)).iterator();
            }

            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());

                /*Document next = cursor.next();
                if(value.equals("category")){
                    g = fillInfoGameFields(next, true);
                }else {
                    g = fillInfoGameFields(next, false);
                }

                ret.add(g);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static int getUsersFromCountry (String country){
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");
        Bson projection = project(fields(excludeId(), computed("country", "$_id"), include("count")));
        Bson group = group("$country", sum("count", 1L));

        Bson match =  match(eq("country",country));
        int ret = 0;
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, group, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                ret = (next.get("count")==null) ? 0: Integer.parseInt(next.get("count").toString());

            }
        }
        return ret;
    }
}
