package it.unipi.dii.LSMDB.project.group5.bean;

public class LikeInfluencer {
    private String username;
    private int howMany;
    private String type;

    public LikeInfluencer(){};

    public void setUsername(String username) {
        this.username = username;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHowMany(int howMany) {
        this.howMany = howMany;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public int getHowMany() {
        return howMany;
    }

    @Override
    public String toString() {
        return "LikeInfluencer{" +
                "username='" + username + '\'' +
                ", howMany=" + howMany +
                ", type='" + type + '\'' +
                '}';
    }
}
