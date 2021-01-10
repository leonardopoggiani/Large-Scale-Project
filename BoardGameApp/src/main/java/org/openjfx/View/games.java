package org.openjfx.View;

import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.google.common.collect.Lists;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.bson.Document;
import org.openjfx.App;
import org.openjfx.Controller.ArticlesCommentsLikesDBController;
import org.openjfx.Controller.GamesReviewsRatesDBController;
import org.openjfx.Entities.Article;
import org.openjfx.Entities.InfoGame;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

public class games {

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

    private static String game;

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
    void goToGame(MouseEvent event) throws IOException {
        logger.info("Carico " + event.getTarget().getClass());
        AnchorPane foo = new AnchorPane();

        if(event.getTarget().getClass() == foo.getClass()){
            AnchorPane pane = (AnchorPane) event.getTarget();
            TitledPane tp = (TitledPane) App.getScene().lookup("#full" + pane.getId());

            game = tp.getText();
            App.setRoot("game");
        }
    }

    @FXML
    void setSuggestedGames() throws IOException {

        if(giàCaricato == -1) {
            GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();
            List<InfoGame> list = controller.neo4jListSuggestedGames(login.getLoggedUser());

            if (list != null) {
                System.out.println("Lunghezza lista " + list.size());
                for (int i = 0; i < list.size() && i < 6; i++) {
                    InfoGame g = list.get(i);
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
    void filterResearch () throws IOException {
        // filtra i risultati in base alle impostazioni passate
        GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();

        ComboBox categoria = (ComboBox) App.getScene().lookup("#category");
        Slider players = (Slider) App.getScene().lookup("#players");
        Text numplayers = (Text) App.getScene().lookup("#numplayers");
        TextField data = (TextField) App.getScene().lookup("#year");
        ComboBox order = (ComboBox) App.getScene().lookup("#order");

        String filtraPerCategoria = " ";
        int filtraGiocatori = 0;
        int filtraPerAnno = 0;
        String ordina = "";

        List<InfoGame> filteredGames0 = Lists.newArrayList();
        List<InfoGame> filteredGames1 = Lists.newArrayList();
        List<InfoGame> filteredGames2 = Lists.newArrayList();
        List<InfoGame> sortedList = Lists.newArrayList();

        if(categoria.getSelectionModel().getSelectedItem() != null) {
            // filtraggio per categoria, crea una lista con tutti i giochi appartenenti alla categoria data
            int index1 = categoria.getSelectionModel().getSelectedIndex();
            filtraPerCategoria = categorie.get(index1);
            filteredGames0 = controller.filterByCategory(filtraPerCategoria);
        }

        if(!data.getText().equals("")) {
            if (Integer.parseInt(data.getText()) > 1900 || Integer.parseInt(data.getText()) < 2022) {
                // filtraggio per anno
                filtraPerAnno = Integer.parseInt(data.getText());
                filteredGames1 = controller.filterByYear(filtraPerAnno);
            }
        }

        if (players.getValue() > 0 || players.getValue() <= 16) {
            // filtraggio per numero di giocatori
            filtraGiocatori = (int) players.getValue();
            filteredGames2 = controller.filterByPlayers(filtraGiocatori);
        }

        filteredGames0.addAll(filteredGames1);
        filteredGames0.addAll(filteredGames2);

        if(order.getSelectionModel().getSelectedItem() != null) {

            int index1 = order.getSelectionModel().getSelectedIndex();
            String ordering = ordinamenti.get(index1);

            System.out.println("Ordinamento " + ordering);

            if(!ordering.equals("None")){
                if (ordering.equals("Average ratings")) {
                    logger.info("Ordino per rating");
                    sortedList = controller.orderByAvgRating();
                } else if (ordering.equals("Number of reviews")) {
                    logger.info("Ordino per reviews");
                    sortedList = controller.orderByNumReviews();
                } else if (ordering.equals("Number of ratings")) {
                    logger.info("Ordino per votes");
                    sortedList = controller.orderByNumVotes();
                } else {
                    logger.info("Non ordino");
                }

                /*if(filteredGames0.size() != 0){
                    sortedList.retainAll(filteredGames0);
                    System.out.println(sortedList);
                }*/
                List<InfoGame> filteringResult = new ArrayList<InfoGame>(new HashSet<InfoGame>(sortedList));
                showFilteringResult(filteringResult);
            }
        } else {
            List<InfoGame> filteringResult = new ArrayList<InfoGame>(new HashSet<InfoGame>(filteredGames0));
            showFilteringResult(filteringResult);
        }
    }

    private void showFilteringResult(List<InfoGame> filteringResult) {
        logger.info("show filtering result");
        if (filteringResult != null) {
            System.out.println("Lunghezza lista " + filteringResult.size());
            for (int i = 0; i < filteringResult.size() && i < 6; i++) {
                logger.info("mostro");
                InfoGame g = (InfoGame) filteringResult.get(i);
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
        login.logout();
    }

    @FXML
    void aggiorna() {
        Slider player = (Slider) App.getScene().lookup("#players");
        Text numplayers = (Text) App.getScene().lookup("#numplayers");

        numplayers.setText(String.valueOf((int)(Math.round(player.getValue()))));
    }

    @FXML
    void caricaCategorie() throws IOException {
        logger.info("Carico le categorie");
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
