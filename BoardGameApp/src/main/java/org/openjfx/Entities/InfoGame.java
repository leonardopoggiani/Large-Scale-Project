package org.openjfx.Entities;

import java.util.List;

public class InfoGame {

    private String name;
    private String category1;
    private String category2;
    private int numReviews;
    private int numRates;
    private int avgRates;
    //Non so se servono per ora le lascio qua
    private List<InfoReview> reviews;
    private List<InfoGroup> groups;

    public InfoGame() {
    }

    public InfoGame(String name, String category1, String category2) {
        this.name = name;
        this.category1 = category1;
        this.category2 = category2;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    @Override
    public String toString() {
        return "InfoGame{" +
                "name='" + name + '\'' +
                ", category1='" + category1 + '\'' +
                ", category2='" + category2 + '\'' +
                ", groups=" + groups +
                '}';
    }

    public String getName() {
        return name;
    }
}

