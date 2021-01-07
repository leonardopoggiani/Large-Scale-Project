package org.openjfx.Controller;

import org.bson.Document;
import org.openjfx.DBManager.MongoDBManager.GameDBManager;
import org.openjfx.DBManager.Neo4jDBManager.GamesReviewsRatesDBManager;
import org.openjfx.DBManager.Neo4jDBManager.ListSuggGamesDBManager;
import org.openjfx.Entities.Article;
import org.openjfx.Entities.InfoGame;
import org.openjfx.Entities.InfoRate;
import org.openjfx.Entities.InfoReview;

import java.lang.annotation.Documented;
import java.util.List;

public class GamesReviewsRatesDBController {
    public GamesReviewsRatesDBController() {
        //LoginSignUpDBManager.InitializeDriver();
        //MongoDBManager.createConnection();
    }

    public List<InfoGame> neo4jListSuggestedGames(String username) {

        List<InfoGame> games ;
        games = ListSuggGamesDBManager.searchSuggestedGames(username);

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
        quanteReviews = GamesReviewsRatesDBManager.countReviews(name);


        System.out.println(quanteReviews);

        return quanteReviews;

    }

    public int neo4jCountRates(String name) {

        int quantiRates = 0;
        quantiRates = GamesReviewsRatesDBManager.countRates(name);

        System.out.println(quantiRates);

        return quantiRates;

    }

    public float neo4jAvgRates(String name) {

        float avgRates = 0;
        avgRates = GamesReviewsRatesDBManager.avgRates(name);

        System.out.println(avgRates);

        return avgRates;

    }


    // tutte le reviews di un gioco

    public List<InfoReview> neo4jListGamesReviews(String name) {

        List<InfoReview> infoReviews;
        infoReviews = GamesReviewsRatesDBManager.searchListReviews(name);

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

    public InfoGame showGame (String game){
        InfoGame g = GameDBManager.readGame(game);
        return g;
    }

    public List<InfoGame> filterByName (String game){
        List<InfoGame> list = GameDBManager.filterByName(game);
        return list;
    }

    public List<InfoGame> filterByCategory(String category){
        List<InfoGame> list = GameDBManager.filterByCategory(category);
        return list;
    }

    public List<InfoGame> filterByPlayers(int players){
        List<InfoGame> list = GameDBManager.filterByPlayers(players);
        return list;
    }

    public List<InfoGame> filterByYear(int year){
        List<InfoGame> list = GameDBManager.filterByYear(year);
        return list;
    }

    public List<InfoGame> orderByAvgRating(){
        List<InfoGame> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.orderBy("avgRating");
        return list;
    }

    public List<InfoGame> orderByNumReviews(){
        List<InfoGame> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.orderBy("reviews");
        return list;
    }

    public List<InfoGame> orderByNumVotes(){
        List<InfoGame> list = org.openjfx.DBManager.MongoDBManager.GameDBManager.orderBy("numVotes");
        return list;
    }


}
