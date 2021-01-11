package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.GroupsPostsDBManager;
import it.unipi.dii.LSMDB.project.group5.bean.GroupBean;

import java.sql.Timestamp;
import java.util.List;

public class GroupsPostsDBController {

    public GroupsPostsDBController(){};

    public List<GroupBean> neo4jShowUsersGroups(String username, String type) {

        List<GroupBean> groups ;
        groups = GroupsPostsDBManager.showUsersGroups(username, type);

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

    public List<String> neo4jShowGroupsMembers(String name, String admin) {

        List<String> members ;
        members = GroupsPostsDBManager.showGroupsMembers(name, admin);

        if(members.isEmpty())
        {
            System.err.println("Niente!");
        }
        else {
            for(int i=0;i<members.size();i++){
                System.out.println(members.get(i));

            }

        }
        return members;

    }

    public int neo4jCountGroupMembers(String name, String admin) {

        int numMembers = 0;
        numMembers = GroupsPostsDBManager.countGroupsMembers(name, admin);

        System.out.println(numMembers);

        return numMembers;

    }

    public Timestamp neo4jTimestampLastPost(String name, String admin) {

        return GroupsPostsDBManager.timestampLastPost(name, admin);

    }

    //Timestamp dell'ultimo post pubblicato su un gruppo
    //Fare funzione a parte e chiamrla all'interno della di usersGroups in entrambi i casi
    //Poi se se possono cancellare viene mostrato altrimenti no


}
