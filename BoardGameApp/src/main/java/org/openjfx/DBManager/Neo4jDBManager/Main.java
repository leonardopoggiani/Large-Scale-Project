package org.openjfx.DBManager.Neo4jDBManager;

import org.openjfx.Controller.ArticlesCommentsLikesDBController;
import org.openjfx.Controller.GamesReviewsRatesDBController;
import org.openjfx.Controller.UpdateDatabaseDBController;
import org.openjfx.Controller.FindSuggestUsersDBController;

public class Main {

    public static void main( String[] args ) throws Exception {
        //Neo4jDBManager.InitializeDriver();
        GamesReviewsRatesDBController gr = new GamesReviewsRatesDBController();

        UpdateDatabaseDBController ud = new UpdateDatabaseDBController();

        ArticlesCommentsLikesDBController la = new ArticlesCommentsLikesDBController();


        la.neo4jListSuggestedArticles("Gaia5");
        System.out.println("---------------------------");

        la.neo4jListSuggestedArticles("sara");
        System.out.println("---------------------------");
        /*
        LoginSignUpDBController cont = new LoginSignUpDBController();
        Article a = new Article();
        InfoComment c = new InfoComment();
        InfoLike like = new InfoLike();
        int quantiLike = 0;
        int quantiDislike = 0;
        int quantiComments = 0;

        User user = new User("Gaia4", "ciaociao", "Strategic", "Cards InfoGame", 28, "normalUser" );

        cont.neo4jRegisterUserController(user);
        System.out.println("---------------------------");
        cont.neo4jLoginUserController("Gaia4", "ciaociao");
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
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        c.setTimestamp(ts);
        c.setAuthorArt("Clarissa1");
        c.setTitleArt("Mio articolo bello");
        Boolean ret;

        ret = ud.Neo4jAddComment(c);

        System.out.println("Comments prima: " + quantiComments);

        quantiComments = la.neo4jCountComments("Mio articolo bello", "Clarissa1");

        System.out.println("Comments dopo: " + quantiComments);


        like.setAuthor("sara");
        like.setType("dislike");
        ts = new Timestamp(System.currentTimeMillis());
        like.setTimestamp(ts);
        like.setAuthorArt("Clarissa1");
        like.setTitleArt("Mio articolo bello");
        int retLike;



        System.out.println("Dislike prima: " + quantiDislike);

        retLike = ud.Neo4jAddLike(like);

        quantiDislike = la.neo4jCountLikes("Mio articolo bello", "Clarissa1", "dislike");

        System.out.println("Dislike dopo: " + quantiDislike);


        System.out.println("Prova deleteComment, Comment prima: ");

        System.out.println(c.toString());


        Boolean retDelCom = ud.Neo4jDeleteComment(c);

        gr.neo4jListSuggestedGames("Gaia5");
        System.out.println("---------------------------");


        int quanteRev = gr.neo4jCountReviews("Spirit Island");
        int quantiRat = gr.neo4jCountRates("Spirit Island");
        System.out.println("Quante reviews: " + quanteRev);

        System.out.println("Quanti rates: " + quantiRat);

        System.out.println("Average rates: " + gr.neo4jAvgRates("Spirit Island"));

        gr.neo4jListGamesReviews("Spirit Island");

        InfoReview rev = new InfoReview();

        rev.setAuthor("sara");
        rev.setText("Gioco di merda");
        Timestamp tsrev = new Timestamp(System.currentTimeMillis());
        rev.setTimestamp(tsrev);
        rev.setGame("Spirit Island");
        Boolean ret;

        ret = ud.Neo4jAddReview(rev);

        System.out.println("Reviews prima: " + quanteRev);

        quanteRev = gr.neo4jCountReviews("Spirit Island");

        System.out.println("Reviews dopo: " + quanteRev);



        InfoRate ra = new InfoRate();

        ra.setAuthor("Gaia4");
        ra.setVote(8);
        Timestamp tsra = new Timestamp(System.currentTimeMillis());
        ra.setTimestamp(tsra);
        ra.setGame("Spirit Island");
        Boolean ret;

        ret = ud.Neo4jAddRate(ra);

        if(ret)
        {
            System.out.println("Ho aggiunto il rate!!");
        }
        else
            System.out.println("Già ha votato questo gioco " + ra.getAuthor() + "!!!");


        System.out.println("Rates prima: " + quantiRat);

        quantiRat = gr.neo4jCountRates("Spirit Island");

        System.out.println("Rates dopo: " + quantiRat);

        float avgRate = gr.neo4jAvgRates("Spirit Island");

        System.out.println("Media rating: " + avgRate);






        InfoReview rev2 = new InfoReview();
        rev2.setAuthor("sara");
        rev2.setText("Gioco di merda");
        Timestamp tsrev2 = Timestamp.valueOf("2021-01-02 17:38:12.893");
        rev2.setTimestamp(tsrev2);
        ud.Neo4jDeleteReview(rev2);

        InfoGroup group = new InfoGroup();
        group.setAdmin("Gaia5");
        group.setDescription("carino e tutto");
        group.setTimestamp(new Timestamp(System.currentTimeMillis()));
        group.setGame("Spirit Island");
        group.setName("Gruppo mio");

        ud.Neo4jAddGroup(group);

        GroupsPostsDBController gp = new GroupsPostsDBController();
        System.out.println("Gruppi di cui sei admin: ");
        gp.neo4jShowUsersGroups("sara", "admin");
        System.out.println("Gruppi di cui sei solo membro: ");
        gp.neo4jShowUsersGroups("sara", "member");


        System.out.println("Membri del gruppo: ");
        gp.neo4jShowGroupsMembers("Gruppo Mio2", "sara");
        gp.neo4jCountGroupMembers("Gruppo Mio2", "sara");

        System.out.println("Aggiungo membro!");
        Boolean retAddMember = false;
        retAddMember = ud.Neo4jAddGroupMember("Gaia4", "Gruppo Mio2", "sara");
        if(retAddMember)
        {
            System.out.println("Aggiunto membro!");
            gp.neo4jShowGroupsMembers("Gruppo Mio2", "sara");
        }

        else
            System.out.println("Membro già presente!");

        Timestamp ret = gp.neo4jTimestampLastPost("Gruppo Mio2", "sara");
        System.out.println(ret.toString());*/

        FindSuggestUsersDBController fs = new FindSuggestUsersDBController();
        System.out.println(fs.neo4jListUsersFriends("sara"));
        Neo4jDBManager.close();





    }
}
