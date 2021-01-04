package org.openjfx.Controller;

import org.bson.Document;
import org.openjfx.DBManager.MongoDBManager.ArticleDBManager;
import org.openjfx.DBManager.Neo4jDBManager.ArticlesCommentsLikesDBManager;
import org.openjfx.DBManager.Neo4jDBManager.ListSuggArticlesDBManager;
import org.openjfx.Entities.Article;
import org.openjfx.Entities.InfoComment;

import java.util.List;
import java.util.logging.Logger;

public class ArticlesCommentsLikesDBController {
    Logger logger =  Logger.getLogger(this.getClass().getName());

    public ArticlesCommentsLikesDBController() {
        //LoginSignUpDBManager.InitializeDriver();
        //MongoDBManager.createConnection();
    }

    public List<Article> neo4jListSuggestedArticles(String username) {

        List<Article> articles ;
        articles = ListSuggArticlesDBManager.searchSuggestedArticles(username);

        if(articles.isEmpty())
        {
            System.err.println("Niente!");
        }
        else {
            for(int i=0;i<articles.size();i++){
                System.out.println(articles.get(i).toString());
                Article a = articles.get(i);

                /*for(int j=0;j<articles.get(i).getComments().size();j++){
                    System.out.println(articles.get(i).getComments().get(j).toString());
                }*/
            }

        }
        return articles;

    }

    // tutti i commenti di un articolo
    public List<InfoComment> neo4jListArticlesComment(String title, String author) {

        List<InfoComment> infoComments;
        infoComments = ArticlesCommentsLikesDBManager.searchListComments(title, author);

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

    public Article mongoDBshowArticle(String title, String author) {

        Article article = new Article();
        article = ArticleDBManager.readArticle(author, title);

        //System.out.println(article.toString());

        return article;

    }

    public int neo4jCountLikes(String title, String author, String type) {

        int quantiLike = 0;
        quantiLike = ArticlesCommentsLikesDBManager.countLikes(title, author, type);

        System.out.println(quantiLike);

        return quantiLike;

    }

    public int neo4jCountComments(String title, String author) {

        int quantiComments = 0;
        quantiComments = ArticlesCommentsLikesDBManager.countComments(title, author);

        System.out.println(quantiComments);

        return quantiComments;

    }

    public List<Document> filterByInfluencer(String influencer){
        List<Document> list = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.filterByInfluencer(influencer);
        return list;
    }

    public List<Document> filterByGame(String game){
        List<Document> list = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.filterByGame(game);
        return list;
    }

    public List<Document> filterByDate(String date){
        List<Document> list = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.filterByDate(date);
        return list;
    }
}
