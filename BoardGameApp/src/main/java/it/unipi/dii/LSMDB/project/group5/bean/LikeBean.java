package it.unipi.dii.LSMDB.project.group5.bean;

import java.sql.Timestamp;

public class LikeBean {

    private String type;
    private String author;
    private Timestamp timestamp;
    private String authorArt;
    private String titleArt;

    public LikeBean(String type, String author, Timestamp timestamp, String authorArt, String titleArt)
    {
        this.type = type;
        this.author = author;
        this. timestamp = timestamp;
        this.authorArt = authorArt;
        this.titleArt = titleArt;
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

    public String getAuthorArt() {
        return authorArt;
    }

    public String getTitleArt() {
        return titleArt;
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

    public void setTitleArt(String titleArt) {
        this.titleArt = titleArt;
    }

    public void setAuthorArt(String authorArt) {
        this.authorArt = authorArt;
    }

    @Override
    public String toString() {
        return "InfoLike{" +
                "type='" + type + '\'' +
                ", author='" + author + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", authorArt='" + authorArt + '\'' +
                ", titleArt='" + titleArt + '\'' +
                '}';
    }
}
