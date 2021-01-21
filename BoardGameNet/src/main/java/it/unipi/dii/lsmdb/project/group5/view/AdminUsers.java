package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.bean.CountryBean;
import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import it.unipi.dii.lsmdb.project.group5.controller.AnalyticsDBController;
import it.unipi.dii.lsmdb.project.group5.controller.UsersPagesDBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

/** The type Admin users. */
public class AdminUsers {

    /** The Controller. */
    AnalyticsDBController controller = new AnalyticsDBController();

    @FXML Text user1;

    @FXML Text user2;

    @FXML Text user3;

    @FXML Text user4;

    @FXML Text user5;

    @FXML Text text;

    @FXML TextField deleting;

    @FXML ImageView tic;

    @FXML Button remove;

    @FXML PieChart userpie;

    @FXML Text promotetext;

    @FXML TextField promote;

    @FXML ImageView tic1;

    @FXML Button moderator;

    @FXML Button admin;

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
    * Logout.
    *
    * @throws IOException the io exception
    */
    @FXML
    void logout() throws IOException {
        App.setRoot("LoginPageView");
        LoginPageView.logout();
    }

    /** Initialize. */
    @FXML
    void initialize() {
        displayLeastRecentlyLoggedUsers();
    }

    /**
     * Display the least recently logged users.
     */
    private void displayLeastRecentlyLoggedUsers() {
        List<UserBean> utenti = controller.showLessRecentLoggedUsers();
        user1.setText((utenti.get(0) == null) ? "" : (utenti.get(0).getUsername() + " / " + utenti.get(0).getLastLogin().toString()));
        user2.setText((utenti.get(1) == null) ? "" : (utenti.get(1).getUsername() +  " / " + utenti.get(1).getLastLogin().toString()));
        user3.setText((utenti.get(2) == null) ? "" : (utenti.get(2).getUsername() +  " / " + utenti.get(2).getLastLogin().toString()));
        user4.setText((utenti.get(3) == null) ? "" : (utenti.get(3).getUsername() +  " / " + utenti.get(3).getLastLogin().toString()));
        user5.setText((utenti.get(4) == null) ? "" : (utenti.get(4).getUsername() +  " / " + utenti.get(4).getLastLogin().toString()));

        displayUserChart();
    }

    /**
     * Search for a user.
     */
    @FXML
    private void searchUser() {
        UsersPagesDBController userController = new UsersPagesDBController();
        UserBean utente = userController.showUser(deleting.getText());

        if(utente != null) {
            remove.setDisable(false);
            tic.setVisible(true);
        } else {
            remove.setDisable(true);
            tic.setVisible(false);
        }
    }

    /**
     * Remove a user.
     */
    @FXML
    private void removeUser() {
        UsersPagesDBController userController = new UsersPagesDBController();
        boolean ret = userController.deleteUser(deleting.getText());
        if(ret) {
            text.setVisible(true);
            remove.setDisable(true);
        } else {
            text.setVisible(false);
        }
    }

  /** Display user chart. */
  @FXML
  void displayUserChart() {
        List<CountryBean> lista = controller.getUsersFromCountry();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();

        for(int i = 0; i < lista.size(); i++) {
            pieChartData.add(new PieChart.Data(lista.get(i).getCountry(), lista.get(i).getNumUser()));

            if(i == 4)
                break;
        }

        userpie.setData(pieChartData);
        userpie.setLabelsVisible(true);
        userpie.setLabelLineLength(10);
        userpie.setLegendSide(Side.LEFT);
    }

    /** Search a user fro promotion. */
    @FXML
    private void searchUserForPromotion() {
        UsersPagesDBController userController = new UsersPagesDBController();
        UserBean utente = userController.showUser(promote.getText());

        if(utente != null) {
            moderator.setDisable(false);
            admin.setDisable(false);
            tic1.setVisible(true);
        } else {
            moderator.setDisable(true);
            admin.setDisable(true);
            tic1.setVisible(false);
        }
    }

    /** Search a user for promotion to moderator. */
    @FXML
    private void promoteModerator() {
        UsersPagesDBController userController = new UsersPagesDBController();
        UserBean utente = userController.showUser(promote.getText());

        if(utente != null && utente.getUsername() != null && userController.promoteDemoteUser(utente.getUsername(),"moderator")) {
            promotetext.setVisible(true);
            moderator.setDisable(true);
            admin.setDisable(true);
            tic1.setVisible(false);
        }
    }

    /** Search a user for promotion to admin. */
    @FXML
    private void promoteAdmin() {
        UsersPagesDBController userController = new UsersPagesDBController();
        UserBean utente = userController.showUser(promote.getText());

        if(utente != null && utente.getUsername() != null && userController.promoteDemoteUser(utente.getUsername(),"admin")) {
            promotetext.setVisible(true);
            moderator.setDisable(true);
            admin.setDisable(true);
            tic1.setVisible(false);
        }
    }

}
