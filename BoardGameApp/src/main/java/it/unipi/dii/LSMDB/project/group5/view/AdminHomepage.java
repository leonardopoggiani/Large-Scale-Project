package it.unipi.dii.LSMDB.project.group5.view;

import com.google.common.collect.Lists;
import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.bean.*;
import it.unipi.dii.LSMDB.project.group5.controller.AnalyticsDBController;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.ArticleDBManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AdminHomepage {

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
            "Exploration:1020","Word-game:1025","Video Game Theme:1101", "None");

    ObservableList<String> gameStatistic = FXCollections.observableArrayList("Least rated game per category", "Least rated game per year");

    ObservableList<String> year = FXCollections.observableArrayList("2020","2019","2018","2017","2016",
            "2015","2014","2013","2012","2011","2010","2009","2008","2007","2006","2005","2004","2003","2002","2001","2000",
            "1999","1998","1997","1996","1995","1994", "1993","1992","1991","1990","1989","1988","1987","1986","1985",
            "1984","1983","1982","1981");

    @FXML
    ComboBox categories;

    @FXML
    Text category1;

    @FXML
    Text category2;

    @FXML
    Text category3;

    @FXML
    PieChart pie;

    @FXML
    PieChart userpie;

    @FXML
    Text user1;

    @FXML
    Text user2;

    @FXML
    Text user3;

    @FXML
    Text user4;

    @FXML
    Text user5;

    @FXML
    LineChart activity;

    @FXML
    void initialize() {
        categories.setItems(categorie);

        category1.setText("");
        category2.setText("");
        category3.setText("");

        displayUserChart();
        displayLeastRecentlyLoggedUsers();
        displayActivityChart();

    }

    private void displayActivityChart() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Users login");
        List<ActivityBean> statistiche = AnalyticsDBManager.getActivitiesStatisticsTotal();

        for(int i = 0; i < statistiche.size(); i++) {
            series.getData().add(new XYChart.Data(statistiche.get(i).getDate(),statistiche.get(i).getNumUser()));
        }
        activity.getData().add(series);
    }

    private void displayLeastRecentlyLoggedUsers() {
        List<UserBean> utenti = AnalyticsDBManager.showLessRecentLoggedUsers();
        user1.setText((utenti.get(0) == null) ? "" : utenti.get(0).getName() + " / " + utenti.get(0).getLastLogin().toString());
        user1.setText((utenti.get(1) == null) ? "" : utenti.get(1).getName() +  " / " + utenti.get(1).getLastLogin().toString());
        user1.setText((utenti.get(2) == null) ? "" : utenti.get(2).getName() +  " / " + utenti.get(2).getLastLogin().toString());
        user1.setText((utenti.get(3) == null) ? "" : utenti.get(3).getName() +  " / " + utenti.get(3).getLastLogin().toString());
        user1.setText((utenti.get(4) == null) ? "" : utenti.get(4).getName() +  " / " + utenti.get(4).getLastLogin().toString());
    }

    @FXML
    void logout() throws IOException {
        App.setRoot("LoginPageView");
        LoginPageView.logout();
    }

    @FXML
    void goToStatistics() throws IOException {
        App.setRoot("adminHomepage");
    }

    @FXML
    void goToGamesAdmin() throws IOException {
        App.setRoot("adminGames");
    }

    @FXML
    void goToUsers() throws IOException {
        App.setRoot("adminUsers");
    }

    @FXML
    void displayCategoryInfo() {
        CategoryBean categoryInfo = AnalyticsDBManager.getCategoryInfo(categorie.get(categories.getSelectionModel().getSelectedIndex()));

        category1.setText(String.valueOf(categoryInfo.getTotGames()));
        category2.setText(String.valueOf(categoryInfo.getAvgRatingTot()));
        category3.setText(String.valueOf(categoryInfo.getNumRatesTot()));

        // pie.setData();
    }

    @FXML
    void displayUserChart() {
        List<CountryBean> lista = AnalyticsDBManager.getUsersFromCountry();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();

        for(int i = 0; i < lista.size(); i++) {
            logger.info(lista.get(i).getCountry());
            pieChartData.add(new PieChart.Data(lista.get(i).getCountry(), lista.get(i).getNumUser()));

            if(i == 4)
                break;
        }

        userpie.setData(pieChartData);
        userpie.setLabelsVisible(true);
        userpie.setLabelLineLength(10);
        userpie.setLegendSide(Side.LEFT);
    }
}
