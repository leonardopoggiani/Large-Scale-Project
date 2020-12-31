package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.openjfx.App;
import org.openjfx.Controller.ArticlesCommentsLikesDBController;
import org.openjfx.Controller.UpdateDatabaseDBController;
import org.openjfx.Entities.Article;
import org.openjfx.Entities.InfoComment;
import org.openjfx.Entities.InfoLike;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

public class article {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    int giàCaricato = -1;

    @FXML
    void setArticleFields() throws IOException {

       if(giàCaricato == -1) {
           ArticlesCommentsLikesDBController article = new ArticlesCommentsLikesDBController(); // sostituire con controller
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

           List<InfoComment> infoComments = null;
           infoComments = article.neo4jListArticlesComment(homePage.getTitolo(),homePage.getAuthor());

           for(int i = 0; i < infoComments.size() && i < 3; i++){
               TextArea commento = (TextArea) App.getScene().lookup("#comment" + (i + 1));
               commento.setText(infoComments.get(i).getText());
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
        UpdateDatabaseDBController update = new UpdateDatabaseDBController();
        InfoLike aLike = new InfoLike("like",login.getLoggedUser(),String.valueOf(new Timestamp(System.currentTimeMillis())), homePage.getAuthor(),homePage.getTitolo());
        update.Neo4jAddLike(aLike);
        // salvare numero like
    }

    @FXML
    void unlike() throws IOException {
        Text like = (Text) App.getScene().lookup("#numberunlike");
        int numberOfUnlike = Integer.parseInt(like.getText());
        like.setText(String.valueOf(numberOfUnlike + 1));
        UpdateDatabaseDBController update = new UpdateDatabaseDBController();
        InfoLike anUnlike = new InfoLike("dislike",login.getLoggedUser(),String.valueOf(new Timestamp(System.currentTimeMillis())), homePage.getAuthor(),homePage.getTitolo());
        update.Neo4jAddLike(anUnlike);
        // salvare numero unlike
    }

    @FXML
    void postComment() throws IOException {
        TextArea articlecomment = (TextArea) App.getScene().lookup("#articlecomment");
        UpdateDatabaseDBController update = new UpdateDatabaseDBController();
        InfoComment comment = new InfoComment(articlecomment.getText(),login.getLoggedUser(),String.valueOf(new Timestamp(System.currentTimeMillis())), homePage.getAuthor(),homePage.getTitolo());
        update.Neo4jAddComment(comment);
        TextArea nuovo = (TextArea) App.getScene().lookup("#comment1");
        nuovo.setText(comment.getText());
    }
}
