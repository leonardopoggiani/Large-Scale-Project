package org.openjfx.View;

import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.openjfx.App;
import org.openjfx.Controller.ArticlesCommentsLikesDBController;
import org.openjfx.Entities.Article;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class homePage {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    ObservableList<String> categorie = FXCollections.observableArrayList(
            "Math:1104","Card Game:1002","Humor:1079","Party Game:1030",
            "Number:1098","Puzzle:1028","Dice:1017","Sports:1038",
            "Book:1117","Fantasy:1010","Miniatures:1047","Wargame:1019",
            "Napoleonic:1051","Children's Game:1041","Memory:1045",
            "Educational:1094","Medical:2145","Animals:1089","Racing:1031",
            "Adventure:1022","Travel:1097","Abstact Strategy:1009",
            "Economic:1021","Trains:1034","Transportation:1011","Real-time:1037",
            "Action/Dexterity:1032","Ancient:1050","Collectible Components:1044",
            "Fighting:1046","Movies/TV/Radio Theme:1064","Bluffing:1023",
            "Zombies:2481","Medieval:1035","Negotiation:1026","World War II: 1049",
            "Spies/Secret Agents:1081","Deduction:1039","Murder/Mystery:1040",
            "Aviation/Flight:2650","Modern Warfare:1069","Territory Building:1086",
            "Print & Play:1120","Novel-Based:1093","Puzzle:1028","Science Fiction:1016",
            "Exploration:1020","Word-game:1025","Video  Game Theme:1101");

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
                for (int i = 0; i < list.size() && i < 6; i++) {
                    Article a = list.get(i);
                    TitledPane articolo = (TitledPane) App.getScene().lookup("#fullarticle" + (i + 1));
                    Text author = (Text) App.getScene().lookup("#authorarticle" + (i + 1));
                    Text timestamp = (Text) App.getScene().lookup("#timestamparticle" + (i + 1));
                    Text stats = (Text) App.getScene().lookup("#stats" + (i + 1));
                    int numComments = home.neo4jCountComments(a.getTitle(),a.getAuthor());
                    int numLikes = home.neo4jCountLikes(a.getTitle(),a.getAuthor(),"like");
                    int numUnlikes = home.neo4jCountLikes(a.getTitle(),a.getAuthor(),"dislike");

                    articolo.setText(a.getTitle());
                    author.setText(a.getAuthor());
                    timestamp.setText(String.valueOf(a.getTimestamp()));
                    stats.setText("Number of comments: " + numComments + ", likes:" + numLikes + ", unlikes: " + numUnlikes);
                }
            }

            giàCaricato = 1;
        }
    }

    @FXML
    void goToArticle (MouseEvent event) throws IOException {
        autore = null;
        timestamp = null;
        articolo = null;
        titolo = null;
        Text testo = new Text();

        if(event.getTarget().getClass() != testo.getClass()) {
            AnchorPane articolo = (AnchorPane) event.getTarget();
            String idArticle = articolo.getId();
            System.out.println("ID " + "#author" + idArticle);
            System.out.println("ID " + "#timestamp" + idArticle);

            Text a = (Text) App.getScene().lookup("#author" + idArticle);
            Text t = (Text) App.getScene().lookup("#timestamp" + idArticle);
            autore = a.getText();
            timestamp = t.getText();
            TitledPane tx = (TitledPane) App.getScene().lookup("#full" + idArticle);
            titolo = tx.getText();
            App.setRoot("article");
        }
    }

    @FXML
    void caricaCategorie() throws IOException {
        logger.info("Carico le categorie");
        Scene scene = App.getScene(); // recupero la scena della signup
        ComboBox cat1 = (ComboBox) scene.lookup("#gioco");

        cat1.setItems(categorie);
    }

    @FXML
    void filterResearch () throws IOException {
        ComboBox gioco = (ComboBox) App.getScene().lookup("#gioco");
        AutoCompleteTextField autore = (AutoCompleteTextField) App.getScene().lookup("#author");
        DatePicker data = (DatePicker) App.getScene().lookup("#data");

        LocalDate valoreData = data.getValue();
        String nome = (String) gioco.getSelectionModel().getSelectedItem();
        System.out.println(nome + " , " + autore.getText() + " , " + data.toString() );
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

    public static void setAuthor(String author) {
        autore = author;
    }
    public static void setTimestamp(String ts) {
        timestamp = ts;
    }
    public static void setTitle(String title) {
        titolo = title;
    }
    public static void setArticle(String article) {
        articolo = article;
    }

}
