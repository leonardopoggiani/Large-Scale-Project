package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.bean.CommentBean;
import it.unipi.dii.LSMDB.project.group5.bean.LikeBean;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.ArticleDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.ArticlesDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.CommentsDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.LikesDBManager;

import java.util.List;
import java.util.logging.Logger;

public class ArticlesPagesDBController {

    Logger logger = Logger.getLogger(this.getClass().getName());

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
    public ArticleBean showArticleDetails(int id) {

       return  ArticleDBManager.readArticle(id);

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
        int id = ArticleDBManager.addArticle(a);
        if(id!=-1)
        {
            if(!ArticlesDBManager.addArticle(a))
            {
                logger.severe("NEO4J | Articolo " + id + " non aggiunto in Neo4j!");
                ArticleDBManager.deleteArticle(id);
                logger.info("MONGODB | Articolo " + id + " rimosso da mongoDB!");
                return  false;
            }
            return true;
        }

        return  false;

    }


    public boolean deleteArticle(int idArt)
    {
        //DELETE MONGODB
        if(ArticleDBManager.deleteArticle(idArt))
        {
            //DELETE NEO4J
            if(!ArticlesDBManager.deleteArticle(idArt))
            {
                logger.severe("NEO4J | Articolo " + idArt + " ancora in Neo4j!");
                return false;
            }

            return  true;
        }

        return  false;
    }

    //NEO4J PRIMA, MONGODB DOPO
    public boolean addComment(CommentBean newComm) {

        boolean ret = CommentsDBManager.addComment(newComm);
        if (ret){
            if(!ArticleDBManager.updateNumComments(1, newComm.getId()))
            {
                logger.severe("MONGODB | Numero dei commenti dell'articolo "+ newComm.getId() + " non incrementato!");
                return false;
            }
            return  ret;
        }

        return false;

    }

    public int addLike(LikeBean like) {

        int ret = LikesDBManager.addLike(like);
        if(ret > -1){
            if (ret == 0){
                if(like.getType().equals("like")){
                    if(!ArticleDBManager.updateNumLike(-1, like.getId()))
                        logger.severe("MONGODB | Numero dei like di " + like.getId() +" non decrementato!");
                }else {
                    if(!ArticleDBManager.updateNumDislike(-1, like.getId()))
                        logger.severe("MONGODB | Numero dei dislike di " + like.getId() +" non decrementato!");

                }
            }else {
                if(like.getType().equals("like")){
                    if(!ArticleDBManager.updateNumLike(1, like.getId()))
                        logger.severe("MONGODB | Numero dei like dell'articolo " + like.getId() +" non incrementato!");
                }else {
                    if(!ArticleDBManager.updateNumDislike(1, like.getId()))
                        logger.severe("MONGODB | Numero dei dislike dell'articolo " + like.getId() +" non incrementato!");

                }
            }

        }

        return ret;

    }

    public boolean deleteComment(CommentBean comm) {

        boolean ret  = CommentsDBManager.deleteComment(comm);
        if (ret){
            if(!ArticleDBManager.updateNumComments(-1, comm.getId() ))
                logger.severe("MONGODB | Numero dei commenti articolo: " + comm.getId() +" non decrementato!");

            return true;
        }


        return  false;
    }
}
