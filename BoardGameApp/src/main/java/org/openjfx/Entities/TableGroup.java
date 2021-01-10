package org.openjfx.Entities;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

public class TableGroup {
    SimpleStringProperty groupName;
    SimpleStringProperty timestamp;
    SimpleStringProperty admin;
    SimpleStringProperty game;

    public TableGroup(String name, Timestamp time, String admin, String game){
        this.groupName = new SimpleStringProperty(name);
        this.timestamp = new SimpleStringProperty(String.valueOf(time));
        this.admin = new SimpleStringProperty(admin);
        this.game = new SimpleStringProperty(game);
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

    public String toString(){
        return getGroupName() + " | " + getAdmin() + " | " + getTimestamp() + " | " + getGame();
    }

}
