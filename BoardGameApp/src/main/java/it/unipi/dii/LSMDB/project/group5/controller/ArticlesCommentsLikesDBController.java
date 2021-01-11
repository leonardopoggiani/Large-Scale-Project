package it.unipi.dii.LSMDB.project.group5.controller;

import com.google.common.collect.Lists;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.ArticleDBManager;
import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.bean.CommentBean;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.ArticlesCommentsLikesDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.ListSuggArticlesDBManager;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

public class ArticlesCommentsLikesDBController {
    Logger logger =  Logger.getLogger(this.getClass().getName());

    public List<ArticleBean> neo4jListSuggestedArticles(String username) {

        List<ArticleBean> articles ;
        articles = ArticlesDBManager.searchSuggestedArticles(username);

        if(articles.isEmpty())
        {
            System.err.println("Niente!");
        }
        else {
            for (ArticleBean article : articles) {
                System.out.println(article.toString());
                ArticleBean a = article;

                /*for(int j=0;j<articles.get(i).getComments().size();j++){
                    System.out.println(articles.get(i).getComments().get(j).toString());
                }*/
            }

        }

        return articles;

    }

    // tutti i commenti di un articolo
    public List<CommentBean> neo4jListArticlesComment(String title, String author) {

        List<CommentBean> infoComments;
        infoComments = CommentsDBManager.searchListComments(title, author);

        if(infoComments.isEmpty())
        {
            System.err.println("Niente!");
        }
        else {
            for(int i = 0; i< infoComments.size(); i++){
                System.out.println(infoComments.get(i).toString());

            }

        }
        return infoComments;

    }

    //Restituisce un articolo intero con testo da mongoDB

    public ArticleBean mongoDBshowArticle(String title, String author) {

        ArticleBean article;
        article = ArticleDBManager.readArticle(author, title);

        //System.out.println(article.toString());

        return article;

    }

    public int neo4jCountLikes(String title, String author, String type) {

        int quantiLike = 0;
        quantiLike = LikesDBManager.countLikes(title, author, type);

        System.out.println(quantiLike);

        return quantiLike;

    }

    public int neo4jCountComments(String title, String author) {

        int quantiComments = 0;
        quantiComments = CommentsDBManager.countComments(title, author);

        System.out.println(quantiComments);

        return quantiComments;

    }

    public List<ArticleBean> filterByInfluencer(String influencer){
        List<ArticleBean> list = ArticleDBManager.filterByInfluencer(influencer);
        return list;
    }

    public List<ArticleBean> filterByGame(String game){
        List<ArticleBean> list = ArticleDBManager.filterByGame(game);
        return list;
    }

    public List<ArticleBean> filterByDate(String date){
        return ArticleDBManager.filterByDate(date);
    }

    public List<ArticleBean> orderByLikes (){
        List<ArticleBean> list = ArticleDBManager.orderBy("like");
        return list;
    }

    public List<ArticleBean> orderByDislikes (){
        List<ArticleBean> list = ArticleDBManager.orderBy("dislike");
        return list;
    }

    public List<ArticleBean> orderByComments (){
        List<ArticleBean> list = ArticleDBManager.orderBy("comments");
        return list;
    }

    public Boolean addArticle(ArticleBean a)
    {
        Boolean ret = false;
        ret = ArticlesDBManager.addArticle(a);
        System.out.println(ret);
        return  ret;
    }
}
