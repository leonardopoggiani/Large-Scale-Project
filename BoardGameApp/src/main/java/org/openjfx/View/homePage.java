package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.openjfx.App;
import org.openjfx.Controller.ArticlesCommentsLikesDBController;
import org.openjfx.Entities.Article;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class homePage {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    int giàCaricato = -1;
    private static String autore;
    private static String timestamp;
    private static String articolo;
    private static String titolo;

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("homePage");
    }

    @FXML
    void goToGames() throws IOException {
        App.setRoot("games");
    }

    @FXML
    void goToGroups() throws IOException {
        App.setRoot("groups");
    }

    @FXML
    void goToFriends() throws IOException {
        App.setRoot("users");
    }

    @FXML
    void goToSettings() throws IOException {
        App.setRoot("profileSettings");
    }

    @FXML
    void logout() throws IOException {
        App.setRoot("login");
        login.logout();
    }

    @FXML
    void setSuggestedArticles() throws IOException {
        if(giàCaricato == -1) {
            ArticlesCommentsLikesDBController home = new ArticlesCommentsLikesDBController();
            List<Article> list = home.neo4jListSuggestedArticles(login.getLoggedUser());

            if (list != null) {
                System.out.println("Lunghezza lista " + list.size());
                for (int i = 0; i < list.size(); i++) {
                    Article a = list.get(i);
                    TitledPane articolo = (TitledPane) App.getScene().lookup("#article" + (i + 1));
                    Text author = (Text) App.getScene().lookup("#authorarticle" + (i + 1));
                    Text timestamp = (Text) App.getScene().lookup("#timestamparticle" + (i + 1));
                    Text stats = (Text) App.getScene().lookup("#stats" + (i + 1));
                    int numComments = home.neo4jCountComments(a.getTitle(),a.getAuthor());
                    int numLikes = home.neo4jCountLikes(a.getTitle(),a.getAuthor(),"like");
                    int numUnlikes = home.neo4jCountLikes(a.getTitle(),a.getAuthor(),"dislike");

                    articolo.setText(a.getTitle());
                    author.setText(a.getAuthor());
                    timestamp.setText(String.valueOf(a.getTimestamp()));
                    stats.setText("Number of comments: " + numComments + ", likes:" + numLikes + "/ unlikes: " + numUnlikes);
                }
            }

            giàCaricato = 1;
        }
    }

    @FXML
    void goToArticle (MouseEvent event) throws IOException {
        Text tx = new Text();
        autore = null;
        timestamp = null;
        articolo = null;
        titolo = null;


        tx = (Text) event.getTarget();

        if(event.getTarget().getClass() == tx.getClass()) {

             tx = (Text) event.getTarget();
             articolo = tx.getText();
             System.out.println("ID " + "#author" + articolo);
            System.out.println("ID " + "#timestamp" + articolo);

            Text a = (Text) App.getScene().lookup("#author" + articolo);
            Text t = (Text) App.getScene().lookup("#timestamp" + articolo);
            autore = a.getText();
            timestamp = t.getText();
            titolo = tx.getText();
            App.setRoot("article");

        } else {
            logger.severe("errore nell'articolo");
        }
    }

    public static String getAuthor() {
        return autore;
    }
    public static String getTimestamp() {
        return timestamp;
    }
    public static String getTitolo() {
        return titolo;
    }
    public static String getArticolo() {
        return articolo;
    }

}
