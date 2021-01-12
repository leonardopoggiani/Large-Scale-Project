package it.unipi.dii.LSMDB.project.group5.bean;

public class ActivityBean {
    private String date;
    private int numUser;

    public void setNumUser(int numUser) {
        this.numUser = numUser;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumUser() {
        return numUser;
    }

    public String getDate() {
        return date;
    }
}
