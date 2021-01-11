package org.openjfx.DBManager.MongoDBManager;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;


import org.openjfx.Entities.*;

import java.util.*;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.*;
import static org.openjfx.DBManager.MongoDBManager.UserDBManager.*;


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

    public static List<CountryBean> getUsersFromCountry (){
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");
        Bson projection = project(fields(excludeId(), computed("country", "$_id"), include("count")));
        Bson group = group("$country", sum("count", 1L));
        Bson match = match(and(ne("country", null), ne("country", "")));

       List<CountryBean> ret = new ArrayList<CountryBean>();
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( group, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                CountryBean a = new CountryBean();
                a.setCountry(next.get("age").toString());
                a.setNumUser((next.get("count") == null) ? 0 : Integer.parseInt(next.get("count").toString()));
                ret.add(a);


            }
        }
        return ret;
    }

    public static CategoryBean getCategoryInfo (String category){
        CategoryBean ret = new CategoryBean();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson unwind = unwind("$category");
        Bson match = match(eq("category", category));
        //Bson projection = project(fields(excludeId(), computed("category", "$_id"), include("totVotes")));
        Bson group = new Document("$group", new Document("_id","$category")
        .append("totVotes", new Document("$sum", "$num_votes"))
        .append("avgRatingtot", new Document("$avg", "$avg_rating"))
        .append("totGames", new Document("$sum", 1L)));
        //Bson group2 = group("$category", avg("avgRatingTot", "$avg_rating"));
        Bson projection2 = project(fields(excludeId(), computed("category", "$_id"), include("category, totVotes, avgRatingTot, totGames")));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( unwind, match, group, projection2)).iterator()) {

            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                ret.setAvgRatingTot(next.get("avgRatingTot")==null ? 0.0 : Double.parseDouble(next.get("avgRatingTot").toString()));
                ret.setName(category);
                ret.setNumRatesTot(next.get("totVotes")==null?0: Integer.parseInt(next.get("totVotes").toString()));
                ret.setTotGames(Integer.parseInt(next.get("totGames").toString()));


            }
        }

        return ret;

    }

    public static List<UserBean> showLessRecentLoggedUsers (){
        List<UserBean> ret = new ArrayList<UserBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");
        Bson sort = sort(ascending("last_login"));
        Bson projection = project(fields(excludeId(), include("username", "last_login")));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( sort, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                UserBean u = fillUserFields(next);
                ret.add(u);


            }
        }

        return ret;
    }

    public static List<AgeBean> getUsersForAge (){
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");
        Bson projection = project(fields(excludeId(), computed("age", "$_id"), include("count")));
        Bson group = group("$age", sum("count", 1L));
        Bson match = match(and(ne("age", null), ne("age", "")));

        List<AgeBean> ret = new ArrayList<AgeBean>();
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, group, projection)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                AgeBean a = new AgeBean();
                a.setAge(Integer.parseInt(next.get("age").toString()));
                a.setNumUser((next.get("count") == null) ? 0 : Integer.parseInt(next.get("count").toString()));
                ret.add(a);


            }
        }
        return ret;
    }

    public static List<ActivityBean> getActivitiesStatisticsTotal (){
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");
        List<ActivityBean> ret = new ArrayList<ActivityBean>();
        BasicDBList pipeline = new BasicDBList();

        Bson projection = project(fields(excludeId(), computed("date", "$_id"), include("count")));
        Bson group = new Document("$group", new Document ("_id", new Document("month" ,new Document("$month", "$last_login"))
            .append("day", new Document ("$dayOfMonth", "$last_login"))
                .append("year", new Document ("$year", "$last_login")))
        .append("count", new Document("$sum", 1L)));


        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(group, projection)).iterator()) {

            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());



            }
        }
        return ret;

    }
}
