package it.unipi.dii.lsmdb.project.group5.controller;


import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.UserDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.UsersDBManager;

import java.util.List;
import java.util.logging.Logger;

public class UsersPagesDBController {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public UsersPagesDBController(){};

    //ONLY MONGODB
    public  List<String> listUsers(String username, String type)
    {
       return UsersDBManager.listUsers(username, type);

    }

    public  List<String> listSuggestingFollowing(String username, String type)
    {
        return UsersDBManager.listSuggestedFollowing(username, type);

    }

    public UserBean showUser(String text) {
        return UserDBManager.showUser(text);
    }

    public  boolean addRemoveFollow(String username1, String username2, String type)
    {
        return  UsersDBManager.addRemoveFollow(username1,username2,type);
    }


    public  boolean deleteUser(String username)
    {

        if(UserDBManager.deleteUser(username))
        {
            if(!UsersDBManager.deleteUser(username))
            {
                logger.severe("NEO4J | Utente " + username +" non eliminato in Neo4j!");
                return false;
            }
            return  true;
        }
        return  false;
    }



    //NEO4J, POI MONGODB
    public  boolean promoteDemoteUser(String username, String role)
    {
        if(UsersDBManager.promoteDemoteUser(username, role))
        {
            if(!UserDBManager.promoteDemoteUser(username, role))
            {
                logger.severe("MONGODB | Ruolo dell'utente " + username +" non aggiornato!");
                return false;
            }
            return  true;
        }
        return  false;
    }


    public List<UserBean> showAllUsers() {
        return UserDBManager.showAllUsers();
    }

    public List<UserBean> showAllInfluencer() {
        return UserDBManager.showAllInfluencer();
    }

    public boolean modifyProfile() {
        return UserDBManager.modifyProfile();
    }
}
