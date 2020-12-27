package org.openjfx.Controller;

import static org.openjfx.DBManager.Neo4jDBManager.SignUpDBManager.InitializeDriver;

public class Neo4jDBController {

    public Neo4jDBController(){
        InitializeDriver();
    }

    /*public void registerNewUser(){
        registerUser(u);
    }*/

}
