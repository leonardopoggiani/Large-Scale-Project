package it.unipi.dii.lsmdb.project.group5.bean;

public class ActivityBean {
    private String date;
    private int numUser;
    private double avgLogin;
    private String country;

    public void setCountry(String country) {
        this.country = country;
    }

    public double getAvgLogin() {
        return avgLogin;
    }

    public String getCountry() {
        return country;
    }

    public void setAvgLogin(double avgLogin) {
        this.avgLogin = avgLogin;
    }

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

    @Override
    public String toString() {
        return date + ", " + numUser + ", " + avgLogin + ", " + country;
    }
}
