package it.unipi.dii.LSMDB.project.group5.view;

import com.google.common.collect.Lists;
import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import it.unipi.dii.LSMDB.project.group5.cache.GamesCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.controller.GamesPageDBController;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class HomepageGames {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    GamesCache cache = GamesCache.getInstance();
    private static List<String> savedGames = Lists.newArrayList();
    private static String game;

    ObservableList<String> ordinamenti = FXCollections.observableArrayList(
            "Number of reviews", "Number of ratings", "Average ratings", "None");

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
            "Exploration:1020","Word-game:1025","Video Game Theme:1101", "None");

    ObservableList<String> filters = FXCollections.observableArrayList(
            "Category", "Number of players", "Release year", "Name", "None");

    @FXML
    TitledPane fullgame1;

    @FXML
    TitledPane fullgame2;

    @FXML
    TitledPane fullgame3;

    @FXML
    TitledPane fullgame4;

    @FXML
    TitledPane fullgame5;

    @FXML
    TitledPane fullgame6;

    @FXML
    Text rating1;

    @FXML
    Text rating2;

    @FXML
    Text rating3;

    @FXML
    Text rating4;

    @FXML
    Text rating5;

    @FXML
    Text rating6;

    @FXML
    Text number1;

    @FXML
    Text number2;

    @FXML
    Text number3;

    @FXML
    Text number4;

    @FXML
    Text number5;

    @FXML
    Text number6;

    @FXML
    ComboBox filter;

    @FXML
    Slider players;

    @FXML
    Text numplayers;

    @FXML
    ComboBox category;

    @FXML
    TextField year;

    @FXML
    TextField name;

    @FXML
    ComboBox order;

    @FXML
    void initialize() throws IOException, ExecutionException {
        setSuggestedGames();
        setSuggestedGames();
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

    private Text chooseNumberOfRating(int i){
        return switch (i) {
            case 1 -> number1;
            case 2 -> number2;
            case 3 -> number3;
            case 4 -> number4;
            case 5 -> number5;
            case 6 -> number6;
            default -> new Text();
        };
    }

    private Text chooseRating(int i){
        return switch (i) {
            case 1 -> rating1;
            case 2 -> rating2;
            case 3 -> rating3;
            case 4 -> rating4;
            case 5 -> rating5;
            case 6 -> rating6;
            default -> new Text();
        };

    }

    private TitledPane chooseGame(int i){
        return switch (i) {
            case 1 -> fullgame1;
            case 2 -> fullgame2;
            case 3 -> fullgame3;
            case 4 -> fullgame4;
            case 5 -> fullgame5;
            case 6 -> fullgame6;
            default -> new TitledPane();
        };

    }

    @FXML
    void goToGame(MouseEvent event) throws IOException {
        AnchorPane gamePressed = (AnchorPane) event.getSource();
        int index =  Integer.parseInt(gamePressed.getId().substring(gamePressed.getId().length() - 1));

        TitledPane gameSelected = chooseGame(index);
        game = gameSelected.getText();
        logger.info("carico " + game);
        App.setRoot("GamePageView");
    }

    @FXML
    void setSuggestedGames() throws IOException, ExecutionException {

        GamesPageDBController controller = new GamesPageDBController();
        if (savedGames.isEmpty()) {
            // non ho giochi salvati in cache
            logger.info("cache vuota");
            List<GameBean> list = controller.neo4jListSuggestedGames(LoginPageView.getLoggedUser());
            showGames(list);
        } else {
            int i = 0;
            logger.info("cache piena");
            for (String game_i : savedGames) {
                GameBean g = cache.getDataIfPresent(game_i);
                if (g != null && g.getName() != null) {
                    if (i < savedGames.size() && i < 6) {

                        TitledPane gioco = chooseGame(i + 1);
                        Text ratings = chooseRating(i + 1);
                        Text number = chooseNumberOfRating(i + 1);

                        gioco.setText(g.getName());
                        ratings.setText(String.valueOf(Math.round(g.getAvgRating())));
                        number.setText(String.valueOf(Math.round(g.getNumVotes())));

                        i++;
                    }
                }
            }
        }

        showFilters();
        caricaCategorie();
        caricaOrdinamenti();
    }


    @FXML
    void showFilters () throws IOException {
        logger.info("Carico i filtri");
        filter.setItems(filters);
    }

    @FXML
    void setFilters() {
        if(filter.getSelectionModel().getSelectedItem() != null){
            String filtroSelezionato = filter.getSelectionModel().getSelectedItem().toString();
            if(!filtroSelezionato.equals("None")){
                switch(filtroSelezionato){
                    case "Category":
                        category.setVisible(true);
                        players.setVisible(false);
                        numplayers.setVisible(false);
                        year.setVisible(false);
                        name.setVisible(false);
                        break;
                    case "Number of players":
                        category.setVisible(false);
                        players.setVisible(true);
                        numplayers.setVisible(true);
                        year.setVisible(false);
                        name.setVisible(false);
                        break;
                    case "Release year":
                        category.setVisible(false);
                        players.setVisible(false);
                        numplayers.setVisible(false);
                        year.setVisible(true);
                        name.setVisible(false);
                        break;
                    case "Name":
                        category.setVisible(false);
                        players.setVisible(false);
                        numplayers.setVisible(false);
                        year.setVisible(false);
                        name.setVisible(true);
                        break;
                    default:
                        category.setVisible(false);
                        players.setVisible(false);
                        numplayers.setVisible(false);
                        year.setVisible(false);
                        name.setVisible(false);
                        break;
                }
            } else {
                category.setVisible(false);
                players.setVisible(false);
                numplayers.setVisible(false);
                year.setVisible(false);
                name.setVisible(false);
            }
        }
    }

    @FXML
    void filterResearch () throws IOException {
        // filtra i risultati in base alle impostazioni passate
        GamesPageDBController controller = new GamesPageDBController();

        String filtraPerCategoria = " ";
        int filtraGiocatori = 0;
        int filtraPerAnno = 0;

        List<GameBean> filteredGames = Lists.newArrayList();
        List<GameBean> sortedList = Lists.newArrayList();
        List<GameBean> filteringResult = Lists.newArrayList();

        if(category.isVisible() && category.getSelectionModel().getSelectedItem() != null) {
            // filtraggio per categoria, crea una lista con tutti i giochi appartenenti alla categoria data
            int index1 = category.getSelectionModel().getSelectedIndex();
            filtraPerCategoria = categorie.get(index1);
            filteredGames = controller.filterByCategory(filtraPerCategoria);
        } else if(year.isVisible() && !year.getText().equals("")) {
            if (Integer.parseInt(year.getText()) > 1900 || Integer.parseInt(year.getText()) < 2022) {
                // filtraggio per anno
                filtraPerAnno = Integer.parseInt(year.getText());
                filteredGames = controller.filterByYear(filtraPerAnno);
                logger.info("data:" + filteredGames.size());
            }
        } else if (players.isVisible() && (players.getValue() > 0 || players.getValue() <= 16) ){
            // filtraggio per numero di giocatori
            filtraGiocatori = (int) players.getValue();
            filteredGames = controller.filterByPlayers(filtraGiocatori);
            logger.info("players:" + filteredGames.size());
        } else if(name.isVisible() && (!name.getText().equals(""))){
            filteredGames = controller.filterByName(name.getText());
        }

        // rimuovo i doppioni
        filteringResult = new ArrayList<GameBean>(new HashSet<GameBean>(filteredGames));
        logger.info("filteringResult:" + filteringResult.size());

        // in filteredGames a questo punto c'e la lista dei giochi filtrati, se voglio ordinarli entro nell'if

        if(order.getSelectionModel().getSelectedItem() != null) {
            logger.info("Voglio ordinare i risultati");
            int index1 = order.getSelectionModel().getSelectedIndex();
            String ordering = ordinamenti.get(index1);

            if(filteredGames.isEmpty()) {
                // qui ci entro solo se non ho selezionato nessun filtro e quindi applico l'ordinamento a tutti i giochi
                if(!ordering.equals("None")){
                    switch (ordering) {
                        case "Average ratings" -> {
                            logger.info("Ordino per rating");
                            sortedList = controller.orderByAvgRating();
                        }
                        case "Number of reviews" -> {
                            logger.info("Ordino per reviews");
                            sortedList = controller.orderByNumReviews();
                        }
                        case "Number of ratings" -> {
                            logger.info("Ordino per votes");
                            sortedList = controller.orderByNumVotes();
                        }
                        default -> logger.info("Non ordino");
                    }
                    filteringResult = new ArrayList<GameBean>(new HashSet<GameBean>(sortedList));
                }
            } else {
                logger.info("non ordino");
                filteringResult = orderingResult(filteredGames, ordering);
            }
        }

        savedGames.clear();
        showGames(filteringResult);
    }

    private List<GameBean> orderingResult(List<GameBean> target, String mode) {

        if(!mode.equals("None")){
            switch (mode) {
                case "Average ratings" -> {
                    logger.info("Ordino per rating");
                    target.sort(new AverageRatingComparator());
                }
                case "Number of reviews" -> {
                    logger.info("Ordino per reviews");
                    target.sort(new NumberOfReviewsComparator());
                }
                case "Number of ratings" -> {
                    logger.info("Ordino per rating");
                    target.sort(new NumberOfRatingsComparator());
                }
                default -> logger.info("Non ordino");
            }
        }

        return target;
    }

    static class AverageRatingComparator implements Comparator<GameBean> {
        @Override
        public int compare(GameBean a, GameBean b) {
            return Double.compare(b.getAvgRating(), a.getAvgRating());
        }
    }

    static class NumberOfReviewsComparator implements Comparator<GameBean> {
        @Override
        public int compare(GameBean a, GameBean b) {
            return Integer.compare(b.getNumReviews(), a.getNumReviews());
        }
    }

    static class NumberOfRatingsComparator implements Comparator<GameBean> {
        @Override
        public int compare(GameBean a, GameBean b) {
            return Integer.compare(b.getNumVotes(), a.getNumVotes());
        }
    }

    private void showGames(List<GameBean> games) {
        logger.info("show filtering result");
        if (games != null) {
            System.out.println("Lunghezza lista " + games.size());
            for (int i = 0; i < 6; i++) {
                TitledPane gioco = chooseGame(i + 1);
                Text ratings = chooseRating(i + 1);
                Text number = chooseNumberOfRating(i + 1);

                if(i < games.size()){
                    GameBean g = games.get(i);
                    savedGames.add(g.getName());
                    gioco.setText(g.getName());
                    ratings.setText(String.valueOf(Math.round(g.getAvgRating())));
                    number.setText(String.valueOf(Math.round(g.getNumVotes())));
                } else {
                    gioco.setText("");
                    ratings.setText("");
                    number.setText("");
                }
            }
        }
    }

    @FXML
    void logout() throws IOException {
        App.setRoot("LoginPageView");
        LoginPageView.logout();
    }

    @FXML
    void aggiorna() {
        numplayers.setText(String.valueOf((int)(Math.round(players.getValue()))));
    }

    @FXML
    void caricaCategorie(){
        category.setItems(categorie);
    }

    @FXML
    void caricaOrdinamenti() throws IOException {
        order.setItems(ordinamenti);
    }

    public static String getGame(){
        return game;
    }
}
