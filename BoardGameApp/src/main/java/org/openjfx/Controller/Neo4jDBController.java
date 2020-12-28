package org.openjfx.Controller;

import org.openjfx.DBManager.Neo4jDBManager.SignUpDBManager;
import org.openjfx.Entities.User;

public class Neo4jDBController {

    public void registerUserController(User user) {

        SignUpDBManager.registerUser(user);

        System.out.println("Creazione utente!");
    }

}
