package it.unipi.dii.lsmdb.project.group5.bean;

public class VersatileUser {
    private String username;
    private int howManyCategories;

    public VersatileUser(){};


    public void setUsername(String username) {
        this.username = username;
    }

    public void setHowManyCategories(int howManyCategories) {
        this.howManyCategories = howManyCategories;
    }


    public String getUsername() {
        return username;
    }

    public int getHowManyCategories() {
        return howManyCategories;
    }


    @Override
    public String toString() {
        return "VersatileUser{" +
                "username='" + username + '\'' +
                ", howManyCategories=" + howManyCategories +
                '}';
    }
}
