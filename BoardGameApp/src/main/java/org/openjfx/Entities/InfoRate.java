package org.openjfx.Entities;

import java.sql.Timestamp;

public class InfoRate {
    private String author;
    private int vote;
    private String game;
    private Timestamp timestamp;

    public InfoRate(){};

    public  InfoRate(String author, int vote, String game, Timestamp timestamp)
    {
        this.author = author;
        this.vote = vote;
        this.game = game;
        this.timestamp = timestamp;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public float getVote() {
        return vote;
    }

    public String getGame() {
        return game;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "InfoRate{" +
                "author='" + author + '\'' +
                ", vote=" + vote +
                ", game='" + game + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
