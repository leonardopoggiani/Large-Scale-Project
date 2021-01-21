package it.unipi.dii.lsmdb.project.group5.bean;

import java.sql.Timestamp;

public class LikeBean {

    private String type;
    private String author;
    private Timestamp timestamp;
    private int id;


    public LikeBean(String type, String author, Timestamp timestamp, int id)
    {
        this.type = type;
        this.author = author;
        this. timestamp = timestamp;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getAuthor() {
        return author;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "InfoLike{" +
                "type='" + type + '\'' +
                ", author='" + author + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", id+ " + id +
                '}';
    }
}
