package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.bean.ActivityBean;
import it.unipi.dii.lsmdb.project.group5.bean.AgeBean;
import it.unipi.dii.lsmdb.project.group5.bean.CategoryBean;
import it.unipi.dii.lsmdb.project.group5.bean.VersatileUser;
import it.unipi.dii.lsmdb.project.group5.controller.AnalyticsDBController;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

/**
 * @author leonardopoggiani
 * Gestore della homepage. Al caricamento vengono mostrati gli articoli suggeriti, in base ad influencer
 * seguiti oppure a categorie di giochi inserite.
 */

public class AdminHomepage {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    AnalyticsDBController controller = new AnalyticsDBController();

    ObservableList<String> categorie = FXCollections.observableArrayList(
            "Math:1104", "Card Game:1002", "Humor:1079", "Party Game:1030",
            "Number:1098", "Puzzle:1028", "Dice:1017", "Sports:1038",
            "Book:1117", "Fantasy:1010", "Miniatures:1047", "Wargame:1019",
            "Napoleonic:1051", "Children's Game:1041", "Memory:1045",
            "Educational:1094", "Medical:2145", "Animals:1089", "Racing:1031",
            "Adventure:1022", "Travel:1097", "Abstact Strategy:1009",
            "Economic:1021", "Trains:1034", "Transportation:1011", "Real-time:1037",
            "Action/Dexterity:1032", "Ancient:1050", "Collectible Components:1044",
            "Fighting:1046", "Movies/TV/Radio Theme:1064", "Bluffing:1023",
            "Zombies:2481", "Medieval:1035", "Negotiation:1026", "World War II: 1049",
            "Spies/Secret Agents:1081", "Deduction:1039", "Murder/Mystery:1040",
            "Aviation/Flight:2650", "Territory Building:1086", "Modern Warfare:1069",
            "Print & Play:1120", "Novel-Based:1093", "Puzzle:1028", "Science Fiction:1016",
            "Exploration:1020", "Word-game:1025", "Video Game Theme:1101", "None");

    ObservableList<String> gameStatistic = FXCollections.observableArrayList("Least rated game per category", "Least rated game per year");

    ObservableList<String> year = FXCollections.observableArrayList("2020", "2019", "2018", "2017", "2016",
            "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", " 2006", "2005", "2004", "2003", "2002", "2001", "2000",
            "1999", "1998", "1997", "1996", "1995", "1994", "1993", "1992", "1991", "1990", "1989", "1988", "1987", "1986", "1985",
            "1984", "1983", "1982", "1981");

    @FXML
    ComboBox categories;

    @FXML
    Text category1;

    @FXML
    Text category2;

    @FXML
    Text category3;

    @FXML
    LineChart activity;

    @FXML
    BarChart age;

    @FXML
    Text primo;

    @FXML
    Text secondo;

    @FXML
    Text terzo;

    @FXML
    Text quarto;

    @FXML
    Text quinto;

    @FXML
    BarChart pie1;

    private static int giaCaricato = 0;

    @FXML
    void initialize() {
        categories.setItems(categorie);

        category1.setText("");
        category2.setText("");
        category3.setText("");

        if(giaCaricato == 0){
            refresh();
            giaCaricato = 1;
        }

    }

    @FXML
    private void refresh() {
        displayActivityChart();
        displayUserAge();
        displayVersatileInfluencer();
        displayLoginChart();
    }

    private void displayLoginChart() {
        List<ActivityBean> lista = controller.getDailyAvgLoginForCountry();
        BarChart.Series series = new BarChart.Series();
        series.setName("Average login");

        for (int i = 0; i < lista.size(); i++) {
            series.getData().add(new BarChart.Data(lista.get(i).getCountry(), lista.get(i).getAvgLogin()));

            if (i == 4){
                break;
            }
        }

        pie1.setBarGap(3);
        pie1.setCategoryGap(20);
        pie1.getData().add(series);

    }

    private Text chooseVersatile(int i){
        return switch (i) {
            case 1 -> primo;
            case 2 -> secondo;
            case 3 -> terzo;
            case 4 -> quarto;
            case 5 -> quinto;
            default -> new Text();
        };
    }

    private void displayVersatileInfluencer() {
        List<VersatileUser> list = controller.showMostVersatileInfluencer(5);

        for(int i = 0; i < 5; i++) {
            Text modify = chooseVersatile(i + 1);

            if(i < list.size()) {
                modify.setText(list.get(i).getUsername() + ", " + list.get(i).getHowManyCategories());
            } else {
                modify.setText("");
            }
        }
    }

    private void displayUserAge() {
        List<AgeBean> lista = controller.getUsersForAge();
        BarChart.Series series = new BarChart.Series();
        series.setName("User age");

        int quantiPrima = 0;
        int quantiSeconda = 0;
        int quantiTerza = 0;
        int quantiQuarta = 0;
        int quantiQuinta = 0;

        String prima = "0/21";
        String seconda = "22/35";
        String terza = "36/50";
        String quarta = "51/70";
        String quinta = "71+";

        for(int i = 0; i < lista.size(); i++) {

            if(lista.get(i).getAge() <= 21) {
                quantiPrima += lista.get(i).getNumUser();
            } else if(lista.get(i).getAge() >= 22 && lista.get(i).getAge() <= 35) {
                quantiSeconda += lista.get(i).getNumUser();
            } else if(lista.get(i).getAge() >= 36 && lista.get(i).getAge() <= 50) {
                quantiTerza += lista.get(i).getNumUser();
            }else if(lista.get(i).getAge() >= 51 && lista.get(i).getAge() <= 70) {
                quantiQuarta += lista.get(i).getNumUser();
            }else if(lista.get(i).getAge() >= 71){
                quantiQuinta += lista.get(i).getNumUser();
            }

        }

        series.getData().add(new BarChart.Data(prima, quantiPrima));
        series.getData().add(new BarChart.Data(seconda, quantiSeconda));
        series.getData().add(new BarChart.Data(terza, quantiTerza));
        series.getData().add(new BarChart.Data(quarta, quantiQuarta));
        series.getData().add(new BarChart.Data(quinta, quantiQuinta));

        age.setBarGap(3);
        age.setCategoryGap(20);
        age.getData().add(series);
    }

    private void displayActivityChart() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Users login");
        List<ActivityBean> statistiche = controller.getActivitiesStatisticsTotal();

        for(int i = 0; i < statistiche.size(); i++) {
            series.getData().add(new XYChart.Data(statistiche.get(i).getDate(),statistiche.get(i).getNumUser()));
        }
        activity.getData().add(series);
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
        CategoryBean categoryInfo = controller.getCategoryInfo(categorie.get(categories.getSelectionModel().getSelectedIndex()));

        category1.setText(String.valueOf(categoryInfo.getTotGames()));
        category2.setText(String.valueOf(Math.round(categoryInfo.getAvgRatingTot())));
        category3.setText(String.valueOf(categoryInfo.getNumRatesTot()));

    }

}
