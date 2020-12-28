package org.openjfx.Controller;
import org.openjfx.DBManager.Neo4jDBManager.LoginSignUpDBManager;
import org.openjfx.Entities.User;

import java.util.logging.Logger;

public class LoginSignUpDBController {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    public LoginSignUpDBController() {
        LoginSignUpDBManager.InitializeDriver();
    }

    public void neo4jRegisterUserController(User user) {

        // varie cose che aggiustano la roba
        int registration;
        String username = user.getUsername();
        String password = user.getPassword();
        String category1 = user.getCategory1();
        String category2 = user.getCategory2();
        int age = user.getAge();
        String role = user.getRole();
        registration = LoginSignUpDBManager.registerUser(username, password, category1, category2, age, role);

        if(registration == 1)
        {
            System.err.println("Esiste utente con lo stesso username!");
        }
        else
            System.out.println("Utente creato!");

    }

    public void neo4jLoginUserController(String username, String password) {

        // varie cose che aggiustano la roba
        int login;
        login = LoginSignUpDBManager.loginUser(username, password);

        if(login == 1)
        {
            System.err.println("Login effettuato con successo!");
        }
        else
            System.out.println("Pass o username non corretti!");
    }

}
