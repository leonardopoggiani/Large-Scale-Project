package org.openjfx.DBManager.Neo4jDBManager;

import org.openjfx.Entities.User;

import static org.openjfx.DBManager.Neo4jDBManager.Neo4jDBManager.InitializeDriver;
import static org.openjfx.DBManager.Neo4jDBManager.SignUpDBManager.registerUser;

public class Main {

    public static void main( String[] args ) throws Exception {
        InitializeDriver();
        User u = new User("Clarissa", "Cards Game", "Strategic", 28, "normalUser");
        registerUser(u);
        System.out.println("---------------------------");

    }
}
