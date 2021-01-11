package org.openjfx.DBManager.Neo4jDBManager;

import org.openjfx.Controller.GamesReviewsRatesDBController;
import org.openjfx.Controller.UpdateDatabaseDBController;
import org.openjfx.Controller.UsersDBController;

import java.util.List;

public class Main {

    public static void main( String[] args ) throws Exception {
        //Neo4jDBManager.InitializeDriver();
        GamesReviewsRatesDBController gr = new GamesReviewsRatesDBController();

        UpdateDatabaseDBController ud = new UpdateDatabaseDBController();

        UsersDBController uDB = new UsersDBController();

        /*ArticlesCommentsLikesDBController la = new ArticlesCommentsLikesDBController();

        ArticleBean art = new ArticleBean("Nuovo articolo1", "Clarissa1", new Timestamp(System.currentTimeMillis()),"Risiko", null);
        la.addArticle(art);

        String authorDel = "Clarissa1";
        String titleDel = "Nuovo articolo2";
        la.deleteArticle(authorDel, titleDel);*/

        //List<String> suggestions = uDB.neo4jListSuggestingFollowing("Clarissa1");
        List<String> listUsers = uDB.neo4jListUsersFriends("Gaia5", "followersOnly");
        //System.out.println(suggestions);
        System.out.println(listUsers);





        Neo4jDBManager.close();





    }
}
