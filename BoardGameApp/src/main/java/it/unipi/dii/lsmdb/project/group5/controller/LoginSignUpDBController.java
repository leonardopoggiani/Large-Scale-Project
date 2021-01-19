package it.unipi.dii.lsmdb.project.group5.controller;
import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.LoginSignupDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.UserDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.LoginSignUpDBManager;

import java.util.logging.Logger;

public class LoginSignUpDBController {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public LoginSignUpDBController() {
    }



    public boolean registerUser(UserBean user) {


        if(LoginSignupDBManager.signup(user))
        {
            int registrationNeo4j = LoginSignUpDBManager.registerUser(user);
            if(registrationNeo4j != 0)
            {
                logger.severe("NEO4J | Utente " + user.getUsername() + " non aggiunto in Neo4j!");
                UserDBManager.deleteUser(user.getUsername());
                logger.severe("MONGODB | Utente " + user.getUsername() + " eliminato da MongoDB!");
                return  false;
            }

            return true;
        }
        return false;
    }

    //NEO4J PRIMA, MONGODB DOPO

    public String loginUser(String username, String password) {

        String roleLogin = LoginSignupDBManager.loginUser(username, password);

        if(roleLogin != null)
        {
            System.out.println("Login effettuato con successo! Role: " + roleLogin);
            LoginSignupDBManager.updateLogin(username);
        }
        else {
            logger.info("Password o username non corretti!");
            System.err.println("Pass o username non corretti! Role: " + roleLogin);
        }

        System.out.println(roleLogin);
        return roleLogin;
    }

}
