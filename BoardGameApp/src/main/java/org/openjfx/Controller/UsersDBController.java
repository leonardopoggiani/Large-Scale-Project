package org.openjfx.Controller;

import org.openjfx.DBManager.Neo4jDBManager.UsersDBManager;

import java.util.List;

public class UsersDBController {
    public UsersDBController(){};

    public  List<String> neo4jListUsersFriends(String username)
    {
        List<String> friends = UsersDBManager.listUsersFriends(username);
        return  friends;
    }
}
