package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.bean.GroupBean;
import it.unipi.dii.LSMDB.project.group5.bean.PostBean;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.GroupsPostsDBManager;

import java.util.List;
import java.util.logging.Logger;

public class GroupsPagesDBController {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    public GroupsPagesDBController(){};

    //ONLY NEO4J

    public List<GroupBean> showUsersGroups(String username, String type) {

        return GroupsPostsDBManager.showUsersGroups(username, type);

    }

    public List<String> showGroupsMembers(String name, String admin) {

        return GroupsPostsDBManager.showGroupsMembers(name, admin);

    }

    public int countGroupMembers(String name, String admin) {

        return GroupsPostsDBManager.countGroupsMembers(name, admin);

    }

    public List<PostBean> showGroupsPost(String name, String admin, int limit) {

        return GroupsPostsDBManager.showGroupsPosts(name, admin, limit);

    }


    public boolean addGroup(GroupBean newGroup) {
        return GroupsPostsDBManager.addGroup(newGroup);
    }


    public boolean deleteGroup(String delGroup, String delAdmin) {
        return GroupsPostsDBManager.deleteGroup(delGroup, delAdmin);

    }


    public boolean addDeleteGroupMember(String username, String name, String admin, String type) {
        return GroupsPostsDBManager.addDeleteGroupMember(username, name, admin, type);

    }


    public boolean addDeletePost(PostBean post, String type) {
        return   GroupsPostsDBManager.addDeletePost(post, type);

    }



}
