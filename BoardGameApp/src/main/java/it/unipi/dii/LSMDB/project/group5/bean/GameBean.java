package it.unipi.dii.LSMDB.project.group5.bean;

import java.util.List;

public class GameBean {
    private String name;
    private int year;
    private String category1;
    private String category2;
    private List<String> publisher;
    private String url;
    private String imageUrl;
    private String rules;
    private int minPlayers;
    private int maxPlayers;
    private int minAge;
    private int maxAge;
    private String minTime;
    private String maxTime;
    private int numReviews;
    private double avgRating;
    private String description;
    private boolean cooperative;
    private List<String> expansion;
    private int numVotes;
    private double complexity;
    List<String> listCategory;

    //Non so se servono per ora le lascio qua
    //private List<InfoReview> reviews;

    public GameBean() { }

    public GameBean(String name, String category1, String category2) {
        this.name = name;
        this.category1 = category1;
        this.category2 = category2;
    }

    public GameBean(String name, double rating, int votes) {
        this.name = name;
        this.avgRating = rating;
        this.numVotes = votes;
    }

    public List<String> getListCategory() {
        return listCategory;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public int getYear() {
        return year;
    }

    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public List<String> getPublisher() {
        return publisher;
    }

    public String getRules() {
        return rules;
    }

    public String getUrl() {
        return url;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public String getMinTime() {
        return minTime;
    }

    public boolean isCooperative() {
        return cooperative;
    }

    public double getComplexity() {
        return complexity;
    }

    public int getNumVotes() {
        return numVotes;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getExpansion() {
        return expansion;
    }

    public void setListCategory(List<String> listCategory) {
        this.listCategory = listCategory;
    }

    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }

    public void setCooperative(boolean cooperative) {
        this.cooperative = cooperative;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExpansion(List<String> expansion) {
        this.expansion = expansion;
    }

    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    public void setPublisher(List<String> publisher) {
        this.publisher = publisher;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "InfoGame{" +
                "name='" + name + '\'' +
                ", category1='" + category1 + '\'' +
                ", category2='" + category2 + '\'' +
                ", avg_rating='" + avgRating + '\'' +
                '}';
    }
}
