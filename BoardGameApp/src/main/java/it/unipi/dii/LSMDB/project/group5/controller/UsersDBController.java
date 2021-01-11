package it.unipi.dii.LSMDB.project.group5.controller;


import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.UsersDBManager;

import java.util.List;

public class UsersDBController {
    public UsersDBController(){};

    public  List<String> neo4jListUsers(String username, String type)
    {
        List<String> friends = UsersDBManager.listUsers(username, type);
        return  friends;
    }

    public  List<String> neo4jListSuggestingFollowing(String username, String type)
    {
        List<String> suggestedFollowing = UsersDBManager.listSuggestedFollowing(username, type);
        return  suggestedFollowing;
    }
}
