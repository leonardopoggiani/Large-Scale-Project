package it.unipi.dii.lsmdb.project.group5.controller;

import it.unipi.dii.lsmdb.project.group5.bean.GameBean;
import it.unipi.dii.lsmdb.project.group5.bean.RatingBean;
import it.unipi.dii.lsmdb.project.group5.bean.ReviewBean;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.GameDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.GamesDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.RatingsDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.ReviewsDBManager;

import java.util.List;

public class GamesPagesDBController {
    public GamesPagesDBController() {}

    public List<GameBean> listSuggestedGames(String username, int limit) {

        return GamesDBManager.searchSuggestedGames(username, limit);
    }

    public List<ReviewBean> listGamesReviews(String name, int limit) {

        return ReviewsDBManager.searchListReviews(name, limit);
    }


    public boolean addGame(GameBean newGame) {

        if(GameDBManager.addGame(newGame)){
            if(!GamesDBManager.addGame(newGame))
            {
                Logger.warning( "NEO4J | Gioco " + newGame.getName() + " non aggiunto!");
                GameDBManager.deleteGame(newGame.getName());
                Logger.warning("MONGODB | Gioco " + newGame.getName() + " rimosso da mongoDB!");
                return  false;
            }
            return true;
        }
        Logger.warning("MONGODB | Gioco " + newGame.getName() + " non aggiunto in mongoDB!");
        return false;
    }

    public boolean addReview(ReviewBean newRev) {

        boolean ret = ReviewsDBManager.addReview(newRev);
        if(ret){
            if(!GameDBManager.updateNumReviews(1, newRev.getGame()))
            {
                Logger.warning("MONGODB | Numero reviews di " + newRev.getGame() +" non incrementato!");
                return false;
            }
            return true;
        }
        Logger.warning("NEO4j | Review al gioco " + newRev.getGame() +" non aggiunta!");
        return ret;
    }


    public boolean addRating(RatingBean newRating) {

        boolean ret = RatingsDBManager.addRating(newRating);
        if (ret){
            if(!GameDBManager.updateRating(newRating.getVote(), newRating.getGame()))
            {
                Logger.warning("MONGODB | Numero di rating di "+ newRating.getGame() +" non aggiornato!");
                return false;
            }
            return  true;
        }
        Logger.warning("NEO4j | Rating al gioco " + newRating.getGame() +" non aggiunto!");
        return ret;
    }


    public boolean deleteGame(String game) {

      if(GamesDBManager.deleteGame(game))
      {
          if(!GameDBManager.deleteGame(game))
          {
              Logger.warning("MONGODB | Game " + game + " non eliminato!");
              return false;
          }
          return true;
      }
      Logger.warning("NEO4J | Game " + game + " non eliminato da mongoDB!");
      return false;
    }

    public boolean deleteReview(ReviewBean review) {
        if(ReviewsDBManager.deleteReview(review))
        {
            if(!GameDBManager.updateNumReviews(-1, review.getGame()))
            {
                Logger.warning("MONGODB | num_reviews al gioco " + review.getGame() + " non decrementato!");
                return false;
            }
            return true;
        }
        Logger.warning("NEO4J | Review al gioco " + review.getGame() + " non eliminata!");
        return false;
    }

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

    public List<GameBean> showAllGames() {
        return  GameDBManager.showAllGames();
    }

}
