package org.openjfx.Entities;

import java.sql.Timestamp;

public class RateBean {
    private String author;
    private double vote;
    private String game;
    private Timestamp timestamp;

    public RateBean(){};

    public RateBean(String author, double vote, String game, Timestamp timestamp)
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

    public double getVote() {
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
