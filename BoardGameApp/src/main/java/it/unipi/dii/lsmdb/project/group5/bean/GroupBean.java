package it.unipi.dii.lsmdb.project.group5.bean;

import java.sql.Timestamp;
import java.util.List;

public class GroupBean {
    private String name;
    private Timestamp timestamp;
    private String admin;
    private String description;
    private String game;
    private Timestamp lastPost;

    public GroupBean() {}

    public GroupBean(String name, Timestamp timestamp, String admin, String description, String game)
    {
        this.name = name;
        this.timestamp = timestamp;
        this.admin = admin;
        this.description = description;
        this.game = game;
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

    public String getGame() {
        return game;
    }

    public Timestamp getLastPost() {
        return (lastPost == null) ? new Timestamp(System.currentTimeMillis()) : lastPost;
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
                '}';
    }
}
