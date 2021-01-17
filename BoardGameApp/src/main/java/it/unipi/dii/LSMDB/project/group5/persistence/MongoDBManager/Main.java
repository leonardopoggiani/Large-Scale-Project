package it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager;

import org.bson.Document;
import it.unipi.dii.LSMDB.project.group5.bean.*;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.*;

import java.sql.Timestamp;
import java.util.*;

public class Main {
    public static void main (String[] args){
        MongoDBManager.createConnection();
        System.out.println("Connesso");

        //signup
        //User u = new User("gaia2", "gaia", "anastasi", 22, "italy");
        //SignupLoginDBManager.signup(u);

        //update login
        //SignupLoginDBManager.updateLogin("gaia2");

        //Read game
        //GameBean g = GameDBManager.readGame("Monopoli");
        /*System.out.println(g.getAvgRating() + " " + g.getMaxAge() + " " + g.getMinAge() + " " +g.getMinPlayers() + " " +
        g.getMaxPlayers() + " " +g.getNumReviews() + " " + g.getYear() + " " + g.getCategory1() + " " +g.getCategory2() + " " + g.getImageUrl() +g.getMaxTime()
        +" " +g.getMinTime() +" " +g.getName() + " " + g.getUrl() + " " + g.getComplexity() + " " + g.getNumVotes() + " " + g.getAlternativeName() + " " + g.getFamily() + " " + g.isCooperative());
*/
        //Filter article
        //List<ArticleBean> list = ArticleDBManager.filterByInfluencer("gaia1");
        //List<ArticleBean> list = ArticleDBManager.filterByGame("Cluedo");
        //List<ArticleBean> list = ArticleDBManager.filterByDate("2020-01-16");
        //List<ArticleBean> list = ArticleDBManager.orderBy("comments");
        //ArticleDBManager.updateNumComments(-1, 0 );
        //ArticleBean list = ArticleDBManager.readArticle(0);
        //System.out.println(list);


        //Filter game
        //List<GameBean> list = GameDBManager.filterByName("Monopoli");
        //boolean ret = GameDBManager.doesGameExists("Muse: Awakenings");


        //List<GameBean> list = GameDBManager.filterByPlayers(10);
        //List<GameBean> list = GameDBManager.filterByYear(1960);
        //List<GameBean> list = GameDBManager.orderBy("numVotes");
        //System.out.println(list);


        //org.openjfx.DBManager.MongoDBManager.GameDBManager.updateRating(7, "Monopoli");
        //org.openjfx.DBManager.MongoDBManager.GameDBManager.updateNumReviews(10, "Monopoli");
        //org.openjfx.DBManager.MongoDBManager.GameDBManager.updateNumVotes(10, "Monopoli");

        //ANALYTICS
       //AnalyticsDBManager.getUsersFromCountry();

        //AnalyticsDBManager.showLeastRatedGames("category", "Card Game:1002");
        //AnalyticsDBManager.getCategoryInfo("Card Game:1002");
        //org.openjfx.DBManager.MongoDBManager.AnalyticsDBManager.showLessRecentLoggedUsers();
        //org.openjfx.DBManager.MongoDBManager.AnalyticsDBManager.getUsersForAge();
        AnalyticsDBManager.getActivitiesStatisticsTotal();
        //AnalyticsDBManager.numberOfArticlesPublishedInASpecifiedPeriod("2020-01-12 00:00:00");
        //AnalyticsDBManager.distinctGamesInArticlesPublishedInASpecifiedPeriod("2020-01-12 00:00:00");
        //AnalyticsDBManager.getNumLikeForEachInfluencer();
        //AnalyticsDBManager.getNumDislikeForEachInfluencer();
        //AnalyticsDBManager.dailyAvgLoginForAgeRange(18,30);

        /*ArticleBean a = new ArticleBean();
        List<String> list1 = new ArrayList<>();
        list1.add("Monopoli");
        list1.add("Sherlock");
        a.setListGame(list1);
        a.setAuthor("chakado");
        a.setNumberComments(0);
        a.setTitle("Articolo1");
        a.setTimestamp(new Timestamp(System.currentTimeMillis()));
        a.setText("NON LO SO");
        a.setNumberDislikes(1);
        a.setNumberLikes(14);
        boolean ret = ArticleDBManager.addArticle(a);*/


        MongoDBManager.close();

    }
}
