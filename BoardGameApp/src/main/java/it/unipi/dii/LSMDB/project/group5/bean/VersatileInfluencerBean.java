package it.unipi.dii.LSMDB.project.group5.bean;

public class VersatileInfluencerBean {
    private String username;
    private int numGames;

    public VersatileInfluencerBean(){};

    public void setNumGames(int numGames) {
        this.numGames = numGames;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getNumGames() {
        return numGames;
    }
}
