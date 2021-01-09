package org.openjfx.Controller;

import org.openjfx.DBManager.Neo4jDBManager.ArticlesCommentsLikesDBManager;
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
        int tot = ArticlesCommentsLikesDBManager.countComments(newComm.getTitleArt(), newComm.getAuthorArt());
        org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumComments(tot, newComm.getAuthorArt(), newComm.getTitleArt());
        return ret;

    }


    public Boolean Neo4jAddLike(InfoLike like) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addLike(like);
        int tot = ArticlesCommentsLikesDBManager.countLikes(like.getTitleArt(), like.getAuthorArt(), like.getType());
        org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumComments(tot, like.getAuthorArt(), like.getTitleArt());
        return ret;

    }

    public Boolean Neo4jDeleteComment(InfoComment comm) {

       Boolean ret = false;
        ret  = UpdateDatabaseDBManager.deleteComment(comm);
        int tot = ArticlesCommentsLikesDBManager.countComments(comm.getTitleArt(), comm.getAuthorArt());
        org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumComments(tot, comm.getAuthorArt(), comm.getTitleArt());
        return ret;

    }

    public Boolean Neo4jAddReview(InfoReview newRev) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addReview(newRev);
        int tot = GamesReviewsRatesDBManager.countReviews(newRev.getGame());
        org.openjfx.DBManager.MongoDBManager.GameDBManager.updateNumReviews(tot, newRev.getGame());

        return ret;

    }

    //Add rate se non esiste già un voto fatto dalla stessa persona sullo stesso gioco
    public Boolean Neo4jAddRate(InfoRate newRate) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addRate(newRate);
        int tot = GamesReviewsRatesDBManager.countRatings(newRate.getGame());
        org.openjfx.DBManager.MongoDBManager.GameDBManager.updateNumVotes(tot, newRate.getGame());
        double avg_rate = GamesReviewsRatesDBManager.avgRatings(newRate.getGame());
        org.openjfx.DBManager.MongoDBManager.GameDBManager.updateAvgRating(avg_rate, newRate.getGame());

        return ret;
    }


    public Boolean Neo4jDeleteReview(InfoReview rev) {

        Boolean ret = false;
        ret  = UpdateDatabaseDBManager.deleteReview(rev);
        int tot = GamesReviewsRatesDBManager.countReviews(rev.getGame());
        org.openjfx.DBManager.MongoDBManager.GameDBManager.updateNumReviews(tot, rev.getGame());
        return ret;
    }

    public Boolean Neo4jAddGroup(InfoGroup newGroup) {

        Boolean ret = false;
        ret  = UpdateDatabaseDBManager.addGroup(newGroup);

        return ret;
    }


    public Boolean Neo4jDeleteGroup(InfoGroup group) {

        Boolean ret = false;
        ret  = UpdateDatabaseDBManager.deleteGroup(group);
        return ret;
    }

    public Boolean Neo4jAddGroupMember(String username, String name, String admin) {

        Boolean ret = false;
        ret  = UpdateDatabaseDBManager.addGroupMember(username, name, admin);

        return ret;
    }



}
