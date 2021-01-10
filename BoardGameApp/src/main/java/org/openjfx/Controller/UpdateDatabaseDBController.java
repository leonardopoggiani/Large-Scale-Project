package org.openjfx.Controller;

import org.openjfx.DBManager.MongoDBManager.GameDBManager;
import org.openjfx.DBManager.Neo4jDBManager.UpdateDatabaseDBManager;
import org.openjfx.Entities.*;

import java.util.logging.Logger;

public class UpdateDatabaseDBController {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    public  UpdateDatabaseDBController() {
        //LoginSignUpDBManager.InitializeDriver();
        //MongoDBManager.createConnection();
    }

    public Boolean Neo4jAddComment(CommentBean newComm) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addComment(newComm);
        if (ret){
            org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumComments(1, newComm.getAuthorArt(), newComm.getTitleArt());

        }
        return ret;

    }


    public int Neo4jAddLike(LikeBean like) {

        int ret = 0;
        ret = UpdateDatabaseDBManager.addLike(like);
        if(ret > 0){
            if (ret == 1){
                if(like.getType().equals("like")){
                    org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumLike(-1, like.getAuthorArt(), like.getTitleArt());
                }else {
                    org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumDislike(-1, like.getAuthorArt(), like.getTitleArt());

                }
            }else {
                if(like.getType().equals("like")){
                    org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumLike(1, like.getAuthorArt(), like.getTitleArt());
                }else {
                    org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumDislike(1, like.getAuthorArt(), like.getTitleArt());

                }
            }

        }


        return ret;

    }

    public Boolean Neo4jDeleteComment(CommentBean comm) {

       Boolean ret = false;
        ret  = UpdateDatabaseDBManager.deleteComment(comm);
        if (ret){
            org.openjfx.DBManager.MongoDBManager.ArticleDBManager.updateNumComments(-1, comm.getAuthorArt(), comm.getTitleArt());

        }
        return ret;

    }

    public Boolean Neo4jAddReview(ReviewBean newRev) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addReview(newRev);
        if(ret){
            org.openjfx.DBManager.MongoDBManager.GameDBManager.updateNumReviews(1, newRev.getGame());
        }


        return ret;

    }

    //Add rate se non esiste già un voto fatto dalla stessa persona sullo stesso gioco
    public Boolean Neo4jAddRating(RateBean newRate) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addRating(newRate);
        if (ret){
            GameDBManager.updateRating(newRate.getVote(), newRate.getGame());
        }
        return ret;
    }


    public Boolean Neo4jDeleteReview(ReviewBean rev) {

        Boolean ret = false;
        ret  = UpdateDatabaseDBManager.deleteReview(rev);
        if(ret){
            org.openjfx.DBManager.MongoDBManager.GameDBManager.updateNumReviews(-1, rev.getGame());
        }
        return ret;
    }

    public Boolean Neo4jAddGroup(GroupBean newGroup) {

        Boolean ret = false;
        ret  = UpdateDatabaseDBManager.addGroup(newGroup);

        return ret;
    }


    public Boolean Neo4jDeleteGroup(GroupBean group) {

        Boolean ret = false;
        ret  = UpdateDatabaseDBManager.deleteGroup(group);
        return ret;
    }

    public Boolean Neo4jAddGroupMember(String username, String name, String admin) {

        Boolean ret = false;
        ret  = UpdateDatabaseDBManager.addGroupMember(username, name, admin);

        return ret;
    }

    public double MongoDBgetAvgRating(String game){
        return GameDBManager.getAvgRating(game);
    }

}
