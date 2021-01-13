package it.unipi.dii.LSMDB.project.group5.bean;

import java.sql.Timestamp;
//Credo servano per lo più al moderator
public class StatisticsInfluencer {
    private String username;
    private int howManyLikes;
    private String type;
    private int howManyArticles;
    private int howManyGames;
    private int howManyCategories;
    private Timestamp date1;
    private Timestamp date2;

    public StatisticsInfluencer(){};

    public void setUsername(String username) {
        this.username = username;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHowManyLikes(int howManyLikes) {
        this.howManyLikes = howManyLikes;
    }

    public void setHowManyArticles(int howManyArticles) {
        this.howManyArticles = howManyArticles;
    }

    public void setHowManyGames(int howManyGames) {
        this.howManyGames = howManyGames;
    }

    public void setHowManyCategories(int howManyCategories) {
        this.howManyCategories = howManyCategories;
    }

    public void setDate1(Timestamp date1) {
        this.date1 = date1;
    }

    public void setDate2(Timestamp date2) {
        this.date2 = date2;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public int getHowManyLikes() {
        return howManyLikes;
    }

    public int getHowManyArticles() {
        return howManyArticles;
    }

    public int getHowManyCategories() {
        return howManyCategories;
    }

    public int getHowManyGames() {
        return howManyGames;
    }

    public Timestamp getDate1() {
        return date1;
    }

    public Timestamp getDate2() {
        return date2;
    }

    @Override
    public String toString() {
        return "StatisticsInfluencer{" +
                "username='" + username + '\'' +
                ", howManyLikes=" + howManyLikes +
                ", type='" + type + '\'' +
                ", howManyArticles=" + howManyArticles +
                ", howManyGames=" + howManyGames +
                ", howManyCategories=" + howManyCategories +
                ", date1=" + date1 +
                ", date2=" + date2 +
                '}';
    }
}
