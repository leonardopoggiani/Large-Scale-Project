package it.unipi.dii.lsmdb.project.group5.view;

import com.google.common.collect.Lists;
import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.bean.ArticleBean;
import it.unipi.dii.lsmdb.project.group5.cache.ArticlesCache;
import it.unipi.dii.lsmdb.project.group5.controller.ArticlesPagesDBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * The type Homepage articles.
 */
public class HomepageArticles {

    @FXML
    Text id1;

    @FXML
    Text id2;

    @FXML
    Text id3;

    @FXML
    Text id4;

    @FXML
    Text id5;

    @FXML
    Text id6;

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

    @FXML
    Button statisticsButton;

    @FXML
    Text writetext;

    @FXML
    ImageView addarticle;

    /**
     * The Cache.
     */
    ArticlesCache cache = ArticlesCache.getInstance();
    private static List<Integer> savedID = Lists.newArrayList();

    /**
     * The Logger.
     */
    Logger logger =  Logger.getLogger(this.getClass().getName());

    /**
     * The Categorie.
     */
    ObservableList<String> categorie = FXCollections.observableArrayList(
            "Math:1104", "Card Game:1002","Humor:1079","Party Game:1030",
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

    /**
     * The Filters.
     */
    ObservableList<String> filters = FXCollections.observableArrayList(
            "Game", "Author", "Release Date", "None");

    /**
     * The Ordinamenti.
     */
    ObservableList<String> ordinamenti = FXCollections.observableArrayList(
            "Number of likes", "Number of dislikes", "Number of comments", "None");

    private static String autore;
    private static String timestamp;
    private static int id;
    private static String titolo;

    /**
     * Gets id.
     *
     * @return the id
     */
    public static int getId() {
        return id;
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

    private Text chooseId(int i){
        return switch (i) {
            case 0 -> id1;
            case 1 -> id2;
            case 2 -> id3;
            case 3 -> id4;
            case 4 -> id5;
            case 5 -> id6;
            default -> new Text();
        };

    }

    /**
     * Initialize.
     *
     * @throws IOException the io exception
     * @throws ExecutionException the execution exception
     */
    @FXML
    void initialize() throws IOException, ExecutionException {
        setSuggestedArticles();
        setSuggestedArticles();

        if(LoginPageView.getLoggedRole().equals("moderator")) {
            statisticsButton.setDisable(false);
        } else {
            statisticsButton.setDisable(true);
        }

        if(LoginPageView.getLoggedRole().equals("influencer")) {
            addarticle.setVisible(true);
            writetext.setVisible(true);
        } else {
            addarticle.setVisible(false);
            writetext.setVisible(false);
        }
    }

    /**
     * Return to homepage.
     *
     * @throws IOException the io exception
     */
    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

    /**
     * Go to games.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToGames() throws IOException {
        App.setRoot("HomepageGames");
    }

    /**
     * Go to groups.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToGroups() throws IOException {
        App.setRoot("HomepageGroups");
    }

    /**
     * Go to friends.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToFriends() throws IOException {
        App.setRoot("HomepageUsers");
    }

    /**
     * Go to settings.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToSettings() throws IOException {
        App.setRoot("ProfileSettingsPageView");
    }

    /**
     * Go to statistics.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToStatistics() throws IOException {
        App.setRoot("HomepageModeratorAnalytics");
    }

    /**
     * Logout.
     *
     * @throws IOException the io exception
     */
    @FXML
    void logout() throws IOException {
        App.setRoot("LoginPageView");
        LoginPageView.logout();
    }

    /**
     * Show filters.
     *
     * @throws IOException the io exception
     */
    @FXML
    void showFilters () {
        filtri.setItems(filters);
    }

    /**
     * Show ordering.
     *
     * @throws IOException the io exception
     */
    @FXML
    void showOrdering () {
        Scene scene = App.getScene();
        ComboBox order = (ComboBox) scene.lookup("#order");

        order.setItems(ordinamenti);
    }

    /**
     * Sets filters.
     */
    @FXML
    void setFilters() {

        if(filtri.getSelectionModel().getSelectedItem() != null){
            String filtroSelezionato = filtri.getSelectionModel().getSelectedItem().toString();
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

    /**
     * Sets suggested articles.
     *
     * @throws IOException the io exception
     * @throws ExecutionException the execution exception
     */
    @FXML
    void setSuggestedArticles() throws ExecutionException {
        ArticlesPagesDBController home = new ArticlesPagesDBController();

        if (savedID.isEmpty()) {
            logger.info("cache vuota");
            List<ArticleBean> list = home.listSuggestedArticles(LoginPageView.getLoggedUser(), 6);
            showArticles(list);
        } else {
            int i = 0;
            logger.info("cache piena");
            for (Integer id : savedID) {
                ArticleBean a = cache.getDataIfPresent(id);
                if (a != null && a.getTitle() != null) {
                    if (i < savedID.size() && i < 6) {
                        TitledPane ar = chooseArticle(i);
                        Text au = chooseAuthor(i);
                        Text ti = chooseTimestamp(i);
                        Text st = chooseStatistics(i);
                        Text identificatore = chooseId(i);

                        identificatore.setText(String.valueOf(a.getId()));
                        ar.setText(a.getTitle());
                        au.setText(a.getAuthor());
                        ti.setText(String.valueOf(a.getTimestamp()));
                        st.setText("Comments: " + a.getNumberComments() + ", likes:" + a.getNumberLikes() + ", unlikes: " + a.getNumberDislike() );
                        i++;
                    }
                }
            }
        }

        showFilters();
    }

    /**
     * Show articles.
     */
    private void showArticles(List<ArticleBean> list) {
        ArticlesPagesDBController home = new ArticlesPagesDBController();

        logger.info("show articles");
        if (list != null) {
            for(int i = 0; i < 6; i++) {

                TitledPane ar = chooseArticle(i);
                Text aut = chooseAuthor(i);
                Text tim = chooseTimestamp(i);
                Text st = chooseStatistics(i);
                Text identificatore = chooseId(i);

                if (i < list.size()) {
                    ArticleBean a = list.get(i);

                    // caching
                    savedID.add(a.getId());

                    identificatore.setText(String.valueOf(a.getId() + 1));
                    ar.setText(a.getTitle());
                    aut.setText(a.getAuthor());
                    tim.setText(String.valueOf(a.getTimestamp()));
                    st.setText("Comments: " + a.getNumberComments() + ", likes:" + a.getNumberLikes() + ", unlikes: " + a.getNumberDislike());
                } else {
                    identificatore.setText("");
                    ar.setText("");
                    aut.setText("");
                    tim.setText("");
                    st.setText("");
                }
            }
        }
    }

    /**
     * Go to article.
     *
     * @param event the event
     * @throws IOException the io exception
     */
    @FXML
    void goToArticle (MouseEvent event) throws IOException {

        AnchorPane articolo = (AnchorPane) event.getSource();
        String idArticle = articolo.getId();

        int index = Integer.parseInt(idArticle.substring(idArticle.length() - 1));
        logger.info("index" + index);
        Text idSelected = chooseId(index - 1);
        id = Integer.parseInt(idSelected.getText());
        App.setRoot("ArticlePageView");
    }

    /**
     * Filter research.
     *
     * @throws IOException the io exception
     */
    @FXML
    void filterResearch () throws IOException {
        ArticlesPagesDBController controller = new ArticlesPagesDBController();

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

    /**
     * The type Number of like comparator.
     */
    static class NumberOfLikeComparator implements Comparator<ArticleBean> {
            @Override
            public int compare(ArticleBean a, ArticleBean b) {
                return Double.compare(b.getNumberLikes(), a.getNumberLikes());
            }
        }

        /**
         * The type Number of dislike comparator.
         */
    static class NumberOfDislikeComparator implements Comparator<ArticleBean> {
            @Override
            public int compare(ArticleBean a, ArticleBean b) {
                return Integer.compare(b.getNumberDislike(), a.getNumberDislike());
            }
        }

        /**
         * The type Number of comments.
         */
    static class NumberOfComments implements Comparator<ArticleBean> {
            @Override
            public int compare(ArticleBean a, ArticleBean b) {
                return Integer.compare(b.getNumberComments(), a.getNumberComments());
            }
        }

    private void showFilteringResult(List<ArticleBean> filteringResult) {
        if (filteringResult != null) {

            for (int i = 0; i < 6; i++) {
                TitledPane articolo = chooseArticle(i);
                Text author = chooseAuthor(i);
                Text timestamp = chooseTimestamp(i);
                Text stats = chooseStatistics(i);
                Text identificatore = chooseId(i);

                if(i < filteringResult.size()) {
                    ArticleBean g = filteringResult.get(i);
                    identificatore.setText(String.valueOf(g.getId()));
                    articolo.setText(g.getTitle());
                    author.setText(g.getAuthor());
                    timestamp.setText(String.valueOf(g.getTimestamp()));
                    stats.setText("Comments: " + g.getNumberComments() + ", likes:" + g.getNumberLikes() + ", unlikes: " + g.getNumberDislike());
                } else {
                    articolo.setText("");
                    author.setText("");
                    timestamp.setText("");
                    stats.setText("");
                    identificatore.setText("");
                }
            }
        }
    }

    /**
     * Add article.
     *
     * @throws IOException the io exception
     */
    @FXML
    void addArticle () throws IOException {
        App.setRoot("AddArticle");

    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public static String getAuthor() {
        return autore;
    }
    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public static String getTimestamp() {
        return timestamp;
    }
    /**
     * Gets titolo.
     *
     * @return the titolo
     */
    public static String getTitolo() {
        return titolo;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public static void setAuthor(String author) {
        autore = author;
    }
    /**
     * Sets timestamp.
     *
     * @param ts the ts
     */
    public static void setTimestamp(String ts) {
        timestamp = ts;
    }
    /**
     * Sets title.
     *
     * @param title the title
     */
    public static void setTitle(String title) {
        titolo = title;
    }
    /**
     * Sets id.
     *
     * @param ident the ident
     */
    public static void setId(int ident) {
        id = ident;
    }

}
