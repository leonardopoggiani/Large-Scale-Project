package it.unipi.dii.lsmdb.project.group5.bean;

public class InfluencerInfoBean {
    private String influencer;
    private int count;

    public int getCount() {
        return count;
    }

    public String getInfluencer() {
        return influencer;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setInfluencer(String influencer) {
        this.influencer = influencer;
    }

    @Override
    public String toString() {
        return influencer + ", " + count;
    }
}
