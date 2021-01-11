package org.openjfx.Entities;

public class CountryBean {
    String country;
    private int numUser;

    public String getCountry() {
        return country;
    }

    public int getNumUser() {
        return numUser;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setNumUser(int numUser) {
        this.numUser = numUser;
    }
}
