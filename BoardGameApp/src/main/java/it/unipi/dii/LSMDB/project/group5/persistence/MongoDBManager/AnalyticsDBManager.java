package it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import org.bson.Document;
import org.bson.conversions.Bson;


import it.unipi.dii.LSMDB.project.group5.bean.*;

import java.sql.Timestamp;
import java.util.*;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.*;
import static it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.GameDBManager.fillInfoGameFields;
import static it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.UserDBManager.*;


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
               //System.out.println(cursor.next().toJson());

                Document next = cursor.next();
                if(value.equals("category")){
                    g = fillInfoGameFields(next, true);
                }else {
                    g = fillInfoGameFields(next, false);
                }

                ret.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static List<CountryBean> getUsersFromCountry (){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Bson projection = project(fields(excludeId(), computed("country", "$_id"), include("count")));
        Bson group = group("$country", sum("count", 1L));
        Bson match = match(and(ne("country", null), ne("country", "")));
        Bson sort = sort(descending("count"));

       List<CountryBean> ret = new ArrayList<CountryBean>();
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(match, group, projection, sort)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                CountryBean a = new CountryBean();
                a.setCountry((next.get("country") == null) ? "" : (next.get("country").toString()));
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
        Bson projection2 = project(fields(excludeId(), computed("category", "$_id"), include("totVotes", "avgRatingTot", "totGames")));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( unwind, match, group, projection2)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                ret.setAvgRatingTot(next.get("avgRatingTot")==null ? 0.0 : Double.parseDouble(next.get("avgRatingTot").toString()));
                ret.setName(category);
                ret.setNumRatesTot(next.get("totVotes")==null ? 0 : Integer.parseInt(next.get("totVotes").toString()));
                ret.setTotGames(Integer.parseInt(next.get("totGames") == null ? "0" : next.get("totGames").toString()));
           }
        }
        return ret;
    }

    public static List<CategoryBean> gamesDistribution (){
        List<CategoryBean> ret = new ArrayList<CategoryBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson unwind = unwind("$category");
        Bson group = group("$category", sum("count", 1L));
        Bson match = match(and(ne("category", null),(ne("category", ""))));
        Bson limit = limit(6);
        Bson projection2 = project(fields(excludeId(), computed("category", "$_id"), include("count")));
        Bson sort = sort(descending("count"));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( unwind, match, group, projection2,sort,limit)).iterator()) {

            while (cursor.hasNext()) {

                Document next = cursor.next();
                // System.out.println(next.toJson());
                CategoryBean g = new CategoryBean();
                g.setName(next.get("category").toString());
                g.setTotGames(Integer.parseInt(next.get("count") == null ? "0" : next.get("count").toString()));
                ret.add(g);
            }
        }
        return ret;
    }


    public static List<UserBean> showLessRecentLoggedUsers (){
        List<UserBean> ret = new ArrayList<UserBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Bson sort = sort(ascending("last_login"));
        Bson projection = project(fields(excludeId(), include("username", "last_login")));
        Bson match = match(and(ne("last_login", null),(ne("last_login", ""))));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, sort, projection)).iterator()) {

            while (cursor.hasNext()) {

                Document next = cursor.next();
                System.out.println(next.toJson());
                UserBean u = fillUserFields(next);
                ret.add(u);
            }
        }

        return ret;
    }

    public static List<AgeBean> getUsersForAge (){
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
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

        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        List<ActivityBean> ret = new ArrayList<ActivityBean>();

        Bson projection1 = project(fields(excludeId(), computed("date", new Document("$toDate", "$last_login")), include("username")));
        Bson match = match(ne("date", null));
        Bson projection = project(fields(excludeId(), computed("date", "$_id"), include("count")));
        Bson group = new Document("$group", new Document ("_id", new Document("month" ,new Document("$month", "$date"))
            .append("day", new Document ("$dayOfMonth", "$date"))
                .append("year", new Document ("$year", "$date")))
        .append("count", new Document("$sum", 1L)));
        Bson sort1 = sort(ascending("date.day"));
        Bson sort2 = sort(ascending("date.month"));
        Bson sort3 = sort(ascending("date.year"));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(projection1, match, group, projection,sort1, sort2, sort3)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                Document date = (Document) next.get("date");
                String d = date.get("year")+"-"+date.get("month")+"-"+date.get("day");
                //System.out.println(d + " " + next.get("count"));
                //System.out.println(next.toJson());
                ActivityBean a = new ActivityBean();
                a.setDate(d);
                a.setNumUser(next.get("count") == null ? 0 : Integer.parseInt(next.get("count").toString()));
                ret.add(a);
            }
        }
        return ret;
    }


    public static List<ActivityBean> dailyAvgLoginForCountry (){

        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        List<ActivityBean> ret = new ArrayList<ActivityBean>();

        Bson projection1 = project(fields(excludeId(), computed("date", new Document("$toDate", "$last_login")), include("username", "country")));
        Bson match = match(ne("date", null));
        Bson match1 = match(and(ne("country", ""), ne("country", null)));
        Bson projection = project(fields(excludeId(), computed("country", "$_id" ), include("avg")));
        Bson group = new Document("$group", new Document ("_id", new Document("month" ,new Document("$month", "$date"))
                .append("day", new Document ("$dayOfMonth", "$date"))
                .append("year", new Document ("$year", "$date"))
                .append("country", "$country"))
                .append("count", new Document("$sum", 1L)));
        Bson group2 = group("$_id.country", avg("avg", "$count"));
        Bson sort = sort(descending("avg"));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(projection1, match, match1, group, group2, projection,sort)).iterator()) {

            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                ActivityBean a = new ActivityBean();
                a.setCountry(next.get("country").toString());
                a.setAvgLogin(next.get("avg") == null ? 0.0 : Double.parseDouble((next.get("count").toString())));
                ret.add(a);
            }
        }
        return ret;
    }

    public static List<ActivityBean> dailyAvgLoginForAgeRange (int startRange, int endRange){

        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        List<ActivityBean> ret = new ArrayList<ActivityBean>();

        Bson projection1 = project(fields(excludeId(), computed("date", new Document("$toDate", "$last_login")), include("username", "age")));
        Bson match = match(ne("date", null));
        Bson match1 = match(and(lte("age", endRange), gte("age", startRange)));
        Bson projection = project(fields(excludeId(), /*computed("age", "$_id" ),*/ include("avg")));
        Bson group = new Document("$group", new Document ("_id", new Document("month" ,new Document("$month", "$date"))
                .append("day", new Document ("$dayOfMonth", "$date"))
                .append("year", new Document ("$year", "$date"))
                )
                .append("count", new Document("$sum", 1L)));
        Bson group2 = group("$_id", avg("avg", "$count"));
        Bson sort = sort(descending("avg"));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(projection1, match, match1, group, group2, projection,sort)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                ActivityBean a = new ActivityBean();
                a.setCountry(next.get("country").toString());
                a.setAvgLogin(next.get("avg") == null ? 0.0 : Double.parseDouble((next.get("count").toString())));
                ret.add(a);
            }
        }
        return ret;
    }

    public static List<InfluencerInfoBean> numberOfArticlesPublishedInASpecifiedPeriod (String start){
        List<InfluencerInfoBean> ret = new ArrayList<InfluencerInfoBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");

        Bson projection = project(fields(excludeId(), computed("author", "$_id"), include("count")));
        Bson group = group("$author",sum("count", 1L) );
        Bson match = match(and(gte("timestamp", start ), lte("timestamp", new Timestamp(System.currentTimeMillis()).toString()) ));
        Bson limit = limit(10);
        Bson sort = sort(ascending("count"));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(match, group, projection, sort, limit)).iterator()) {

            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                InfluencerInfoBean a = new InfluencerInfoBean();
                a.setCount(next.get("count") == null ? 0 : Integer.parseInt(next.get("count").toString()));
                a.setInfluencer(next.get("username") == null ? "": (next.get("username").toString()));
                ret.add(a);

            }
        }

        return ret;
    }

    public static List<InfluencerInfoBean> distinctGamesInArticlesPublishedInASpecifiedPeriod (String start){
        List<InfluencerInfoBean> ret = new ArrayList<InfluencerInfoBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");


        Bson unwind1 = unwind("$articles.games");
        Bson projection = project(fields(excludeId(), computed("author", "$_id"), include("count")));
        Bson group1 = new Document("$group", new Document ("_id", new Document("author" ,"$username")
                .append("games", "$articles.games")));
        Bson group = group("$_id.author",sum("count", 1L) );
        Bson match = match(and(gte("timestamp", start ), lte("timestamp", new Timestamp(System.currentTimeMillis()).toString()) ));
        Bson match2 = match (lte("count",10));
        Bson limit = limit(10);
        Bson sort = sort(ascending("count"));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( unwind1,  match, group1, group, match2, projection, sort, limit)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                InfluencerInfoBean a = new InfluencerInfoBean();
                a.setCount(next.get("count") == null ? 0 : Integer.parseInt(next.get("count").toString()));
                a.setInfluencer(next.get("username") == null ? "": (next.get("username").toString()));
                ret.add(a);

            }
        }

        return ret;
    }

    public static List<InfluencerInfoBean> getNumLikeForEachInfluencer (){
        List<InfluencerInfoBean> ret = new ArrayList<InfluencerInfoBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Bson projection = project(fields(excludeId(), computed("author", "$_id"), include("count")));
        Bson group = group("$author",sum("count", "$num_likes"));
        Bson limit = limit(3);
        Bson sort = sort(descending("count"));
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(group, projection, sort, limit)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                InfluencerInfoBean a = new InfluencerInfoBean();
                a.setCount(next.get("count") == null ? 0 : Integer.parseInt(next.get("count").toString()));
                a.setInfluencer(next.get("username") == null ? "": (next.get("username").toString()));
                ret.add(a);

            }
        }

        return ret;
    }

    public static List<InfluencerInfoBean> getNumDislikeForEachInfluencer (){
        List<InfluencerInfoBean> ret = new ArrayList<InfluencerInfoBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Bson projection = project(fields(excludeId(), computed("author", "$_id"), include("count")));
        Bson group = group("$author",sum("count", "$num_dislikes"));
        Bson limit = limit(3);
        Bson sort = sort(descending("count"));
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(group, projection, sort, limit)).iterator()) {

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                Document next = cursor.next();
                InfluencerInfoBean a = new InfluencerInfoBean();
                a.setCount(next.get("count") == null ? 0 : Integer.parseInt(next.get("count").toString()));
                a.setInfluencer(next.get("username") == null ? "": (next.get("username").toString()));
                ret.add(a);

            }
        }

        return ret;
    }
}
