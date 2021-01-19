package it.unipi.dii.lsmdb.project.group5.view.tablebean;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

public class TableGroupBean {
    SimpleStringProperty groupName;
    SimpleStringProperty timestamp;
    SimpleStringProperty timestampLastPost;
    SimpleStringProperty admin;
    SimpleStringProperty game;
    SimpleIntegerProperty members;

    public TableGroupBean(String name, String time,String lastPost, String admin, String game, int members){
        this.groupName = new SimpleStringProperty(name);
        this.timestamp = new SimpleStringProperty(String.valueOf(time));
        this.admin = new SimpleStringProperty(admin);
        this.game = new SimpleStringProperty(game);
        this.members = new SimpleIntegerProperty(members);
        this.timestampLastPost = new SimpleStringProperty(String.valueOf(lastPost));
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

    public void updateTimestamp() {timestamp = new SimpleStringProperty(String.valueOf(new Timestamp(System.currentTimeMillis())));}

    public int getMembers() { return members.get(); }

    public String toString(){
        return getGroupName() + " | " + getAdmin() + " | " + getTimestamp() + " | " + getGame();
    }

}
