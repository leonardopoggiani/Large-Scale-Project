package org.openjfx.DBManager.Neo4jDBManager;

import org.openjfx.Controller.ArticlesCommentsLikesDBController;
import org.openjfx.Controller.GamesReviewsRatesDBController;
import org.openjfx.Controller.UpdateDatabaseDBController;
import org.openjfx.Entities.ArticleBean;

import java.sql.Timestamp;

public class Main {

    public static void main( String[] args ) throws Exception {
        //Neo4jDBManager.InitializeDriver();
        GamesReviewsRatesDBController gr = new GamesReviewsRatesDBController();

        UpdateDatabaseDBController ud = new UpdateDatabaseDBController();

        ArticlesCommentsLikesDBController la = new ArticlesCommentsLikesDBController();

        ArticleBean art = new ArticleBean("Nuovo articolo1", "Clarissa1", new Timestamp(System.currentTimeMillis()),"Risiko", null);
        la.addArticle(art);

        Neo4jDBManager.close();





    }
}
