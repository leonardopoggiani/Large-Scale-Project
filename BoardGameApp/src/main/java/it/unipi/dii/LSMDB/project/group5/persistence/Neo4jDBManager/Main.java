package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.controller.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main( String[] args ) throws Exception {
        //Neo4jDBManager.InitializeDriver();
        GamesReviewsRatesDBController gr = new GamesReviewsRatesDBController();

        ArticlesCommentsLikesDBController artCon  = new ArticlesCommentsLikesDBController();

        UpdateDatabaseDBController ud = new UpdateDatabaseDBController();

        UsersDBController uDB = new UsersDBController();

        AnalyticsDBController aDB = new AnalyticsDBController();

        /*ArticlesCommentsLikesDBController la = new ArticlesCommentsLikesDBController();

        ArticleBean art = new ArticleBean("Nuovo articolo1", "Clarissa1", new Timestamp(System.currentTimeMillis()),"Risiko", null);
        la.addArticle(art);

        String authorDel = "Clarissa1";
        String titleDel = "Nuovo articolo2";
        la.deleteArticle(authorDel, titleDel);*/
        //List<ArticleBean> suggArticles = artCon.neo4jListSuggestedArticles("Gaia5");
        //List<String> suggestions = uDB.neo4jListSuggestingFollowing("Gaia5","normalUser");
        //List<String> listUsers = uDB.neo4jListUsers("Gaia5", "followersOnly");
        //System.out.println(suggArticles);
        //System.out.println(suggestions);
        //System.out.println(listUsers);

        //uDB.neo4jAddRemoveFollow("Gaia5", "Leonardo1", "remove");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        long time = date.getTime();
        String dateString = new Timestamp(time).toString();
        //List<StatisticsInfluencer> statInfluVer = AnalyticsDBManager.versatileInfluencers(dateString);
        //System.out.println("Fatto");
        //List<ArticleBean> art = artCon.neo4jListSuggestedArticles("Gaia5");
        //System.out.println(statInfluVer);






        Neo4jDBManager.close();





    }
}
