package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.bean.*;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.ArticleDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.GameDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.*;

import java.util.logging.Logger;

public class UpdateDatabaseDBController {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    public  UpdateDatabaseDBController() {
        //LoginSignUpDBManager.InitializeDriver();
        //MongoDBManager.createConnection();
    }

    public Boolean Neo4jAddComment(CommentBean newComm) {

        Boolean ret = false;
        ret = CommentsDBManager.addComment(newComm);
        if (ret){
            ArticleDBManager.updateNumComments(1, newComm.getAuthorArt(), newComm.getTitleArt());

        }
        return ret;

    }


    public int Neo4jAddLike(LikeBean like) {

        int ret = 0;
        ret = LikesDBManager.addLike(like);
        if(ret > 0){
            if (ret == 1){
                if(like.getType().equals("like")){
                    ArticleDBManager.updateNumLike(-1, like.getAuthorArt(), like.getTitleArt());
                }else {
                    ArticleDBManager.updateNumDislike(-1, like.getAuthorArt(), like.getTitleArt());

                }
            }else {
                if(like.getType().equals("like")){
                    ArticleDBManager.updateNumLike(1, like.getAuthorArt(), like.getTitleArt());
                }else {
                    ArticleDBManager.updateNumDislike(1, like.getAuthorArt(), like.getTitleArt());

                }
            }

        }


        return ret;

    }

    public Boolean Neo4jDeleteComment(CommentBean comm) {

       Boolean ret = false;
        ret  = CommentsDBManager.deleteComment(comm);
        if (ret){
            ArticleDBManager.updateNumComments(-1, comm.getAuthorArt(), comm.getTitleArt());

        }
        return ret;

    }

    public Boolean Neo4jAddReview(ReviewBean newRev) {

        Boolean ret = false;
        ret = ReviewsDBManager.addReview(newRev);
        if(ret){
            GameDBManager.updateNumReviews(1, newRev.getGame());
        }


        return ret;

    }

    //Add rate se non esiste già un voto fatto dalla stessa persona sullo stesso gioco
    public Boolean Neo4jAddRating(RateBean newRate) {

        Boolean ret = false;
        ret = RatingsDBManager.addRating(newRate);
        if (ret){
            GameDBManager.updateRating(newRate.getVote(), newRate.getGame());
        }
        return ret;
    }


    public Boolean Neo4jDeleteReview(ReviewBean rev) {

        Boolean ret = false;
        ret  = ReviewsDBManager.deleteReview(rev);
        if(ret){
            GameDBManager.updateNumReviews(-1, rev.getGame());
        }
        return ret;
    }

    public Boolean Neo4jAddGroup(GroupBean newGroup) {

        Boolean ret = false;
        ret  = GroupsPostsDBManager.addGroup(newGroup);

        return ret;
    }


    public Boolean Neo4jDeleteGroup(String delGroup,String delAdmin) {

        Boolean ret = false;
        ret  = GroupsPostsDBManager.deleteGroup(delGroup, delAdmin);
        return ret;
    }

    public Boolean Neo4jAddGroupMember(String username, String name, String admin) {

        Boolean ret = false;
        ret  = GroupsPostsDBManager.addGroupMember(username, name, admin);

        return ret;
    }

    public double MongoDBgetAvgRating(String game){
        return GameDBManager.getAvgRating(game);
    }

}
