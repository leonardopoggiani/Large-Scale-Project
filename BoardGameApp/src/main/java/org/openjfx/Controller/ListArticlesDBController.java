package org.openjfx.Controller;

import org.openjfx.DBManager.Neo4jDBManager.ListSuggArticlesDBManager;
import org.openjfx.DBManager.Neo4jDBManager.LoginSignUpDBManager;
import org.openjfx.Entities.Article;

import java.util.List;
import java.util.logging.Logger;

public class ListArticlesDBController {
    Logger logger =  Logger.getLogger(this.getClass().getName());

    public ListArticlesDBController() {
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
            }

        }
        return articles;

    }
}
