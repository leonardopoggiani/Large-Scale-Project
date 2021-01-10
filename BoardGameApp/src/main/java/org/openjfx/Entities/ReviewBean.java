package org.openjfx.Entities;

import java.sql.Timestamp;

public class ReviewBean {
    private String text;
    private String game;
    private String author;
    private Timestamp timestamp;

    public ReviewBean(){};

    public ReviewBean(String text, String game, String author, Timestamp timestamp) {
        this.text = text;
        this.game = game;
        this.author = author;
        this.timestamp = timestamp;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public String getGame() {
        return game;
    }

    public String getAuthor() {
        return author;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "InfoReview{" +
                "text='" + text + '\'' +
                ", game='" + game + '\'' +
                ", author='" + author + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
