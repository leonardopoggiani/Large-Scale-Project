package org.openjfx.Controller;

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
                for(int j=0;j<articles.get(i).getComments().size();j++){
                    System.out.println(articles.get(j).toString());
                }
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
                for(int j=0;j< comments.size();j++){
                    System.out.println(comments.get(j).toString());
                }
            }

        return comments;

    }
}
