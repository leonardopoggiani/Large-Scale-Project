package it.unipi.dii.LSMDB.project.group5.bean;

import java.sql.Timestamp;
//Credo servano per lo più al moderator
public class VersatileUser {
    private String username;
    private int howManyCategories;
    private String type;
    private String role;
    private Timestamp date1;

    public VersatileUser(){};


    public void setUsername(String username) {
        this.username = username;
    }

    public void setHowManyCategories(int howManyCategories) {
        this.howManyCategories = howManyCategories;
    }

    public void setDate1(Timestamp date1) {
        this.date1 = date1;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public int getHowManyCategories() {
        return howManyCategories;
    }

    public Timestamp getDate1() {
        return date1;
    }

    public String getType() {
        return type;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "VersatileUser{" +
                "username='" + username + '\'' +
                ", howManyCategories=" + howManyCategories +
                ", type='" + type + '\'' +
                ", role='" + role + '\'' +
                ", date1=" + date1 +
                '}';
    }
}
