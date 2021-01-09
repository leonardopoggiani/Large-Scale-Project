package org.openjfx.Controller;

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
        if (ret){
            org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumComments(1, newComm.getAuthorArt(), newComm.getTitleArt());

        }
        return ret;

    }


    public int Neo4jAddLike(InfoLike like) {

        int ret = 0;
        ret = UpdateDatabaseDBManager.addLike(like);

        return ret;

    }

    public Boolean Neo4jDeleteComment(InfoComment comm) {

       Boolean ret = false;
        ret  = UpdateDatabaseDBManager.deleteComment(comm);
        if (ret){
            org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumComments(-1, comm.getAuthorArt(), comm.getTitleArt());

        }
        return ret;

    }

    public Boolean Neo4jAddReview(InfoReview newRev) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addReview(newRev);
        if(ret){
            org.openjfx.DBManager.MongoDBManager.GameDBManager.updateNumReviews(1, newRev.getGame());
        }


        return ret;

    }

    //Add rate se non esiste già un voto fatto dalla stessa persona sullo stesso gioco
    public Boolean Neo4jAddRating(InfoRate newRate) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addRating(newRate);
        if (ret){
            org.openjfx.DBManager.MongoDBManager.GameDBManager.updateRating(newRate.getVote(), newRate.getGame());
        }


        return ret;
    }


    public Boolean Neo4jDeleteReview(InfoReview rev) {

        Boolean ret = false;
        ret  = UpdateDatabaseDBManager.deleteReview(rev);
        if(ret){
            org.openjfx.DBManager.MongoDBManager.GameDBManager.updateNumReviews(-11, rev.getGame());
        }
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
