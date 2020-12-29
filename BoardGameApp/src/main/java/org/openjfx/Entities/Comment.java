package org.openjfx.Entities;

public class Comment {

    private String text;
    private String author;
    private String timestamp;

    public Comment() {
    }

    public void Comment(String text, String author, String timestamp)
    {
        this.text = text;
        this.author = author;
        this. timestamp = timestamp;
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

    public String getTimestamp() {
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
        return "Comment{" +
                "text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}


