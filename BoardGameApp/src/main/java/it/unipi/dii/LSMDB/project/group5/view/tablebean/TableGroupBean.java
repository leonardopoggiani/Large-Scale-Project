package it.unipi.dii.LSMDB.project.group5.view.tablebean;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

public class TableGroupBean {
    SimpleStringProperty groupName;
    SimpleStringProperty timestamp;
    SimpleStringProperty admin;
    SimpleStringProperty game;
    SimpleIntegerProperty members;
    SimpleIntegerProperty numberposts;

    public TableGroupBean(String name, Timestamp time, String admin, String game, int members, int posts){
        this.groupName = new SimpleStringProperty(name);
        this.timestamp = new SimpleStringProperty(String.valueOf(time));
        this.admin = new SimpleStringProperty(admin);
        this.game = new SimpleStringProperty(game);
        this.members = new SimpleIntegerProperty(members);
        this.numberposts = new SimpleIntegerProperty(posts);
    }

    public String getGroupName() {
        return groupName.get();
    }

    public String getGame() {
        return game.get();
    }

    public String getAdmin() {
        return admin.get();
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public int getNumberPosts() { return numberposts.get(); }

    public void updateNumberPost() { numberposts = new SimpleIntegerProperty(numberposts.get() + 1); }

    public void updateTimestamp() {timestamp = new SimpleStringProperty(String.valueOf(new Timestamp(System.currentTimeMillis())));}

    public int getMembers() { return members.get(); }

    public String toString(){
        return getGroupName() + " | " + getAdmin() + " | " + getTimestamp() + " | " + getGame();
    }

}
