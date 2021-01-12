package it.unipi.dii.LSMDB.project.group5.view;
import java.util.concurrent.*;

import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.cache.ArticlesCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.controller.ArticlesCommentsLikesDBController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

public class HomepageArticles {

    ArticlesCache cache = ArticlesCache.getInstance();
    private static HashMap<String,String> savedArticles = Maps.newHashMap();
    private static List<String> savedTitles = Lists.newArrayList();

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
            "Exploration:1020","Word-game:1025","Video Game Theme:1101");

    int giàCaricato = -1;
    private static String autore;
    private static String timestamp;
    private static String articolo;
    private static String titolo;

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

    @FXML
    void goToGames() throws IOException {
        App.setRoot("HomepageGames");
    }

    @FXML
    void goToGroups() throws IOException {
        App.setRoot("HomepageGroups");
    }

    @FXML
    void goToFriends() throws IOException {
        App.setRoot("HomepageUsers");
    }

    @FXML
    void goToSettings() throws IOException {
        App.setRoot("ProfileSettingsPageView");
    }

    @FXML
    void logout() throws IOException {
        App.setRoot("LoginPageView");
        LoginPageView.logout();
    }

    @FXML
    void setSuggestedArticles() throws IOException, ExecutionException {
        ArticlesCommentsLikesDBController home = new ArticlesCommentsLikesDBController();

        if(giàCaricato == -1) {
            List<String> titoli = Lists.newArrayList(savedArticles.values());
            if (titoli.isEmpty()) {
                // non ho salvato i titoli degli articoli da mostrare
                logger.info("cache vuota");
                List<ArticleBean> list = home.neo4jListSuggestedArticles(LoginPageView.getLoggedUser());

                if (list != null) {
                    System.out.println("Lunghezza lista " + list.size());
                    for (int i = 0; i < list.size() && i < 6; i++) {
                        ArticleBean a = list.get(i);
                        // salvo i titoli degli articoli mostrati su homepage
                        savedTitles.add(a.getTitle());
                        savedArticles.put(a.getTitle(),a.getAuthor());

                        TitledPane articolo = (TitledPane) App.getScene().lookup("#articolocompleto" + (i + 1));
                        Text author = (Text) App.getScene().lookup("#authorcompleto" + (i + 1));
                        Text timestamp = (Text) App.getScene().lookup("#timestampcompleto" + (i + 1));
                        Text stats = (Text) App.getScene().lookup("#statscompleto" + (i + 1));
                        int numComments = home.neo4jCountComments(a.getTitle(), a.getAuthor());
                        int numLikes = home.neo4jCountLikes(a.getTitle(), a.getAuthor(), "like");
                        int numUnlikes = home.neo4jCountLikes(a.getTitle(), a.getAuthor(), "dislike");

                        articolo.setText(a.getTitle());
                        author.setText(a.getAuthor());
                        timestamp.setText(String.valueOf(a.getTimestamp()));
                        stats.setText("Comments: " + numComments + ", likes:" + numLikes + ", unlikes: " + numUnlikes);
                    }
                }

                giàCaricato = 1;
            } else {
                int i = 0;
                for (String titolo : savedTitles) {
                    if(i < savedTitles.size() && i < 6) {
                        String autore = savedArticles.get(titolo);
                        cache.setAuthor(autore);
                        ArticleBean a = cache.getDataIfPresent(titolo);
                        if(a != null && a.getTitle() != null) {
                            TitledPane articolo = (TitledPane) App.getScene().lookup("#articolocompleto" + (i + 1));
                            Text author = (Text) App.getScene().lookup("#authorcompleto" + (i + 1));
                            Text timestamp = (Text) App.getScene().lookup("#timestampcompleto" + (i + 1));
                            Text stats = (Text) App.getScene().lookup("#statscompleto" + (i + 1));
                            int numComments = home.neo4jCountComments(a.getTitle(), a.getAuthor());
                            int numLikes = home.neo4jCountLikes(a.getTitle(), a.getAuthor(), "like");
                            int numUnlikes = home.neo4jCountLikes(a.getTitle(), a.getAuthor(), "dislike");

                            System.out.println("#articolocompleto" + (i + 1) + " ," + articolo);

                            articolo.setText(a.getTitle());
                            author.setText(a.getAuthor());
                            timestamp.setText(String.valueOf(a.getTimestamp()));
                            stats.setText("Comments: " + numComments + ", likes:" + numLikes + ", unlikes: " + numUnlikes);
                            i++;
                        }
                    }
                }

                giàCaricato = 1;

            }
        }
    }

    @FXML
    void goToArticle (MouseEvent event) throws IOException {

        AnchorPane articolo = (AnchorPane) event.getSource();
        String idArticle = articolo.getId();

        Text a = (Text) App.getScene().lookup("#author" + idArticle);
        Text t = (Text) App.getScene().lookup("#timestamp" + idArticle);
        autore = a.getText();
        timestamp = t.getText();
        TitledPane tx = (TitledPane) App.getScene().lookup("#articolo" + idArticle);
        titolo = tx.getText();
        App.setRoot("ArticlePageView");

    }

    @FXML
    void filterResearch () throws IOException {
        ArticlesCommentsLikesDBController controller = new ArticlesCommentsLikesDBController();

        TextField gioco = (TextField) App.getScene().lookup("#game");
        AutoCompleteTextField autore = (AutoCompleteTextField) App.getScene().lookup("#author");
        DatePicker data = (DatePicker) App.getScene().lookup("#data");

        LocalDate valoreData = data.getValue();
        String game = gioco.getText();
        String nome = autore.getText();

        List<ArticleBean> filteredGames0 = Lists.newArrayList();
        List<ArticleBean> filteredGames1 = Lists.newArrayList();
        List<ArticleBean> filteredGames2 =Lists.newArrayList();

        if(game != null) {
            filteredGames0 = controller.filterByGame(game);
        }

        if(valoreData != null){
            filteredGames1 = controller.filterByDate(String.valueOf(valoreData));
        }

        if(nome != null) {
            filteredGames2 = controller.filterByInfluencer(nome);
        }

        filteredGames0.addAll(filteredGames1);
        filteredGames0.addAll(filteredGames2);

        List<ArticleBean> filteringResult = new ArrayList<ArticleBean>(new HashSet<ArticleBean>(filteredGames0));
        showFilteringResult(filteringResult);
    }

    private void showFilteringResult(List<ArticleBean> filteringResult) {
        logger.info("show filtering result");
        if (filteringResult != null) {
            System.out.println("Lunghezza lista " + filteringResult.size());

            for (int i = 0; i < filteringResult.size() && i < 6; i++) {
                logger.info("mostro");
                ArticleBean g = (ArticleBean) filteringResult.get(i);
                TitledPane articolo = (TitledPane) App.getScene().lookup("#fullarticle" + (i + 1));
                Text author = (Text) App.getScene().lookup("#authorarticle" + (i + 1));
                Text timestamp = (Text) App.getScene().lookup("#timestamparticle" + (i + 1));
                Text stats = (Text) App.getScene().lookup("#stats" + (i + 1));

                articolo.setText(g.getTitle());
                author.setText(g.getAuthor());
                // timestamp.setText(String.valueOf(g.getTimestamp()));
            }
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
