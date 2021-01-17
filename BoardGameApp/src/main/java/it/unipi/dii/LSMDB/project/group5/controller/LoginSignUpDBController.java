package it.unipi.dii.LSMDB.project.group5.controller;
import it.unipi.dii.LSMDB.project.group5.bean.UserBean;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.UserDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.LoginSignUpDBManager;

import java.util.logging.Logger;

public class LoginSignUpDBController {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public LoginSignUpDBController() {
    }

    //MONGODB PRIMA, NEO4J DOPO

    public boolean registerUser(UserBean user) {


        if(UserDBManager.signup(user))
        {
            int registrationNeo4j = LoginSignUpDBManager.registerUser(user);
            if(registrationNeo4j == 0)
                return true;
        }
        return false;
    }

    //NEO4J PRIMA, MONGODB DOPO
    //TODO
    public String loginUser(String username, String password) {

        String roleLogin = LoginSignUpDBManager.loginUser(username, password);

        if(roleLogin != "NA")
        {
            System.out.println("Login effettuato con successo! Role: " + roleLogin);
            UserDBManager.updateLogin(username);
        }
        else {
            logger.info("Password o username non corretti!");
            System.err.println("Pass o username non corretti! Role: " + roleLogin);
        }

        return roleLogin;
    }

}
