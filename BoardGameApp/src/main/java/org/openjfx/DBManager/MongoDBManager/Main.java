package org.openjfx.DBManager.MongoDBManager;

import org.openjfx.Entities.*;

public class Main {
    public static void main (String[] args){
        MongoDBManager.createConnection();
        System.out.println("Connesso");

        //signup
        User u = new User("gaia2", "gaia", "anastasi", 22, "italy"); //Da definire
        //SignupLoginDBManager.signup(u);

        //update login
        //SignupLoginDBManager.updateLogin("gaia2");

        //Read article
        InfoGame g = org.openjfx.DBManager.MongoDBManager.GameDBManager.readGame("Monopoli");
        /*System.out.println(g.getAvgRating() + " " + g.getMaxAge() + " " + g.getMinAge() + " " +g.getMinPlayers() + " " +
        g.getMaxPlayers() + " " +g.getNumReviews() + " " + g.getYear() + " " + g.getCategory1() + " " +g.getCategory2() + " " + g.getImageUrl() +g.getMaxTime()
        +" " +g.getMinTime() +" " +g.getName() + " " + g.getUrl());*/

        MongoDBManager.close();

    }
}
