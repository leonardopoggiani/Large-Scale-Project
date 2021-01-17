package it.unipi.dii.LSMDB.project.group5.bean;

//Credo servano per lo più al moderator
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
