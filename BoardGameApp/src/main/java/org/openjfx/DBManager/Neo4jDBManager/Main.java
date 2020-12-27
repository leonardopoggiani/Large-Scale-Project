package org.openjfx.DBManager.Neo4jDBManager;

import org.openjfx.Controller.Neo4jDBController;
import org.openjfx.Entities.User;

public class Main {

    public static void main( String[] args ) throws Exception {
        Neo4jDBManager.InitializeDriver();
        Neo4jDBController cont = new Neo4jDBController();
        User user = new User("Clarissa", "Cards Game", "Strategic", 28, "normalUser");

        cont.registerUserController(user);
        System.out.println("---------------------------");

    }
}
