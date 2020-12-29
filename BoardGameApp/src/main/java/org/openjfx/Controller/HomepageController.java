package org.openjfx.Controller;

import org.openjfx.DBManager.Neo4jDBManager.ListSuggArticlesDBManager;
import org.openjfx.Entities.Article;
import org.openjfx.View.login;

import java.util.List;
import java.util.logging.Logger;

public class HomepageController {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    public HomepageController() {
        ListSuggArticlesDBManager.InitializeDriver();
    }

    public List<Article> showSuggestedArticlesController(String username) {
        List<Article> ret = ListSuggArticlesDBManager.searchSuggestedArticles(username);
        for (int i=0; i<ret.size(); i++){

        }
        return ret;
    }
}
