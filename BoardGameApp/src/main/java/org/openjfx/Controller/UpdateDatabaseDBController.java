package org.openjfx.Controller;

import org.openjfx.DBManager.Neo4jDBManager.GamesReviewsRatesDBManager;
import org.openjfx.DBManager.Neo4jDBManager.UpdateDatabaseDBManager;
import org.openjfx.Entities.*;

import java.util.logging.Logger;

public class UpdateDatabaseDBController {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    public  UpdateDatabaseDBController() {
        //LoginSignUpDBManager.InitializeDriver();
        //MongoDBManager.createConnection();
    }

    public Boolean Neo4jAddComment(InfoComment newComm) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addComment(newComm);

        return ret;

    }


    public Boolean Neo4jAddLike(InfoLike like) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addLike(like);

        return ret;

    }

    public Boolean Neo4jDeleteComment(InfoComment comm) {

       Boolean ret = false;
        ret  = UpdateDatabaseDBManager.deleteComment(comm);

        return ret;

    }

    public Boolean Neo4jAddReview(InfoReview newRev) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addReview(newRev);
        int tot = GamesReviewsRatesDBManager.countReviews(newRev.getGame());
        org.openjfx.DBManager.MongoDBManager.GameDBManager.updateAvgRating(tot, newRev.getGame());

        return ret;

    }

    //Add rate se non esiste già un voto fatto dalla stessa persona sullo stesso gioco
    public Boolean Neo4jAddRate(InfoRate newRate) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addRate(newRate);
        double avg_rate = GamesReviewsRatesDBManager.avgRates(newRate.getGame());
        org.openjfx.DBManager.MongoDBManager.GameDBManager.updateAvgRating(avg_rate, newRate.getGame());


        return ret;

    }


    public Boolean Neo4jDeleteReview(InfoReview rev) {

        Boolean ret = false;
        ret  = UpdateDatabaseDBManager.deleteReview(rev);

        return ret;

    }

    public Boolean Neo4jAddGroup(InfoGroup newGroup) {

        Boolean ret = false;
        ret  = UpdateDatabaseDBManager.addGroup(newGroup);

        return ret;

    }


}
