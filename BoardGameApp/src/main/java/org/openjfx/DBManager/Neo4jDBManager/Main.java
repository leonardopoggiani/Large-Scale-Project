package org.openjfx.DBManager.Neo4jDBManager;

import static org.openjfx.DBManager.Neo4jDBManager.Neo4jDBManager.InitializeDriver;
import static org.openjfx.DBManager.Neo4jDBManager.SignUpDBManager.registerUser;

public class Main {

    public static void main( String[] args ) throws Exception {
        InitializeDriver();
        registerUser("Clarissa", "Cards Game", "Strategic", 28, "normalUser");
        System.out.println("---------------------------");

    }
}
