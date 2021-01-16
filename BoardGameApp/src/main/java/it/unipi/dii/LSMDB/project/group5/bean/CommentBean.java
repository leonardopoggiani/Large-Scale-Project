package it.unipi.dii.LSMDB.project.group5.bean;

import java.sql.Timestamp;

public class CommentBean {

    private String text;
    private String author;
    private Timestamp timestamp;
    private String authorArt;
    private String titleArt;
    private int id;

    public CommentBean() { }

    public CommentBean(String text, String author, Timestamp timestamp, String authorArt, String titleArt)
    {

        this.text = text;
        this.author = author;
        this. timestamp = timestamp;
        this.authorArt = authorArt;
        this.titleArt = titleArt;
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

    public void setAuthorArt(String authorArt) {
        this.authorArt = authorArt;
    }

    public String getTitleArt() {
        return titleArt;
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

    public void setTitleArt(String titleArt) {
        this.titleArt = titleArt;
    }

    public String getAuthorArt() {
        return authorArt;
    }

    @Override
    public String toString() {
        return "InfoComment{" +
                "text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", authorArt='" + authorArt + '\'' +
                ", titleArt='" + titleArt + '\'' +
                '}';
    }
}


