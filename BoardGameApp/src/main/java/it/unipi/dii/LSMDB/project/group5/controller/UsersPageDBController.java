package it.unipi.dii.LSMDB.project.group5.controller;


import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.UsersDBManager;

import java.util.List;

public class UsersPageDBController {
    public UsersPageDBController(){};

    public  List<String> listUsers(String username, String type)
    {
       return UsersDBManager.listUsers(username, type);

    }

    public  List<String> listSuggestingFollowing(String username, String type)
    {
        return UsersDBManager.listSuggestedFollowing(username, type);

    }

    public  boolean addRemoveFollow(String username1, String username2, String type)
    {
        return  UsersDBManager.addRemoveFollow(username1,username2,type);
    }
}
