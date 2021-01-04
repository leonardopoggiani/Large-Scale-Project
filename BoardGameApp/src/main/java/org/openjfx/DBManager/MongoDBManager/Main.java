package org.openjfx.DBManager.MongoDBManager;

import org.bson.Document;
import org.openjfx.Entities.*;

import java.util.*;

public class Main {
    public static void main (String[] args){
        //MongoDBManager.createConnection();
        System.out.println("Connesso");

        //signup
        User u = new User("gaia2", "gaia", "anastasi", 22, "italy"); //Da definire
        //SignupLoginDBManager.signup(u);

        //update login
        //SignupLoginDBManager.updateLogin("gaia2");

        //Read game
        //InfoGame g = org.openjfx.DBManager.MongoDBManager.GameDBManager.readGame("Monopoli");
        /*System.out.println(g.getAvgRating() + " " + g.getMaxAge() + " " + g.getMinAge() + " " +g.getMinPlayers() + " " +
        g.getMaxPlayers() + " " +g.getNumReviews() + " " + g.getYear() + " " + g.getCategory1() + " " +g.getCategory2() + " " + g.getImageUrl() +g.getMaxTime()
        +" " +g.getMinTime() +" " +g.getName() + " " + g.getUrl());*/

        //Filter article
        //List<Document> list = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.filterByInfluencer("gaia1");
        //List<Document> list = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.filterByGame("Cluedo");
        //List<Document> list = org.openjfx.DBManager.MongoDBManager.ArticleDBManager.filterByDate("2020-12-29");
        //List<Document> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.filterByName("Monopoli");
        //List<Document> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.filterByCategory("Economy");
        //List<Document> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.filterByPlayers(10);
        //List<Document> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.filterByYear(1960);
        List<Document> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.orderBy("reviews");


        MongoDBManager.close();

    }
}
