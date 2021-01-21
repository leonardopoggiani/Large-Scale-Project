package it.unipi.dii.lsmdb.project.group5.bean;

public class CategoryBean {
    private String name;
    private int totGames;
    private int numRatesTot;
    private double avgRatingTot;

    public void setName(String name) {
        this.name = name;
    }

    public void setAvgRatingTot(double avgRatingTot) {
        this.avgRatingTot = avgRatingTot;
    }

    public void setNumRatesTot(int numRatesTot) {
        this.numRatesTot = numRatesTot;
    }

    public void setTotGames(int totGames) {
        this.totGames = totGames;
    }

    public int getNumRatesTot() {
        return numRatesTot;
    }

    public int getTotGames() {
        return totGames;
    }

    public String getName() {
        return name;
    }

    public double getAvgRatingTot() {
        return avgRatingTot;
    }

    @Override
    public String toString() {
        return name + ", " + totGames + ", " + numRatesTot + ", " + avgRatingTot;
    }
}
