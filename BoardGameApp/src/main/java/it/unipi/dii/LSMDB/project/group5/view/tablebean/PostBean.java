package it.unipi.dii.LSMDB.project.group5.view.tablebean;

import java.sql.Timestamp;

public class PostBean {
    private String author;
    private String text;
    private Timestamp timestamp;
    private  String group;
    private String admin;

    public void setGroup(String group) {
        this.group = group;
    }

    public void setTimestamp(Timestamp timestamp) {
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

    public Timestamp getTimestamp() {
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
