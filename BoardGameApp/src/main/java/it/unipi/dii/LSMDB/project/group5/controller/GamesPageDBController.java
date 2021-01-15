package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import it.unipi.dii.LSMDB.project.group5.bean.RatingBean;
import it.unipi.dii.LSMDB.project.group5.bean.ReviewBean;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.GameDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.GamesDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.RatingsDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.ReviewsDBManager;

import java.util.List;

public class GamesPageDBController {
    public GamesPageDBController() {}

    //ONLY NEO4J
    public List<GameBean> listSuggestedGames(String username, int limit) {

        return GamesDBManager.searchSuggestedGames(username, limit);
    }

    public List<ReviewBean> listGamesReviews(String name, int limit) {

        return ReviewsDBManager.searchListReviews(name, limit);
    }





    //PRIMA NEO4J, POI MONGODB
    public boolean addReview(ReviewBean newRev) {

        boolean ret = ReviewsDBManager.addReview(newRev);
        if(ret){
            GameDBManager.updateNumReviews(1, newRev.getGame());
        }

        return ret;

    }


    public boolean addRating(RatingBean newRating) {

        boolean ret = RatingsDBManager.addRating(newRating);
        if (ret){
            GameDBManager.updateRating(newRating.getVote(), newRating.getGame());
        }
        return ret;
    }


    public boolean deleteReview(ReviewBean rev) {

        boolean ret  = ReviewsDBManager.deleteReview(rev);
        if(ret){
            GameDBManager.updateNumReviews(-1, rev.getGame());
        }
        return ret;
    }



    //ONLY MONGODB
    public double getAvgRating(String game){
        return GameDBManager.getAvgRating(game);
    }

    public GameBean showGame (String game){
        return GameDBManager.readGame(game);
    }

    public List<GameBean> filterByName (String game){
        return GameDBManager.filterByName(game);
    }

    public List<GameBean> filterByCategory(String category){
        return GameDBManager.filterByCategory(category);
    }

    public List<GameBean> filterByPlayers(int players){
        return GameDBManager.filterByPlayers(players);
    }

    public List<GameBean> filterByYear(int year){
        return GameDBManager.filterByYear(year);
    }

    public List<GameBean> orderByAvgRating(){
        return GameDBManager.orderBy("avgRating");
    }

    public List<GameBean> orderByNumReviews(){
        return GameDBManager.orderBy("reviews");
    }

    public List<GameBean> orderByNumVotes(){
        return GameDBManager.orderBy("numVotes");
    }



    /*
    public int countReviews(String name) {
        return countReviews(name);
    }


    public int countRatings(String name) {

        return RatingsDBManager.countRatings(name);
    }


    public double avgRatings(String name) {

        return RatingsDBManager.avgRatings(name);


    }*/


}
