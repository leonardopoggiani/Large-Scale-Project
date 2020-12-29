package org.openjfx.DBManager.MongoDBManager;

import org.openjfx.Entities.*;

public class Main {
    public static void main (String[] args){
        MongoDBManager.createConnection();
        System.out.println("Connesso");

        //signup
        User u = new User("gaia2", "gaia", "anastasi", 22, "italy"); //Da definire
        SignupLoginDBManager.signup(u);

        //update login
        //SignupLoginDBManager.updateLogin("gaia2");

        //Read article
        Article a = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.readArticle("Clarissa1", "Mio articolo bello");
        System.out.println(a.getAuthor() +" , " + a.getTitle() + " , " +a.getTimestamp() +" , " + a.getText());

        MongoDBManager.close();

    }
}
