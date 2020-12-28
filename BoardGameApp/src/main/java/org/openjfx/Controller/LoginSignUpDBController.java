package org.openjfx.Controller;
import org.openjfx.DBManager.Neo4jDBManager.SignUpDBManager;
import org.openjfx.Entities.User;

import java.util.logging.Logger;

public class LoginSignUpDBController {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    public void neo4jRegisterUserController(User user) {

        // varie cose che aggiustano la roba
        String username = user.getUsername();
        String password = user.getPassword();
        String category1 = user.getCategory1();
        String category2 = user.getCategory2();
        int age = user.getAge();
        String role = user.getRole();
        SignUpDBManager.registerUser(username, password, category1, category2, age, role);

       logger.info("Utente registrato");
    }



}
