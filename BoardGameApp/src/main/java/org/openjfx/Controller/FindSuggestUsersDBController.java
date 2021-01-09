package org.openjfx.Controller;

import org.openjfx.DBManager.Neo4jDBManager.FindSuggestUsersDBManager;

import java.util.List;

public class FindSuggestUsersDBController {
    public FindSuggestUsersDBController(){};

    public  List<String> neo4jListUsersFriends(String username)
    {
        List<String> friends = FindSuggestUsersDBManager.listUsersFriends(username);
        return  friends;
    }
}
