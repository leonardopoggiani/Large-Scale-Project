package it.unipi.dii.lsmdb.project.group5.bean;

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

    @Override
    public String toString() {
        return country + ", " + numUser;
    }
}
