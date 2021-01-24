package it.unipi.dii.lsmdb.project.group5.controller;


import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.UserDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.UsersDBManager;

import java.util.List;

public class UsersPagesDBController {


    public UsersPagesDBController(){}

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

        if(UsersDBManager.deleteUser(username))
        {
            if(!UserDBManager.deleteUser(username))
            {
                Logger.warning("MONGODB | Utente " + username +" non eliminato in MongoDB!");
                return false;
            }

            return  true;
        }
        Logger.warning("NEO4J | Utente " + username +" non eliminato in NEO4J!");
        return  false;
    }



    //NEO4J, POI MONGODB
    public  boolean promoteDemoteUser(String username, String role)
    {
        if(UsersDBManager.promoteDemoteUser(username, role))
        {
            if(!UserDBManager.promoteDemoteUser(username, role))
            {
                Logger.warning("MONGODB | Ruolo dell'utente " + username +" non aggiornato!");
                return false;
            }
            return  true;
        }
        Logger.warning("NEO4J | Ruolo dell'utente " + username +" non aggiornato!");
        return  false;
    }

    public List<UserBean> showAllUsers() {
        return UserDBManager.showAllUsers();
    }

    public List<UserBean> showAllInfluencer() {
        return UserDBManager.showAllInfluencer();
    }

    public boolean modifyProfile(String username, String name, String surname, String password, String age, String categoria1, String categoria2) {
        return UserDBManager.modifyProfile(username,name,surname,password,age) && UsersDBManager.modifyProfile(categoria1,categoria2);
    }
}
