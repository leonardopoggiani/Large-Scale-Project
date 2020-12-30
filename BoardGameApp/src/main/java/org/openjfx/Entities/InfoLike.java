package org.openjfx.Entities;

public class InfoLike {

    private String type;
    private String author;
    private String timestamp;
    private String authorArt;
    private String titleArt;

    public void InfoLike(String type, String author, String timestamp, String authorArt, String titleArt)
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

    public String getTimestamp() {
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

    public void setTimestamp(String timestamp) {
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
