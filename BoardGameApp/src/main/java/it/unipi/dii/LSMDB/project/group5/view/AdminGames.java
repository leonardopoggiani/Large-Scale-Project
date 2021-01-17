package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import it.unipi.dii.LSMDB.project.group5.controller.AnalyticsDBController;
import it.unipi.dii.LSMDB.project.group5.controller.GamesPagesDBController;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AdminGames {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    AnalyticsDBController controller = new AnalyticsDBController();

    ObservableList<String> gameStatistic = FXCollections.observableArrayList("Least rated game per category", "Least rated game per year");

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

    ObservableList<String> year = FXCollections.observableArrayList("2020","2019","2018","2017","2016",
            "2015","2014","2013","2012","2011","2010","2009","2008","2007","2006","2005","2004","2003","2002","2001","2000",
            "1999","1998","1997","1996","1995","1994", "1993","1992","1991","1990","1989","1988","1987","1986","1985",
            "1984","1983","1982","1981");

    @FXML
    ComboBox games;

    @FXML
    ComboBox choosenCategory;

    @FXML
    Text game1;

    @FXML
    Text game2;

    @FXML
    Text game3;

    @FXML
    TextField delete;

    @FXML
    Button remove;

    @FXML
    ImageView tic;

    @FXML
    void returnToStatistics() throws IOException {
        App.setRoot("adminHomepage");
    }

    @FXML
    void goToAdminGames() throws IOException {
        App.setRoot("adminGames");
    }

    @FXML
    void goToAdminUsers() throws IOException {
        App.setRoot("adminUsers");
    }

    @FXML
    void logout() throws IOException {
        App.setRoot("LoginPageView");
        LoginPageView.logout();
    }

    @FXML
    void initialize() {
        games.setItems(gameStatistic);


        game1.setText("");
        game2.setText("");
        game3.setText("");

    }

    @FXML
    void displayGameStatisticResult() {
        String statistic = gameStatistic.get(games.getSelectionModel().getSelectedIndex());
        if(statistic.equals("Least rated game per category")) {
            choosenCategory.setItems(categorie);
        } else if(statistic.equals("Least rated game per year")) {
            choosenCategory.setItems(year);
        } else {

        }
    }


    private Text chooseGame(int i) {
        return switch(i) {
            case 0 -> game1;
            case 1 -> game2;
            case 2 -> game3;
            default -> new Text();
        };
    }

    @FXML
    void displayCategoryStatisticResult() {
        if(gameStatistic.get(games.getSelectionModel().getSelectedIndex()).equals("Least rated game per category")) {
            List<GameBean> lista = controller.showLeastRatedGames("category", categorie.get(choosenCategory.getSelectionModel().getSelectedIndex()));

            logger.info("least rated game category, size " + lista.size());

            for (int i = 0; i < 3; i++) {
                Text game_i = chooseGame(i);
                if (i < lista.size()) {
                    logger.info("game " + lista.get(i).getName());
                    game_i.setText(lista.get(i).getName());
                } else {
                    game_i.setText("");
                }
            }
        } else {
            List<GameBean> lista = controller.showLeastRatedGames("year", year.get(choosenCategory.getSelectionModel().getSelectedIndex()));

            logger.info("least rated game year");

            for (int i = 0; i < 3; i++) {
                Text game_i = chooseGame(i);
                if (i < lista.size()) {
                    game_i.setText(lista.get(i).getName());
                } else {
                    game_i.setText("");
                }
            }
        }
    }

    @FXML
    private void searchGame() {
        GamesPagesDBController gameController = new GamesPagesDBController();
        GameBean gioco = gameController.showGame(delete.getText());

        if(gioco != null) {
            remove.setDisable(false);
            tic.setVisible(true);
        } else {
            remove.setDisable(true);
            tic.setVisible(false);
        }
    }

    @FXML
    private void removeGame(){
        GamesPagesDBController gameController = new GamesPagesDBController();
        // gameController.deleteGame(delete.getText());
    }

}
