package it.unipi.dii.lsmdb.project.group5.controller;
import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.LoginSignupDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.UserDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.UsersDBManager;

public class LoginSignUpDBController {

    public LoginSignUpDBController() {
    }

    public boolean registerUser(UserBean user) {

        if(LoginSignupDBManager.signup(user))
        {
            int registrationNeo4j = UsersDBManager.registerUser(user);
            if(registrationNeo4j != 0)
            {
                Logger.log("NEO4J | Utente " + user.getUsername() + " non aggiunto in Neo4j!");
                UserDBManager.deleteUser(user.getUsername());
                Logger.log("MONGODB | Utente " + user.getUsername() + " eliminato da MongoDB!");
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
            Logger.log("role: " + roleLogin);
            LoginSignupDBManager.updateLogin(username);
        }
        else {
            Logger.log("can't login with these credentials!");
        }

        return roleLogin;
    }

}
