package org.openjfx.DBManager.Neo4jDBManager;

import org.openjfx.Controller.ListArticlesAndCommentsDBController;
import org.openjfx.Controller.LoginSignUpDBController;
import org.openjfx.Entities.Article;
import org.openjfx.Entities.User;

public class Main {

    public static void main( String[] args ) throws Exception {
        Neo4jDBManager.InitializeDriver();
        LoginSignUpDBController cont = new LoginSignUpDBController();
        Article a = new Article();
        int quantiLike = 0;
        ListArticlesAndCommentsDBController la = new ListArticlesAndCommentsDBController();
        User user = new User("Gaia4", "ciaociao", "Strategic", "Cards Game", 28, "normalUser" );

        cont.neo4jRegisterUserController(user);
        System.out.println("---------------------------");
        cont.neo4jLoginUserController("Gaia4", "ciaociao");
        System.out.println("---------------------------");


        la.neo4jListSuggestedArticles("Gaia5");
        System.out.println("---------------------------");

        la.neo4jListArticlesComment("Mio articolo bello", "Clarissa1");
        System.out.println("---------------------------");

        a = la.mongoDBshowArticle("Mio articolo bello", "Clarissa1");
        System.out.println(a.toString());
        System.out.println("---------------------------");

        quantiLike = la.neo4jCountLikes("Mio articolo bello", "Clarissa1", "like");

        quantiLike = la.neo4jCountLikes("Mio articolo bello", "Clarissa1", "dislike");



    }
}
