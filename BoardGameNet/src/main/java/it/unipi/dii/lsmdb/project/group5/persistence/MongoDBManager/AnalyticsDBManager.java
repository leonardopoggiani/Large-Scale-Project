package it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import it.unipi.dii.lsmdb.project.group5.bean.GameBean;
import it.unipi.dii.lsmdb.project.group5.bean.*;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.sql.Timestamp;
import java.util.*;
import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.*;

/** The type Analytics db manager. */
public class AnalyticsDBManager {
  /**
   * Show least rated games list.
   *
   * @param mode the mode
   * @param value the value
   * @return the list
   */
  public static List<GameBean> showLeastRatedGames(String mode, String value) {
      //Return the top 3 least rated games
        List<GameBean> ret = new ArrayList<GameBean>();

        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson unwind = null;
        Bson projection = null;
        Bson sort = null;
        Bson match =  null;
        Bson limit = null;

        if (mode.equals("category")){
            //Return the top 3 least rated games given a specific category
            unwind = unwind("$category");
            projection = project(fields( excludeId(), include("name", "num_votes")));
            sort = sort(ascending("num_votes"));
            match =  match(and(eq("category",value), ne("num_votes","") , ne("num_votes", null)));
            limit = limit(3);

        } else {
            //Return the top 3 least rated games given a specific year
            int year = Integer.parseInt(value);
            /*projection = project(fields( excludeId(), include("name", "num_votes")));
            sort = sort(ascending("num_votes"));
            match =  match(and(eq("year",year), ne("num_votes","") , ne("num_votes", null)));
            limit = limit(10);*/
            projection = fields( excludeId(), include("name", "num_votes"));
            sort = (ascending("num_votes"));
            match =  (and(eq("year",year), ne("num_votes","") , ne("num_votes", null)));


        }
        GameBean g = new GameBean();

        try{
            MongoCursor<Document> cursor;
            if(mode.equals("category")) {
                //We are searching the least rated games given a category
                cursor = collection.aggregate(Arrays.asList(unwind, match, projection, sort, limit)).iterator();
            }

            else {
                //We are searching the least rated games given a year
                //cursor = collection.aggregate(Arrays.asList( match, projection, sort, limit)).iterator();
                cursor = collection.find(match).projection(projection).sort(sort).limit(3).iterator();
            }

            while (cursor.hasNext()) {
                Document next = cursor.next();
                if(mode.equals("category")){
                    g = GameDBManager.fillInfoGameFields(next, true);
                }else {
                    g = GameDBManager.fillInfoGameFields(next, false);
                }
                //Add the game to the list
                ret.add(g);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        return ret;
    }

  /**
   * Get users from country list.
   *
   * @return the list
   */
  public static List<CountryBean> getUsersFromCountry() {
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Bson projection = project(fields(excludeId(), computed("country", "$_id"), include("count")));
        Bson group = group("$country", sum("count", 1L));
        Bson match = match(and(ne("country", null), ne("country", "")));
        Bson sort = sort(descending("count"));
        Bson limit = limit(6);

       List<CountryBean> ret = new ArrayList<CountryBean>();
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(match, group, projection, sort, limit)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                CountryBean a = new CountryBean();
                a.setCountry((next.get("country") == null) ? "" : (next.get("country").toString()));
                a.setNumUser((next.get("count") == null) ? 0 : Integer.parseInt(next.get("count").toString()));
                ret.add(a);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());

        }
        return ret;
    }

