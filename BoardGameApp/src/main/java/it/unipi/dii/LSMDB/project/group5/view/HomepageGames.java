package it.unipi.dii.LSMDB.project.group5.view;

import com.google.common.collect.Lists;
import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.controller.GamesReviewsRatesDBController;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class HomepageGames {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    int giàCaricato = -1;

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
            "Category", "Number of players", "Release year", "None");

    List<GameBean> mostrati = Lists.newArrayList();

    private static String game;

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
    void goToGame(MouseEvent event) throws IOException {
        logger.info("Carico " + event.getTarget().getClass());
        AnchorPane foo = new AnchorPane();

        if(event.getTarget().getClass() == foo.getClass()){
            AnchorPane pane = (AnchorPane) event.getTarget();
            TitledPane tp = (TitledPane) App.getScene().lookup("#full" + pane.getId());

            game = tp.getText();
            App.setRoot("GamePageView");
        }
    }

    @FXML
    void setSuggestedGames() throws IOException {

        if(giàCaricato == -1) {
            GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();
            List<GameBean> list = controller.neo4jListSuggestedGames(LoginPageView.getLoggedUser());

            if (list != null) {
                System.out.println("Lunghezza lista " + list.size());
                for (int i = 0; i < list.size() && i < 6; i++) {
                    GameBean g = list.get(i);
                    TitledPane gioco = (TitledPane) App.getScene().lookup("#fullgame" + (i + 1));
                    Text category1 = (Text) App.getScene().lookup("#category1" + (i + 1));
                    Text category2 = (Text) App.getScene().lookup("#category2" + (i + 1));

                    gioco.setText(g.getName());

                    if(!g.getCategory1().equals("null")) {
                        category1.setText(g.getCategory1());
                    } else {
                        category1.setText("");
                    }
                    if(!g.getCategory2().equals("null")) {
                        category2.setText(g.getCategory2());
                    } else {
                        category2.setText("");
                    }
                }
            }

            giàCaricato = 1;
        }
    }

    @FXML
    void showFilters () throws IOException {
        logger.info("Carico i filtri");
        Scene scene = App.getScene(); // recupero la scena della signup
        ComboBox filtri = (ComboBox) scene.lookup("#filter");

        filtri.setItems(filters);

    }

    @FXML
    void setFilters() {
        Scene scene = App.getScene(); // recupero la scena della signup
        Slider players = (Slider) scene.lookup("#players");
        Text numPlayers = (Text) scene.lookup("#numplayers");
        ComboBox categorie = (ComboBox) scene.lookup("#category");
        TextField releseaYear = (TextField) scene.lookup("#year");
        ComboBox filtri = (ComboBox) scene.lookup("#filter");

        if(filtri.getSelectionModel().getSelectedItem() != null){
            String filtroSelezionato = filtri.getSelectionModel().getSelectedItem().toString();
            if(!filtroSelezionato.equals("None")){
                switch(filtroSelezionato){
                    case "Category":
                        categorie.setVisible(true);
                        players.setVisible(false);
                        numPlayers.setVisible(false);
                        releseaYear.setVisible(false);
                        break;
                    case "Number of players":
                        categorie.setVisible(false);
                        players.setVisible(true);
                        numPlayers.setVisible(true);
                        releseaYear.setVisible(false);
                        break;
                    case "Release year":
                        categorie.setVisible(false);
                        players.setVisible(false);
                        numPlayers.setVisible(false);
                        releseaYear.setVisible(true);
                        break;
                    default:
                        categorie.setVisible(false);
                        players.setVisible(false);
                        numPlayers.setVisible(false);
                        releseaYear.setVisible(false);
                        break;
                }
            } else {
                categorie.setVisible(false);
                players.setVisible(false);
                numPlayers.setVisible(false);
                releseaYear.setVisible(false);
            }
        }
    }

    @FXML
    void filterResearch () throws IOException {
        // filtra i risultati in base alle impostazioni passate
        GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();

        ComboBox categoria = (ComboBox) App.getScene().lookup("#category");
        Slider players = (Slider) App.getScene().lookup("#players");
        TextField data = (TextField) App.getScene().lookup("#year");
        ComboBox order = (ComboBox) App.getScene().lookup("#order");

        String filtraPerCategoria = " ";
        int filtraGiocatori = 0;
        int filtraPerAnno = 0;

        List<GameBean> filteredGames = Lists.newArrayList();
        List<GameBean> sortedList = Lists.newArrayList();
        List<GameBean> filteringResult = Lists.newArrayList();

        if(categoria.isVisible() && categoria.getSelectionModel().getSelectedItem() != null) {
            // filtraggio per categoria, crea una lista con tutti i giochi appartenenti alla categoria data
            int index1 = categoria.getSelectionModel().getSelectedIndex();
            filtraPerCategoria = categorie.get(index1);
            filteredGames = controller.filterByCategory(filtraPerCategoria);
        } else if(data.isVisible() && !data.getText().equals("")) {
            if (Integer.parseInt(data.getText()) > 1900 || Integer.parseInt(data.getText()) < 2022) {
                // filtraggio per anno
                filtraPerAnno = Integer.parseInt(data.getText());
                filteredGames = controller.filterByYear(filtraPerAnno);
            }
        } else if (players.isVisible() && (players.getValue() > 0 || players.getValue() <= 16) ){
            // filtraggio per numero di giocatori
            filtraGiocatori = (int) players.getValue();
            filteredGames = controller.filterByPlayers(filtraGiocatori);
        }

        // rimuovo i doppioni
        filteringResult = new ArrayList<GameBean>(new HashSet<GameBean>(filteredGames));
        System.out.println("Risultati: \n" + filteringResult);
        // in filteredGames a questo punto c'e la lista dei giochi filtrati, se voglio ordinarli entro nell'if

        if(order.getSelectionModel().getSelectedItem() != null) {
            logger.info("Voglio ordinare i risultati");
            int index1 = order.getSelectionModel().getSelectedIndex();
            String ordering = ordinamenti.get(index1);

            System.out.println("Ordinamento " + ordering);

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
                filteringResult = orderingResult(filteredGames, ordering);
            }
        }

        showFilteringResult(filteringResult);
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

        System.out.println(target);
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

    private void showFilteringResult(List<GameBean> filteringResult) {
        logger.info("show filtering result");
        if (filteringResult != null) {
            System.out.println("Lunghezza lista " + filteringResult.size());
            for (int i = 0; i < filteringResult.size() && i < 6; i++) {
                GameBean g = (GameBean) filteringResult.get(i);
                TitledPane gioco = (TitledPane) App.getScene().lookup("#fullgame" + (i + 1));
                Text category1 = (Text) App.getScene().lookup("#category1" + (i + 1));
                Text category2 = (Text) App.getScene().lookup("#category2" + (i + 1));

                gioco.setText(g.getName());

                if(g.getCategory1() == null || !g.getCategory1().equals("null")) {
                    category1.setText(g.getCategory1());
                } else {
                    category1.setText("");
                }
                if(g.getCategory1() == null || !g.getCategory2().equals("null")) {
                    category2.setText(g.getCategory2());
                } else {
                    category2.setText("");
                }
            }
        }
    }

    @FXML
    void logout() throws IOException {
        App.setRoot("login");
        LoginPageView.logout();
    }

    @FXML
    void aggiorna() {
        Slider player = (Slider) App.getScene().lookup("#players");
        Text numplayers = (Text) App.getScene().lookup("#numplayers");

        numplayers.setText(String.valueOf((int)(Math.round(player.getValue()))));
    }

    @FXML
    void caricaCategorie() throws IOException {
        Scene scene = App.getScene(); // recupero la scena della signup
        ComboBox cat1 = (ComboBox) scene.lookup("#category");

        cat1.setItems(categorie);
    }
    @FXML
    void caricaOrdinamenti() throws IOException {
        Scene scene = App.getScene(); // recupero la scena della signup
        ComboBox cat1 = (ComboBox) scene.lookup("#order");

        cat1.setItems(ordinamenti);
    }


    public static String getGame(){
        return game;
    }
}
