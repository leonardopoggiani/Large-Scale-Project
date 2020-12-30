package org.openjfx.Entities;

public class InfoComment {

    private String text;
    private String author;
    private String timestamp;
    private String authorArt;
    private String titleArt;

    public InfoComment() {
    }

    public InfoComment(String text, String author, String timestamp, String authorArt, String titleArt)
    {
        this.text = text;
        this.author = author;
        this. timestamp = timestamp;
        this.authorArt = authorArt;
        this.titleArt = titleArt;
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

    public void setAuthorArt(String authorArt) {
        this.authorArt = authorArt;
    }

    public String getTitleArt() {
        return titleArt;
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


