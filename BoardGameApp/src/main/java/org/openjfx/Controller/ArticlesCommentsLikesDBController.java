package org.openjfx.Controller;

import org.openjfx.DBManager.MongoDBManager.MongoDBManager;
import org.openjfx.DBManager.Neo4jDBManager.ArticlesCommentsLikesDBManager;
import org.openjfx.DBManager.Neo4jDBManager.ListSuggArticlesDBManager;
import org.openjfx.DBManager.Neo4jDBManager.LoginSignUpDBManager;
import org.openjfx.Entities.Article;
import org.openjfx.Entities.Comment;

import java.util.List;
import java.util.logging.Logger;

public class ArticlesCommentsLikesDBController {
    Logger logger =  Logger.getLogger(this.getClass().getName());

    public ArticlesCommentsLikesDBController() {
        LoginSignUpDBManager.InitializeDriver();
        MongoDBManager.createConnection();
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
    public List<Comment> neo4jListArticlesComment(String title, String author) {

        List<Comment> comments ;
        comments = ArticlesCommentsLikesDBManager.searchListComments(title, author);

        if(comments.isEmpty())
        {
            System.err.println("Niente!");
        }
        else {
            for(int i=0;i<comments.size();i++){
                System.out.println(comments.get(i).toString());

            }

        }
        return comments;

    }

    //Restituisce un articolo intero con testo da mongoDB

    public Article mongoDBshowArticle(String title, String author) {

        Article article = new Article();
        article = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.readArticle(author, title);

        System.out.println(article.toString());

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
}
