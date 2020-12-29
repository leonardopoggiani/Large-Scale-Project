package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.openjfx.App;
import org.openjfx.Controller.ListArticlesAndCommentsDBController;
import org.openjfx.DBManager.MongoDBManager.ArticleDBManager;
import org.openjfx.Entities.Article;
import org.openjfx.Entities.Comment;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class article {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    int giàCaricato = -1;

    @FXML
    void setArticleFields() throws IOException {

       if(giàCaricato == -1) {
           ListArticlesAndCommentsDBController article = new ListArticlesAndCommentsDBController(); // sostituire con controller
           Article a = article.mongoDBshowArticle(homePage.getArticolo(),homePage.getAuthor());
           // devo recuperare l'articolo intero
           Text titolo = (Text) App.getScene().lookup("#titolo");
           TextArea body = (TextArea) App.getScene().lookup("#articlebody");
           Text author = (Text) App.getScene().lookup("#author");
           Text like = (Text) App.getScene().lookup("#numberlike");
           Text unlike = (Text) App.getScene().lookup("#numberunlike");

           author.setText("written by " + homePage.getAuthor() + " published on " + homePage.getTimestamp());
           titolo.setText(homePage.getTitolo());
           like.setText(String.valueOf(article.neo4jCountLikes(homePage.getTitolo(),homePage.getAuthor(),"like")));
           unlike.setText(String.valueOf(article.neo4jCountLikes(homePage.getTitolo(),homePage.getAuthor(),"dislike")));
            body.setText(a.getText());

           List<Comment> comments = null;
           comments = article.neo4jListArticlesComment(homePage.getTitolo(),homePage.getAuthor());

           for(int i = 0; /*i < comments.size() &&*/ i < 3; i++){
               TextArea commento = (TextArea) App.getScene().lookup("#comment" + (i + 1));
               // commento.setText(comments.get(i).getText());
               commento.setText("Provaaa");
           }
           giàCaricato = 1;
       }
    }

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("homePage");
    }

    @FXML
    void like() throws IOException {
        Text like = (Text) App.getScene().lookup("#numberlike");
        int numberOfLike = Integer.parseInt(like.getText());
        like.setText(String.valueOf(numberOfLike + 1));
        // salvare numero like
    }

    @FXML
    void unlike() throws IOException {
        Text like = (Text) App.getScene().lookup("#numberunlike");
        int numberOfUnlike = Integer.parseInt(like.getText());
        like.setText(String.valueOf(numberOfUnlike + 1));
        // salvare numero unlike
    }

    @FXML
    void postComment() throws IOException {
        logger.info("Commento");
    }
}
