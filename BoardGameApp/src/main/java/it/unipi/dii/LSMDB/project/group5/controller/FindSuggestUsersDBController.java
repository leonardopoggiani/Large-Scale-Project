package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.FindSuggestUsersDBManager;

import java.util.List;

public class FindSuggestUsersDBController {
    public FindSuggestUsersDBController(){};

    public  List<String> neo4jListUsersFriends(String username)
    {
        List<String> friends = FindSuggestUsersDBManager.listUsersFriends(username);
        return  friends;
    }
}
