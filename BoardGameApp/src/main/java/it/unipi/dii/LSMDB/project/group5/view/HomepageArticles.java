package it.unipi.dii.LSMDB.project.group5.view;
import java.util.concurrent.*;

import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import it.unipi.dii.LSMDB.project.group5.cache.ArticlesCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    ObservableList<String> filters = FXCollections.observableArrayList(
            "Game", "Author", "Release Date", "None");

    ObservableList<String> ordinamenti = FXCollections.observableArrayList(
            "Number of likes", "Number of dislikes", "Number of comments", "None");


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
    void showFilters () throws IOException {
        logger.info("Carico i filtri");
        Scene scene = App.getScene(); // recupero la scena della signup
        ComboBox filtri = (ComboBox) scene.lookup("#filter");

        filtri.setItems(filters);
    }

    @FXML
    void showOrdering () throws IOException {
        Scene scene = App.getScene(); // recupero la scena della signup
        ComboBox order = (ComboBox) scene.lookup("#order");

        order.setItems(ordinamenti);
    }

    @FXML
    void setFilters() {
        Scene scene = App.getScene(); // recupero la scena della signup
        TextField game = (TextField) scene.lookup("#game");
        TextField author = (TextField) scene.lookup("#author");
        DatePicker data = (DatePicker) scene.lookup("#data");
        ComboBox filtri = (ComboBox) scene.lookup("#filter");

        if(filtri.getSelectionModel().getSelectedItem() != null){
            String filtroSelezionato = filtri.getSelectionModel().getSelectedItem().toString();
            System.out.println(filtroSelezionato);
            if(!filtroSelezionato.equals("None")){
                switch(filtroSelezionato){
                    case "Game":
                        game.setVisible(true);
                        author.setVisible(false);
                        data.setVisible(false);
                        break;
                    case "Author":
                        game.setVisible(false);
                        author.setVisible(true);
                        data.setVisible(false);
                        break;
                    case "Release Date":
                        game.setVisible(false);
                        author.setVisible(false);
                        data.setVisible(true);
                        break;
                    default:
                        game.setVisible(false);
                        author.setVisible(false);
                        data.setVisible(false);
                        break;
                }
            } else {
                game.setVisible(false);
                author.setVisible(false);
                data.setVisible(false);
            }
        }
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
                    for (int i = 0; i < 6; i++) {

                        TitledPane articolo = (TitledPane) App.getScene().lookup("#articolocompleto" + (i + 1));
                        Text author = (Text) App.getScene().lookup("#authorcompleto" + (i + 1));
                        Text timestamp = (Text) App.getScene().lookup("#timestampcompleto" + (i + 1));
                        Text stats = (Text) App.getScene().lookup("#statscompleto" + (i + 1));


                        if( !(articolo == null || author == null || timestamp == null || stats == null) ) {
                            if (i < list.size()) {
                                ArticleBean a = list.get(i);
                                // salvo i titoli degli articoli mostrati su homepage
                                savedTitles.add(a.getTitle());
                                savedArticles.put(a.getTitle(), a.getAuthor());
                                articolo.setText(a.getTitle());
                                author.setText(a.getAuthor());
                                timestamp.setText(String.valueOf(a.getTimestamp()));

                                int numComments = home.neo4jCountComments(a.getTitle(), a.getAuthor());
                                int numLikes = home.neo4jCountLikes(a.getTitle(), a.getAuthor(), "like");
                                int numUnlikes = home.neo4jCountLikes(a.getTitle(), a.getAuthor(), "dislike");
                                stats.setText("Comments: " + numComments + ", likes:" + numLikes + ", unlikes: " + numUnlikes);
                            } else {
                                articolo.setText("");
                                author.setText("");
                                timestamp.setText("");
                                stats.setText("");
                            }
                        }
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

                            articolo.setText(a.getTitle());
                            author.setText(a.getAuthor());
                            timestamp.setText(String.valueOf(a.getTimestamp()));
                            stats.setText("Comments: " + a.getNumberComments() + ", likes:" + a.getNumberLikes() + ", unlikes: " + a.getNumberDislike());
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
        TextField autore = (TextField) App.getScene().lookup("#author");
        DatePicker data = (DatePicker) App.getScene().lookup("#data");
        ComboBox order = (ComboBox) App.getScene().lookup("#order");

        List<ArticleBean> filteredArticles = Lists.newArrayList();
        List<ArticleBean> sortedList = Lists.newArrayList();


        if(gioco.isVisible() && !gioco.getText().equals("")) {
            // filtraggio per gioco
            filteredArticles = controller.filterByGame(gioco.getText());
        } else if(autore.isVisible() && !autore.getText().equals("")) {
            // filtraggio per autore
            filteredArticles = controller.filterByInfluencer(autore.getText());
        } else if (data.isVisible() && !(data.getValue() == null)){
            // filtraggio per data
            filteredArticles = controller.filterByDate(String.valueOf(data.getValue()));
        }

        List<ArticleBean> filteringResult = new ArrayList<ArticleBean>(new HashSet<ArticleBean>(filteredArticles));

        if(order.getSelectionModel().getSelectedItem() != null) {
            logger.info("Voglio ordinare i risultati");
            int index1 = order.getSelectionModel().getSelectedIndex();
            String ordering = ordinamenti.get(index1);

            if(filteredArticles.isEmpty()) {
                // qui ci entro solo se non ho selezionato nessun filtro e quindi applico l'ordinamento a tutti i giochi
                if(!ordering.equals("None")){
                    switch (ordering) {
                        case "Number of likes" -> {
                            logger.info("Ordino per likes");
                            sortedList = controller.orderByLikes();
                        }
                        case "Number of dislikes" -> {
                            logger.info("Ordino per dislikes");
                            sortedList = controller.orderByDislikes();
                        }
                        case "Number of comments" -> {
                            logger.info("Ordino per comments");
                            sortedList = controller.orderByComments();
                        }
                        default -> logger.info("Non ordino");
                    }
                    filteringResult = new ArrayList<ArticleBean>(new HashSet<ArticleBean>(sortedList));
                }
            } else {
                filteringResult = orderingResult(filteredArticles, ordering);
            }

        }

        showFilteringResult(filteringResult);
    }

    private List<ArticleBean> orderingResult(List<ArticleBean> target, String mode) {

        if(!mode.equals("None")){
            switch (mode) {
                case "Average ratings" -> {
                    logger.info("Ordino per like");
                    target.sort(new HomepageArticles.NumberOfLikeComparator());
                }
                case "Number of reviews" -> {
                    logger.info("Ordino per dislike");
                    target.sort(new HomepageArticles.NumberOfDislikeComparator());
                }
                case "Number of ratings" -> {
                    logger.info("Ordino per comments");
                    target.sort(new HomepageArticles.NumberOfComments());
                }
                default -> logger.info("Non ordino");
            }
        }

        return target;
    }

    static class NumberOfLikeComparator implements Comparator<ArticleBean> {
        @Override
        public int compare(ArticleBean a, ArticleBean b) {
            return Double.compare(b.getNumberLikes(), a.getNumberLikes());
        }
    }

    static class NumberOfDislikeComparator implements Comparator<ArticleBean> {
        @Override
        public int compare(ArticleBean a, ArticleBean b) {
            return Integer.compare(b.getNumberDislike(), a.getNumberDislike());
        }
    }

    static class NumberOfComments implements Comparator<ArticleBean> {
        @Override
        public int compare(ArticleBean a, ArticleBean b) {
            return Integer.compare(b.getNumberComments(), a.getNumberComments());
        }
    }

    private void showFilteringResult(List<ArticleBean> filteringResult) {
        if (filteringResult != null) {
            System.out.println("Lunghezza lista " + filteringResult.size());

            for (int i = 0; i < 6; i++) {
                TitledPane articolo = (TitledPane) App.getScene().lookup("#articolocompleto" + (i + 1));
                Text author = (Text) App.getScene().lookup("#authorcompleto" + (i + 1));
                Text timestamp = (Text) App.getScene().lookup("#timestampcompleto" + (i + 1));
                Text stats = (Text) App.getScene().lookup("#statscompleto" + (i + 1));

                if(i < filteringResult.size()) {
                    ArticleBean g = filteringResult.get(i);
                    articolo.setText(g.getTitle());
                    author.setText(g.getAuthor());
                    timestamp.setText(String.valueOf(g.getTimestamp()));
                    stats.setText("Comments: " + g.getNumberComments() + ", likes:" + g.getNumberLikes() + ", unlikes: " + g.getNumberDislike());
                } else {
                    articolo.setText("");
                    author.setText("");
                    timestamp.setText("");
                    stats.setText("");
                }
            }
        }
    }

    @FXML
    void addArticle () throws IOException {
        App.setRoot("AddArticle");

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
