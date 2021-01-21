package it.unipi.dii.lsmdb.project.group5.controller;
import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.LoginSignupDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.UserDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.UsersDBManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginSignUpDBController {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public LoginSignUpDBController() {
    }

    public boolean registerUser(UserBean user) {


        if(LoginSignupDBManager.signup(user))
        {
            int registrationNeo4j = UsersDBManager.registerUser(user);
            if(registrationNeo4j != 0)
            {
                logger.log(Level.SEVERE,"NEO4J | Utente " + user.getUsername() + " non aggiunto in Neo4j!");
                UserDBManager.deleteUser(user.getUsername());
                logger.log(Level.SEVERE,"MONGODB | Utente " + user.getUsername() + " eliminato da MongoDB!");
                return  false;
            }

            return true;
        }
        return false;
    }

    public String loginUser(String username, String password) {

        String roleLogin = LoginSignupDBManager.loginUser(username, password);

        if(roleLogin != null)
        {
            logger.log(Level.INFO,"Role: " + roleLogin);
            LoginSignupDBManager.updateLogin(username);
        }
        else {
            logger.log(Level.INFO,"Can't login with these credentials!");
        }

        return roleLogin;
    }

}
