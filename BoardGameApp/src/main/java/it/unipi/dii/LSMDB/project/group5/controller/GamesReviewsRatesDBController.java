package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import it.unipi.dii.LSMDB.project.group5.bean.ReviewBean;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.GameDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.GamesDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.RatingsDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.ReviewsDBManager;

import java.util.List;

public class GamesReviewsRatesDBController {
    public GamesReviewsRatesDBController() {
        //LoginSignUpDBManager.InitializeDriver();
        //MongoDBManager.createConnection();
    }

    public List<GameBean> neo4jListSuggestedGames(String username) {

        List<GameBean> games ;
        games = GamesDBManager.searchSuggestedGames(username);

        if(games.isEmpty())
        {
            System.err.println("Niente!");
        }
        else {
            for(int i=0;i<games.size();i++){
                System.out.println(games.get(i).toString());
                //InfoGame g = games.get(i);

                /*for(int j=0;j<games.get(i).getGroups().size();j++){
                    System.out.println(games.get(i).getGroups().get(j).toString());
                }*/
            }

        }
        return games;

    }

    public int neo4jCountReviews(String name) {

        int quanteReviews = 0;
        quanteReviews = ReviewsDBManager.countReviews(name);


        System.out.println(quanteReviews);

        return quanteReviews;

    }

    public int neo4jCountRatings(String name) {

        int quantiRates = 0;
        quantiRates = RatingsDBManager.countRatings(name);

        System.out.println(quantiRates);

        return quantiRates;

    }

    public double neo4jAvgRatings(String name) {

        double avgRates = 0;
        avgRates = RatingsDBManager.avgRatings(name);

        System.out.println(avgRates);

        return avgRates;

    }




    // tutte le reviews di un gioco

    public List<ReviewBean> neo4jListGamesReviews(String name, int quante) {

        List<ReviewBean> infoReviews;
        infoReviews = ReviewsDBManager.searchListReviews(name, quante);

        if(infoReviews.isEmpty())
        {
            System.err.println("Niente!");
        }
        else {
            for(int i = 0; i< infoReviews.size(); i++){
                System.out.println(infoReviews.get(i).toString());

            }

        }
        return infoReviews;

    }



    public GameBean showGame (String game){
        GameBean g = GameDBManager.readGame(game);
        return g;
    }

    public List<GameBean> filterByName (String game){
        List<GameBean> list = GameDBManager.filterByName(game);
        return list;
    }

    public List<GameBean> filterByCategory(String category){
        List<GameBean> list = GameDBManager.filterByCategory(category);
        return list;
    }

    public List<GameBean> filterByPlayers(int players){
        List<GameBean> list = GameDBManager.filterByPlayers(players);
        return list;
    }

    public List<GameBean> filterByYear(int year){
        List<GameBean> list = GameDBManager.filterByYear(year);
        return list;
    }

    public List<GameBean> orderByAvgRating(){
        List<GameBean> list = GameDBManager.orderBy("avgRating");
        return list;
    }

    public List<GameBean> orderByNumReviews(){
        List<GameBean> list = GameDBManager.orderBy("reviews");
        return list;
    }

    public List<GameBean> orderByNumVotes(){
        List<GameBean> list = GameDBManager.orderBy("numVotes");
        return list;
    }


}
