package org.openjfx.DBManager.MongoDBManager;

import org.openjfx.Entities.*;

import org.openjfx.DBManager.Neo4jDBManager.SignUpDBManager;

public class Main {
    public static void main (String[] args){
        MongoDBManager.createConnection();
        System.out.println("Connesso");

        //signup
        User u = new User("gaia1", "gaia", "anastasi", 22, "italy"); //Da definire
        org.openjfx.DBManager.MongoDBManager.SignUpDBManager.signup(u);

        MongoDBManager.close();

    }
}
