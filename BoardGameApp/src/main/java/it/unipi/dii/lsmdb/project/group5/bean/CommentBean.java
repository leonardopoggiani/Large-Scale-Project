package it.unipi.dii.lsmdb.project.group5.bean;

import java.sql.Timestamp;

public class CommentBean {

    private String text;
    private String author;
    private Timestamp timestamp;
    private int id;

    public CommentBean() { }

    public CommentBean(String text, String author, Timestamp timestamp, int id)
    {

        this.text = text;
        this.author = author;
        this. timestamp = timestamp;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "InfoComment{" +
                "text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}


