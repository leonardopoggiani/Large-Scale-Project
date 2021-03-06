package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.bean.CategoryBean;
import it.unipi.dii.lsmdb.project.group5.bean.GameBean;
import it.unipi.dii.lsmdb.project.group5.controller.AnalyticsDBController;
import it.unipi.dii.lsmdb.project.group5.controller.GamesPagesDBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

/**
 * The view of Admin games.
 */
public class AdminGames {

    /**
     * The Controller.
     */
    AnalyticsDBController controller = new AnalyticsDBController();

    /**
     * The Game statistic.
     */
    ObservableList<String> gameStatistic = FXCollections.observableArrayList("Least rated game per category", "Least rated game per year");

    /**
     * The Categorie.
     */
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

    /**
     * The Years.
     */
    ObservableList<String> years = FXCollections.observableArrayList("2020","2019","2018","2017","2016",
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
    Text text;

    @FXML
    TextField name;

    @FXML
    TextField year;

    @FXML
    ComboBox category;

    @FXML
    TextField minplayer;

    @FXML
    TextField maxplayer;

    @FXML
    TextField publisher;

    @FXML
    TextField minage;

    @FXML
    TextField maxage;

    @FXML
    CheckBox cooperative;

    @FXML
    Button add;

    @FXML
    PieChart pie;

    /**
     * Return to statistics.
     *
     * @throws IOException the io exception
     */
    @FXML
    void returnToStatistics() throws IOException {
        App.setRoot("adminHomepage");
    }

    /**
     * Go to admin games.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToAdminGames() throws IOException {
        App.setRoot("adminGames");
    }

    /**
     * Go to admin users.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToAdminUsers() throws IOException {
        App.setRoot("adminUsers");
    }


    /**
     * return the Text of the choosen game
     */
    private Text chooseGame(int i) {
        return switch(i) {
            case 0 -> game1;
            case 1 -> game2;
            case 2 -> game3;
            default -> new Text();
        };
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
     * Initialize.
     */
    @FXML
    void initialize() {
        games.setItems(gameStatistic);


        game1.setText("");
        game2.setText("");
        game3.setText("");

        category.setItems(categorie);
        displayCategoryDistribution();
        displayUserChart();

    }

    /**
     * Display game statistic result.
     */
    @FXML
    void displayGameStatisticResult() {
        choosenCategory.getSelectionModel().clearSelection();
        if(games.getSelectionModel().getSelectedIndex() != -1){
            if(games.getSelectionModel().getSelectedIndex() == 0) {
                choosenCategory.setItems(categorie);
            } else {
                choosenCategory.setItems(years);
            }
        }
    }


    /**
     * Display category statistic result.
     */
    @FXML
    void displayCategoryStatisticResult() {
        if(choosenCategory.getSelectionModel().getSelectedIndex() != -1){

            if(games.getSelectionModel().getSelectedIndex() == 0)
            {

                List<GameBean> lista = controller.showLeastRatedGames("category", categorie.get(choosenCategory.getSelectionModel().getSelectedIndex()));
                for (int i = 0; i < 3; i++) {
                    Text game_i = chooseGame(i);
                    if (i < lista.size()) {
                        game_i.setText(lista.get(i).getName() + ", votes: " + lista.get(i).getNumVotes());
                    } else {
                        game_i.setText("");
                    }

                }
            } else {
                List<GameBean> lista = controller.showLeastRatedGames("year", years.get(choosenCategory.getSelectionModel().getSelectedIndex()));

                for (int i = 0; i < 3; i++) {
                    Text game_i = chooseGame(i);
                    if (i < lista.size()) {
                        game_i.setText(lista.get(i).getName() + ", votes: " + lista.get(i).getNumVotes());
                    } else {
                        game_i.setText("");
                    }
                }
            }
        }
    }

    /**
     * search for a game to delete.
     */
    @FXML
    private void searchGame() {
        GamesPagesDBController gameController = new GamesPagesDBController();
        GameBean gioco = gameController.showGame(delete.getText());

        if(gioco != null && gioco.getName() != null) {
            remove.setDisable(false);
            tic.setVisible(true);
        } else {
            remove.setDisable(true);
            tic.setVisible(false);
        }
    }

    /**
     * remove the game searched.
     */
    @FXML
    private void removeGame(){
        GamesPagesDBController gameController = new GamesPagesDBController();
        if(gameController.deleteGame(delete.getText()) ) {
            text.setVisible(true);
            delete.setText("");
        } else {
            delete.setStyle("-fx-background-color: red;");
        }
    }

    /**
     * add the game (if possible).
     */
    @FXML
    private void addGame(){
        GamesPagesDBController gameController = new GamesPagesDBController();
        if(gameController.addGame(new GameBean(name.getText(), Integer.parseInt(year.getText()), categorie.get(category.getSelectionModel().getSelectedIndex()),
                minplayer.getText(), maxplayer.getText(), publisher.getText(), minage.getText(), maxage.getText(), cooperative.isSelected())))
        {
            add.setStyle("-fx-background-color: green;");
        } else {
            add.setStyle("-fx-background-color: red;");
        }
    }

    /**
     * display the games category distribution.
     */
    private void displayCategoryDistribution() {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();

        List<CategoryBean> categoryInfo2 = controller.getGamesDistribution();

        for(int i = 0; i < categoryInfo2.size(); i++) {
            pieChartData.add(new PieChart.Data(categoryInfo2.get(i).getName(), categoryInfo2.get(i).getTotGames()));

            if(i == 4) {
                break;
            }
        }

        pie.setData(pieChartData);
    }

    /**
     * Display user chart.
     */
    @FXML
    void displayUserChart() {
        List<CategoryBean> lista = controller.getGamesDistribution();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();

        for(int i = 0; i < lista.size(); i++) {
            pieChartData.add(new PieChart.Data(lista.get(i).getName(), lista.get(i).getTotGames()));

            if(i == 4)
                break;
        }

        pie.setData(pieChartData);
        pie.setLabelsVisible(true);
        pie.setLabelLineLength(10);
        pie.setLegendSide(Side.LEFT);
    }
}
