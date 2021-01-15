package it.unipi.dii.LSMDB.project.group5.view;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.cache.ArticlesCache;
import it.unipi.dii.LSMDB.project.group5.controller.ArticlesPageDBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class HomepageArticles {

    @FXML
    TitledPane articolocompleto1;

    @FXML
    TitledPane articolocompleto2;

    @FXML
    TitledPane articolocompleto3;

    @FXML
    TitledPane articolocompleto4;

    @FXML
    TitledPane articolocompleto5;

    @FXML
    TitledPane articolocompleto6;

    @FXML
    Text authorcompleto1;

    @FXML
    Text authorcompleto2;

    @FXML
    Text authorcompleto3;

    @FXML
    Text authorcompleto4;

    @FXML
    Text authorcompleto5;

    @FXML
    Text authorcompleto6;

    @FXML
    Text timestampcompleto1;

    @FXML
    Text timestampcompleto2;

    @FXML
    Text timestampcompleto3;

    @FXML
    Text timestampcompleto4;

    @FXML
    Text timestampcompleto5;

    @FXML
    Text timestampcompleto6;

    @FXML
    Text statscompleto1;

    @FXML
    Text statscompleto2;

    @FXML
    Text statscompleto3;

    @FXML
    Text statscompleto4;

    @FXML
    Text statscompleto5;

    @FXML
    Text statscompleto6;

    @FXML
    ComboBox filtri;

    @FXML
    ComboBox order;

    @FXML
    TextField game;

    @FXML
    TextField author;

    @FXML
    DatePicker data;

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

    private static String autore;
    private static String timestamp;
    private static String articolo;
    private static String titolo;

    @FXML
    void initialize() throws IOException, ExecutionException {
        setSuggestedArticles();
    }

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
        ArticlesPageDBController home = new ArticlesPageDBController();

        if (savedTitles.isEmpty()) {
            // non ho salvato i titoli degli articoli da mostrare
            logger.info("cache vuota");
            List<ArticleBean> list = home.listSuggestedArticles(LoginPageView.getLoggedUser(), 10);
            System.out.println("Lunghezza lista " + list.size());
            showArticles(list);
        } else {
            int i = 0;
            logger.info("cache piena");
            for (String titolo : savedTitles) {
                if (i < savedTitles.size()) {
                    String autore = savedArticles.get(titolo);
                    cache.setAuthor(autore);
                    ArticleBean a = cache.getDataIfPresent(titolo);
                    if (a != null && a.getTitle() != null) {
                        TitledPane ar = chooseArticle(i);
                        Text au = chooseAuthor(i);
                        Text ti = chooseTimestamp(i);
                        Text st = chooseStatistics(i);

                        ar.setText(a.getTitle());
                        au.setText(a.getAuthor());
                        ti.setText(String.valueOf(a.getTimestamp()));
                        st.setText("Comments: " + a.getNumberComments() + ", likes:" + a.getNumberLikes() + ", unlikes: " + a.getNumberDislike());
                    }
                }
            }
        }

        showFilters();
    }

    private void showArticles(List<ArticleBean> list) {
        ArticlesPageDBController home = new ArticlesPageDBController();

        int numComments = 0;
        int numLikes = 0;
        int numUnlikes = 0;

        logger.info("show articles");
        if (list != null) {
            System.out.println("Lunghezza lista " + list.size());
            for(int i = 0; i < 6; i++) {

                TitledPane ar = chooseArticle(i);
                Text aut = chooseAuthor(i);
                Text tim = chooseTimestamp(i);
                Text st = chooseStatistics(i);

                if (i < list.size()) {
                    ArticleBean a = list.get(i);

                    // caching
                    savedTitles.add(a.getTitle());
                    savedArticles.put(a.getTitle(), a.getAuthor());

                    numComments = home.countComments(a.getTitle(), a.getAuthor());
                    numLikes = home.countLikes(a.getTitle(), a.getAuthor(), "like");
                    numUnlikes = home.countLikes(a.getTitle(), a.getAuthor(), "dislike");

                    ar.setText(a.getTitle());
                    aut.setText(a.getAuthor());
                    tim.setText(String.valueOf(a.getTimestamp()));
                    st.setText("Comments: " + numComments + ", likes:" + numLikes + ", unlikes: " + numUnlikes);
                } else {
                    ar.setText("");
                    aut.setText("");
                    tim.setText("");
                    st.setText("");
                }
            }
        }
    }

    private Text chooseTimestamp(int i){
        return switch (i) {
            case 0 -> timestampcompleto1;
            case 1 -> timestampcompleto2;
            case 2 -> timestampcompleto3;
            case 3 -> timestampcompleto4;
            case 4 -> timestampcompleto5;
            case 5 -> timestampcompleto6;
            default -> new Text();
        };

    }

    private Text chooseAuthor(int i){
        return switch (i) {
            case 0 -> authorcompleto1;
            case 1 -> authorcompleto2;
            case 2 -> authorcompleto3;
            case 3 -> authorcompleto4;
            case 4 -> authorcompleto5;
            case 5 -> authorcompleto6;
            default -> new Text();
        };
    }

    private Text chooseStatistics(int i){
        return switch (i) {
            case 0 -> statscompleto1;
            case 1 -> statscompleto2;
            case 2 -> statscompleto3;
            case 3 -> statscompleto4;
            case 4 -> statscompleto5;
            case 5 -> statscompleto6;
            default -> new Text();
        };

    }

    private TitledPane chooseArticle(int i){
        return switch (i) {
            case 0 -> articolocompleto1;
            case 1 -> articolocompleto2;
            case 2 -> articolocompleto3;
            case 3 -> articolocompleto4;
            case 4 -> articolocompleto5;
            case 5 -> articolocompleto6;
            default -> new TitledPane();
        };

    }

    @FXML
    void goToArticle (MouseEvent event) throws IOException {

        AnchorPane articolo = (AnchorPane) event.getSource();
        String idArticle = articolo.getId();

        int index = Integer.parseInt(idArticle.substring(idArticle.length() - 1));

        Text a = chooseAuthor(index - 1);
        TitledPane ar = chooseArticle(index - 1);
        Text ts = chooseTimestamp(index - 1);

        autore = a.getText();
        timestamp = ts.getText();
        titolo = ar.getText();

        App.setRoot("ArticlePageView");

    }

    @FXML
    void filterResearch () throws IOException {
        ArticlesPageDBController controller = new ArticlesPageDBController();

        List<ArticleBean> filteredArticles = Lists.newArrayList();
        List<ArticleBean> sortedList = Lists.newArrayList();

        if(game.isVisible() && !game.getText().equals("")) {
            // filtraggio per gioco
            filteredArticles = controller.filterByGame(game.getText());
        } else if(author.isVisible() && !author.getText().equals("")) {
            // filtraggio per autore
            filteredArticles = controller.filterByInfluencer(author.getText());
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
                TitledPane articolo = chooseArticle(i);
                Text author = chooseAuthor(i);
                Text timestamp = chooseTimestamp(i);
                Text stats = chooseStatistics(i);

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
