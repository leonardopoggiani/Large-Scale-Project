package it.unipi.dii.LSMDB.project.group5.bean;

import java.sql.Time;
import java.sql.Timestamp;

public class PostBean {
    private String author;
    private String text;
    private String timestamp;
    private  String group;
    private String admin;

    public PostBean() {}

    public PostBean(String author, String text, String timestamp, String group, String admin) {
        this.author = author; this.text = text; this.timestamp = timestamp; this.group = group; this.admin = admin;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGroup() {
        return group;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAdmin() {
        return admin;
    }

    @Override
    public String toString() {
        return "PostBean{" +
                "author='" + author + '\'' +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                ", group='" + group + '\'' +
                ", admin='" + admin + '\'' +
                '}';
    }
}
