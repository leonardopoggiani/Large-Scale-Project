package org.openjfx.Controller;

import org.openjfx.DBManager.MongoDBManager.MongoDBManager;
import org.openjfx.DBManager.Neo4jDBManager.GamesReviewsRatesDBManager;
import org.openjfx.DBManager.Neo4jDBManager.ListSuggGamesDBManager;
import org.openjfx.DBManager.Neo4jDBManager.LoginSignUpDBManager;
import org.openjfx.Entities.InfoGame;
import org.openjfx.Entities.InfoReview;

import java.util.List;

public class GamesReviewsRatesDBController {
    public GamesReviewsRatesDBController() {
        LoginSignUpDBManager.InitializeDriver();
        MongoDBManager.createConnection();
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

    public int neo4jAvgRates(String name) {

        int avgRates = 0;
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
}