  /**
   * Get category info category bean.
   *
   * @param category the category
   * @return the category bean
   */
  public static CategoryBean getCategoryInfo(String category) {
      //Given a specific category return the number of ratings, the average rating and the number of games related to that category
        CategoryBean ret = new CategoryBean();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson unwind = unwind("$category");
        Bson match = match(eq("category", category));
        Bson group = new Document("$group", new Document("_id","$category")
        .append("totVotes", new Document("$sum", "$num_votes"))
        .append("avgRatingTot", new Document("$avg", "$avg_rating"))
        .append("totGames", new Document("$sum", 1L)));
        Bson projection2 = project(fields(excludeId(), computed("category", "$_id"), include("totVotes", "avgRatingTot", "totGames")));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( unwind, match, group, projection2)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                ret.setAvgRatingTot(next.get("avgRatingTot")==null ? 0.0 : Double.parseDouble(next.get("avgRatingTot").toString()));
                ret.setName(category);
                ret.setNumRatesTot(next.get("totVotes")==null ? 0 : Integer.parseInt(next.get("totVotes").toString()));
                ret.setTotGames(Integer.parseInt(next.get("totGames") == null ? "0" : next.get("totGames").toString()));
           }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        return ret;
    }

  /**
   * Games distribution list.
   *
   * @return the list
   */
  public static List<CategoryBean> gamesDistribution() {
      //For each category it computes the total number of games based on the currently considered
      // category and then will return the top 6 category for number of games based on them
        List<CategoryBean> ret = new ArrayList<CategoryBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Games");
        Bson unwind = unwind("$category");
        Bson group = group("$category", sum("count", 1L));
        Bson match = match(and(ne("category", null),(ne("category", ""))));
        Bson limit = limit(6);
        Bson projection2 = project(fields(excludeId(), computed("category", "$_id"),
                include("count")));
        Bson sort = sort(descending("count"));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( unwind, match, group, projection2,sort,limit)).iterator()) {

            while (cursor.hasNext()) {

                Document next = cursor.next();
                CategoryBean g = new CategoryBean();
                g.setName(next.get("category").toString());
                g.setTotGames(Integer.parseInt(next.get("count") == null ? "0" : next.get("count").toString()));
                ret.add(g);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        return ret;
    }

  /**
   * Show less recent logged users list.
   *
   * @return the list
   */
  public static List<UserBean> showLessRecentLoggedUsers() {
      //This query shows the users who has logged less recently in the application
        List<UserBean> ret = new ArrayList<UserBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Bson sort = sort(ascending("last_login"));
        Bson projection = project(fields(excludeId(), include("username", "last_login")));
        Bson match = match(and(ne("last_login", null),(ne("last_login", ""))));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, sort, projection)).iterator()) {

            while (cursor.hasNext()) {

                Document next = cursor.next();
                UserBean u = UserDBManager.fillUserFields(next);
                ret.add(u);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }

        return ret;
    }

  /**
   * Get users for age list.
   *
   * @return the list
   */
  public static List<AgeBean> getUsersForAge() {
      //For each age this query returns the total number of games based on the currently considered age
        MongoCollection<Document> collection = MongoDBManager.getCollection("Users");
        Bson projection = project(fields(excludeId(), computed("age", "$_id"), include("count")));
        Bson group = group("$age", sum("count", 1L));
        Bson match = match(and(ne("age", null), ne("age", "")));

        List<AgeBean> ret = new ArrayList<AgeBean>();
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( match, group, projection)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                AgeBean a = new AgeBean();
                a.setAge(Integer.parseInt(next.get("age").toString()));
                a.setNumUser((next.get("count") == null) ? 0 : Integer.parseInt(next.get("count").toString()));
                ret.add(a);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        return ret;
    }

  /**
   * Get activities statistics total list.
   *
   * @return the list
   */
  public static List<ActivityBean> getActivitiesStatisticsTotal() {
      //This query groups all the users on the day of their last login and for each day count the number of people who have logged in the application.
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
                Document next = cursor.next();
                Document date = (Document) next.get("date");
                String d = date.get("year")+"-"+date.get("month")+"-"+date.get("day");
                ActivityBean a = new ActivityBean();
                a.setDate(d);
                a.setNumUser(next.get("count") == null ? 0 : Integer.parseInt(next.get("count").toString()));
                ret.add(a);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        return ret;
    }

  /**
   * Daily avg login for country list.
   *
   * @return the list
   */
  public static List<ActivityBean> dailyAvgLoginForCountry() {
    //This query computes the number of login made in a single day and than it considers users' country to compute the average number of login made by users from each country in a single day.
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
                Document next = cursor.next();
                ActivityBean a = new ActivityBean();
                a.setCountry(next.get("country").toString());
                a.setAvgLogin(next.get("avg") == null ? 0.0 : Double.parseDouble((next.get("avg").toString())));
                ret.add(a);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        return ret;
    }

  /**
   * Number of articles published in a specified period list.
   *
   * @param start the start
   * @return the list
   */
  public static List<InfluencerInfoBean> numberOfArticlesPublishedInASpecifiedPeriod(String start) {
      //Considering a specific period this query returns the top 10 influencers with the least number of articles published the period previously specified
      List<InfluencerInfoBean> ret = new ArrayList<InfluencerInfoBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");

        Bson projection = project(fields(excludeId(), computed("author", "$_id"), include("count")));
        Bson group = group("$author",sum("count", 1L) );
        Bson match = match(and(gte("timestamp", start ), lte("timestamp", new Timestamp(System.currentTimeMillis()).toString()) ));
        Bson limit = limit(10);
        Bson sort = sort(ascending("count"));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(match, group, projection, sort, limit)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                InfluencerInfoBean a = new InfluencerInfoBean();
                a.setCount(next.get("count") == null ? 0 : Integer.parseInt(next.get("count").toString()));
                a.setInfluencer(next.get("author") == null ? "": (next.get("author").toString()));
                ret.add(a);

            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }

        return ret;
    }

  /**
   * Distinct games in articles published in a specified period list.
   *
   * @param start the start
   * @return the list
   */
  public static List<InfluencerInfoBean> distinctGamesInArticlesPublishedInASpecifiedPeriod(
      String start) {
      //This query returns the top 10 influencers with the least number of articles about distinct games published in the period previously specified. It considers only the influencers that have wrote about less than 10 different games.
        List<InfluencerInfoBean> ret = new ArrayList<InfluencerInfoBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");


        Bson unwind1 = unwind("$games");
        Bson projection = project(fields(excludeId(), computed("author", "$_id"), include("count")));
        Bson group1 = new Document("$group", new Document ("_id", new Document("author" ,"$author")
                .append("games", "$games")));
        Bson group = group("$_id.author",sum("count", 1L) );
        Bson match = match(and(gte("timestamp", start ), lte("timestamp", new Timestamp(System.currentTimeMillis()).toString()) ));
        Bson match2 = match (lte("count",10));
        Bson limit = limit(10);
        Bson sort = sort(ascending("count"));

        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList( unwind1,  match, group1, group, match2, projection, sort, limit)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                InfluencerInfoBean a = new InfluencerInfoBean();
                a.setCount(next.get("count") == null ? 0 : Integer.parseInt(next.get("count").toString()));
                a.setInfluencer(next.get("author") == null ? "": (next.get("author").toString()));
                ret.add(a);

            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }

        return ret;
    }

  /**
   * Get num like for each influencer list.
   *
   * @return the list
   */
  public static List<InfluencerInfoBean> getNumLikeForEachInfluencer() {
      //This query returns the top 3 influencers that have received the most number of likes considering all their articles
        List<InfluencerInfoBean> ret = new ArrayList<InfluencerInfoBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Bson projection = project(fields(excludeId(), computed("author", "$_id"), include("count")));
        Bson group = group("$author",sum("count", "$num_likes"));
        Bson limit = limit(3);
        Bson sort = sort(descending("count"));
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(group, projection, sort, limit)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                InfluencerInfoBean a = new InfluencerInfoBean();
                a.setCount(next.get("count") == null ? 0 : Integer.parseInt(next.get("count").toString()));
                a.setInfluencer(next.get("author") == null ? "": (next.get("author").toString()));
                ret.add(a);

            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }

        return ret;
    }

  /**
   * Get num dislike for each influencer list.
   *
   * @return the list
   */
  public static List<InfluencerInfoBean> getNumDislikeForEachInfluencer() {
        List<InfluencerInfoBean> ret = new ArrayList<InfluencerInfoBean>();
        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");
        Bson projection = project(fields(excludeId(), computed("author", "$_id"), include("count")));
        Bson group = group("$author",sum("count", "$num_dislikes"));
        Bson limit = limit(3);
        Bson sort = sort(descending("count"));
        try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(group, projection, sort, limit)).iterator()) {

            while (cursor.hasNext()) {
                Document next = cursor.next();
                InfluencerInfoBean a = new InfluencerInfoBean();
                a.setCount(next.get("count") == null ? 0 : Integer.parseInt(next.get("count").toString()));
                a.setInfluencer(next.get("author") == null ? "": (next.get("author").toString()));
                ret.add(a);

            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }

        return ret;
    }
}
