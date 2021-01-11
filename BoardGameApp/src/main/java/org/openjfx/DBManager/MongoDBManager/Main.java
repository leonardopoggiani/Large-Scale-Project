package org.openjfx.DBManager.MongoDBManager;

import org.bson.Document;
import org.openjfx.Entities.*;

import java.util.*;

public class Main {
    public static void main (String[] args){
        //MongoDBManager.createConnection();
        System.out.println("Connesso");

        //signup
        //User u = new User("gaia2", "gaia", "anastasi", 22, "italy");
        //SignupLoginDBManager.signup(u);

        //update login
        //SignupLoginDBManager.updateLogin("gaia2");

        //Read game
        //InfoGame g = org.openjfx.DBManager.MongoDBManager.GameDBManager.readGame("Monopoli");
        /*System.out.println(g.getAvgRating() + " " + g.getMaxAge() + " " + g.getMinAge() + " " +g.getMinPlayers() + " " +
        g.getMaxPlayers() + " " +g.getNumReviews() + " " + g.getYear() + " " + g.getCategory1() + " " +g.getCategory2() + " " + g.getImageUrl() +g.getMaxTime()
        +" " +g.getMinTime() +" " +g.getName() + " " + g.getUrl() + " " + g.getComplexity() + " " + g.getNumVotes() + " " + g.getAlternativeName() + " " + g.getFamily() + " " + g.isCooperative());
*/
        //Filter article
        //List<Article> list = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.filterByInfluencer("gaia1");
        //List<Article> list = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.filterByGame("Cluedo");
        //List<Article> list = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.filterByDate("2020-12-29");
        //List<Article> list = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.orderBy("comments");
        //org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumComments(-1, "gaia1", "Giudizio su Azul" );
       // System.out.println(list);


        //Filter game
        //List<InfoGame> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.filterByName("Monopoli");
        //List<InfoGame> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.filterByCategory("Economy");

        //List<InfoGame> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.filterByPlayers(10);
        //List<InfoGame> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.filterByYear(1960);
        //List<InfoGame> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.orderBy("numVotes");
        //System.out.println(list);


        //org.openjfx.DBManager.MongoDBManager.GameDBManager.updateRating(7, "Monopoli");
        //org.openjfx.DBManager.MongoDBManager.GameDBManager.updateNumReviews(10, "Monopoli");
        //org.openjfx.DBManager.MongoDBManager.GameDBManager.updateNumVotes(10, "Monopoli");

        //ANALYTICS
        //org.openjfx.DBManager.MongoDBManager.AnalyticsDBManager.getUsersFromCountry("italy");
        //org.openjfx.DBManager.MongoDBManager.AnalyticsDBManager.showLeastRatedGames("year", "1960");
        //org.openjfx.DBManager.MongoDBManager.AnalyticsDBManager.getCategoryInfo("Play Card");
        //org.openjfx.DBManager.MongoDBManager.AnalyticsDBManager.showLessRecentLoggedUsers();
        //org.openjfx.DBManager.MongoDBManager.AnalyticsDBManager.getUsersForAge();
        org.openjfx.DBManager.MongoDBManager.AnalyticsDBManager.getActivitiesStatisticsTotal();

        MongoDBManager.close();

    }
}
