package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.bean.CommentBean;
import it.unipi.dii.LSMDB.project.group5.bean.LikeBean;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.ArticleDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.ArticlesDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.CommentsDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.LikesDBManager;

import java.util.List;

public class ArticlesPageDBController {


    //ONLY NEO4J
    public List<ArticleBean> listSuggestedArticles(String username, int limit) {

        return ArticlesDBManager.searchSuggestedArticles(username, limit);

    }

    public List<CommentBean> listArticlesComments(String title, String author, int limit) {

        return CommentsDBManager.searchListComments(title, author, limit);

    }

    public int countLikes(String title, String author, String type) {

        return LikesDBManager.countLikes(title, author, type);
    }

    public int countComments(String title, String author) {

        return CommentsDBManager.countComments(title, author);

    }

    //ONLY MONGODB
    public ArticleBean showArticleDetails(String title, String author) {

       return  ArticleDBManager.readArticle(author, title);

    }



    public List<ArticleBean> filterByInfluencer(String influencer){
        return ArticleDBManager.filterByInfluencer(influencer);

    }

    public List<ArticleBean> filterByGame(String game){
        return ArticleDBManager.filterByGame(game);

    }

    public List<ArticleBean> filterByDate(String date){
        return ArticleDBManager.filterByDate(date);
    }

    public List<ArticleBean> orderByLikes (){
        return ArticleDBManager.orderBy("like");

    }

    public List<ArticleBean> orderByDislikes (){
        return ArticleDBManager.orderBy("dislike");
    }

    public List<ArticleBean> orderByComments (){
       return ArticleDBManager.orderBy("comments");
    }


    //MONGODB PRIMA, NEO4J DOPO
    public boolean addArticle(ArticleBean a)
    {
        if(ArticleDBManager.addArticle(a))
        {
             return ArticlesDBManager.addArticle(a);
        }
        return  false;

    }


    public boolean deleteArticle(String author, String title)
    {
        //DELETE MONGODB
        if(true)
        {
            ArticlesDBManager.deleteArticle(author, title);
        }

        return  false;
    }

    //NEO4J PRIMA, MONGODB DOPO
    public boolean addComment(CommentBean newComm) {

        boolean ret = CommentsDBManager.addComment(newComm);
        if (ret){
            ArticleDBManager.updateNumComments(1, newComm.getAuthorArt(), newComm.getTitleArt());
        }
        return ret;

    }

    public int addLike(LikeBean like) {

        int ret = LikesDBManager.addLike(like);
        if(ret > -1){
            if (ret == 0){
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

    public boolean deleteComment(CommentBean comm) {

        boolean ret  = CommentsDBManager.deleteComment(comm);
        if (ret){
            ArticleDBManager.updateNumComments(-1, comm.getAuthorArt(), comm.getTitleArt());

        }
        return ret;

    }
}
