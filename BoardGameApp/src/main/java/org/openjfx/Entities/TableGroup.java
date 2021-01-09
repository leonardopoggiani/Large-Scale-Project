package org.openjfx.Entities;

import java.sql.Timestamp;

public class TableGroup {
    String groupName;
    Timestamp timestamp;
    String admin;
    String game;

    public TableGroup(String name, Timestamp time, String admin, String game){
        this.groupName = name; this.timestamp = time; this.admin = admin; this.game = game;
    }
}
