package org.openjfx.Controller;

import org.openjfx.DBManager.Neo4jDBManager.GroupsPostsDBManager;
import org.openjfx.Entities.InfoGroup;

import java.util.List;

public class GroupsPostsDBController {

    public GroupsPostsDBController(){};

    public List<InfoGroup> neo4jShowUsersGroups(String username) {

        List<InfoGroup> groups ;
        groups = GroupsPostsDBManager.showUsersGroups(username);

        if(groups.isEmpty())
        {
            System.err.println("Niente!");
        }
        else {
            for(int i=0;i<groups.size();i++){
                System.out.println(groups.get(i).toString());
                //InfoGame g = games.get(i);

                /*for(int j=0;j<games.get(i).getGroups().size();j++){
                    System.out.println(games.get(i).getGroups().get(j).toString());
                }*/
            }

        }
        return groups;

    }
}
