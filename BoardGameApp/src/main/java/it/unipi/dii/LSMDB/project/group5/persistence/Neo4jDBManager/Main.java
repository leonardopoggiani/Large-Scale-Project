package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.PostBean;
import it.unipi.dii.LSMDB.project.group5.controller.*;

import java.sql.Timestamp;

public class Main {

    public static void main( String[] args ) throws Exception {

        Neo4jDBManager.InitializeDriver();



            GamesReviewsRatesDBController gr = new GamesReviewsRatesDBController();
            ArticlesCommentsLikesDBController artCon = new ArticlesCommentsLikesDBController();

            UpdateDatabaseDBController ud = new UpdateDatabaseDBController();

            GroupsPostsDBController grpos = new GroupsPostsDBController();

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
        /*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        long time = date.getTime();
        String dateString = new Timestamp(time).toString();*/
            //List<VersatileUser> statVer = AnalyticsDBManager.versatileUsers("influencer");
            //System.out.println("Fatto");
        /*List<ArticleBean> art = artCon.neo4jListSuggestedArticles("Gaia5");
        System.out.println(art);

        ArticleBean b = new ArticleBean("Ammazza che articolone","Leonardo1",new Timestamp(System.currentTimeMillis()),"Spirit Island");
        boolean ret = ArticlesDBManager.addArticle(b);
        System.out.println(ret);*/

        PostBean post = new PostBean();
        post.setGroup("Gruppo brutto");
        post.setAdmin("Gaia5");
        post.setAuthor("Gaia5");
        post.setTimestamp(new Timestamp(System.currentTimeMillis()));
        post.setText("Ci siete amici??");
        ud.Neo4jAddDeletePost(post, "add");

        PostBean post2 = new PostBean();
        post2.setGroup("Gruppo brutto");
        post2.setAdmin("Gaia5");
        post2.setAuthor("sara");
        post2.setTimestamp(new Timestamp(System.currentTimeMillis()));
        post2.setText("Ci siete amici miei??");
        //ud.Neo4jAddDeletePost(post, "add");

        //grpos.neo4jShowGroupsPost("Gruppo brutto", "Gaia5");

        /*ud.Neo4jAddDeleteGroupMember("Leonardo1", "Gruppo brutto", "Gaia5", "add");
        grpos.neo4jShowGroupsMembers("Gruppo brutto", "Gaia5");
        ud.Neo4jAddDeleteGroupMember("Leonardo1", "Gruppo brutto", "Gaia5", "delete");
        grpos.neo4jShowGroupsMembers("Gruppo brutto", "Gaia5");*/

        ud.Neo4jAddDeletePost(post2, "add");
        grpos.neo4jShowGroupsPost("Gruppo brutto", "Gaia5");
        ud.Neo4jAddDeletePost(post2, "delete");
        grpos.neo4jShowGroupsPost("Gruppo brutto", "Gaia5");
        Neo4jDBManager.close();





    }
}
