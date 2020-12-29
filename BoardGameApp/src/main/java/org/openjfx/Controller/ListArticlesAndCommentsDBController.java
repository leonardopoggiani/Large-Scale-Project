package org.openjfx.Controller;

import org.openjfx.DBManager.MongoDBManager.MongoDBManager;
import org.openjfx.DBManager.MongoDBManager.SignupLoginDBManager;
import org.openjfx.DBManager.Neo4jDBManager.ListArticlesCommentDBManager;
import org.openjfx.DBManager.Neo4jDBManager.ListSuggArticlesDBManager;
import org.openjfx.DBManager.Neo4jDBManager.LoginSignUpDBManager;
import org.openjfx.Entities.Article;
import org.openjfx.Entities.Comment;

import java.util.List;
import java.util.logging.Logger;

public class ListArticlesAndCommentsDBController {
    Logger logger =  Logger.getLogger(this.getClass().getName());

    public ListArticlesAndCommentsDBController() {
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

    public List<Comment> neo4jListArticlesComment(String title, String author) {

        List<Comment> comments ;
        comments = ListArticlesCommentDBManager.searchListComments(title, author);

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
        quantiLike = ListArticlesCommentDBManager.countLikes(title, author, type);

        System.out.println(quantiLike);

        return quantiLike;

    }
}
