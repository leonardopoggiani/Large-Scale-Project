package org.openjfx.Entities;

import java.sql.Timestamp;
import java.util.List;

public class InfoGroup {
    private String name;
    private Timestamp timestamp;
    private String admin;
    private String description;
    private String game;
    private Timestamp lastPost;
    //Non so se serve
    private List<User> members;

    public InfoGroup(){};

    public InfoGroup(String name, Timestamp timestamp, String admin, String description, String game)
    {
        this.name = name;
        this.timestamp = timestamp;
        this.admin = admin;
        this.description = description;
        this.game = game;
        this.members = null;
    }

    public String getName() {
        return name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getAdmin() {
        return admin;
    }

    public String getDescription() {
        return description;
    }

    public List<User> getMembers() {
        return members;
    }

    public String getGame() {
        return game;
    }

    public Timestamp getLastPost() {
        return lastPost;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setLastPost(Timestamp lastPost) {
        this.lastPost = lastPost;
    }

    @Override
    public String toString() {
        return "InfoGroup{" +
                "name='" + name + '\'' +
                ", timestamp=" + timestamp +
                ", admin='" + admin + '\'' +
                ", description='" + description + '\'' +
                ", game='" + game + '\'' +
                ", lastPost=" + lastPost +
                ", members=" + members +
                '}';
    }
}
