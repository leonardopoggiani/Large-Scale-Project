package org.openjfx.DBManager.Neo4jDBManager;

import org.openjfx.Controller.ArticlesCommentsLikesDBController;
import org.openjfx.Controller.LoginSignUpDBController;
import org.openjfx.Controller.UpdateDatabaseDBController;
import org.openjfx.Entities.Article;
import org.openjfx.Entities.InfoComment;
import org.openjfx.Entities.InfoLike;
import org.openjfx.Entities.User;

public class Main {

    public static void main( String[] args ) throws Exception {
        Neo4jDBManager.InitializeDriver();
        LoginSignUpDBController cont = new LoginSignUpDBController();
        UpdateDatabaseDBController ud = new UpdateDatabaseDBController();
        Article a = new Article();
        InfoComment c = new InfoComment();
        InfoLike like = new InfoLike();
        int quantiLike = 0;
        int quantiDislike = 0;
        int quantiComments = 0;
        ArticlesCommentsLikesDBController la = new ArticlesCommentsLikesDBController();
        User user = new User("Gaia4", "ciaociao", "Strategic", "Cards Game", 28, "normalUser" );

        cont.neo4jRegisterUserController(user);
        System.out.println("---------------------------");
        cont.neo4jLoginUserController("Gaia4", "ciaociao");
        System.out.println("---------------------------");


        la.neo4jListSuggestedArticles("Gaia5");
        System.out.println("---------------------------");

        la.neo4jListArticlesComment("Mio articolo bello", "Clarissa1");
        System.out.println("---------------------------");

        a = la.mongoDBshowArticle("Nuovo articolo6", "Clarissa1");
        System.out.println(a.toString());
        System.out.println("---------------------------");

        quantiLike = la.neo4jCountLikes("Mio articolo bello", "Clarissa1", "like");

        quantiDislike = la.neo4jCountLikes("Mio articolo bello", "Clarissa1", "dislike");

        quantiComments = la.neo4jCountComments("Mio articolo bello", "Clarissa1");

        c.setAuthor("sara");
        c.setText("Carinooo");
        c.setTimestamp("DataEora");
        c.setAuthorArt("Clarissa1");
        c.setTitleArt("Mio articolo bello");
        Boolean ret;

        ret = ud.Neo4jAddComment(c);

        System.out.println("Comments prima: " + quantiComments);

        quantiComments = la.neo4jCountComments("Mio articolo bello", "Clarissa1");

        System.out.println("Comments dopo: " + quantiComments);


        like.setAuthor("sara");
        like.setType("dislike");
        like.setTimestamp("DataEora");
        like.setAuthorArt("Clarissa1");
        like.setTitleArt("Mio articolo bello");
        Boolean retLike;



        System.out.println("Dislike prima: " + quantiDislike);

        retLike = ud.Neo4jAddLike(like);

        quantiDislike = la.neo4jCountLikes("Mio articolo bello", "Clarissa1", "dislike");

        System.out.println("Dislike dopo: " + quantiDislike);


    }
}
