package org.openjfx.Controller;

import static org.openjfx.DBManager.Neo4jDBManager.InitializeDriver;
import static org.openjfx.DBManager.SignUpDBManager.registerUser;

public class Neo4jDBController {

    public Neo4jDBController(){
        InitializeDriver();
    }

    public void registerNewUser(){
        registerUser("Clarissa", "Cards Game", "Strategic", 28, "normalUser");
    }

}
