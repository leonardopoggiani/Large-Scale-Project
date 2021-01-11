package org.openjfx.Controller;

import org.openjfx.DBManager.Neo4jDBManager.UsersDBManager;

import java.util.List;

public class UsersDBController {
    public UsersDBController(){};

    public  List<String> neo4jListUsersFriends(String username, String type)
    {
        List<String> friends = UsersDBManager.listUsers(username, type);
        return  friends;
    }

    public  List<String> neo4jListSuggestingFollowing(String username)
    {
        List<String> suggestedFollowing = UsersDBManager.listSuggestedFollowing(username);
        return  suggestedFollowing;
    }
}
