package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import it.unipi.dii.LSMDB.project.group5.bean.UserBean;
import it.unipi.dii.LSMDB.project.group5.controller.GamesPagesDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UsersPagesDBController;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class AdminUsers {

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
    TextField deleting;

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
        displayLeastRecentlyLoggedUsers();
    }

    private void displayLeastRecentlyLoggedUsers() {
        List<UserBean> utenti = AnalyticsDBManager.showLessRecentLoggedUsers();
        user1.setText((utenti.get(0) == null) ? "" : utenti.get(0).getName() + " / " + utenti.get(0).getLastLogin().toString());
        user2.setText((utenti.get(1) == null) ? "" : utenti.get(1).getName() +  " / " + utenti.get(1).getLastLogin().toString());
        user3.setText((utenti.get(2) == null) ? "" : utenti.get(2).getName() +  " / " + utenti.get(2).getLastLogin().toString());
        user4.setText((utenti.get(3) == null) ? "" : utenti.get(3).getName() +  " / " + utenti.get(3).getLastLogin().toString());
        user5.setText((utenti.get(4) == null) ? "" : utenti.get(4).getName() +  " / " + utenti.get(4).getLastLogin().toString());
    }

    @FXML
    private void searchUser() {
        UsersPagesDBController userController = new UsersPagesDBController();
        // UserBean utente = userController.deleteUser(deleting.getText());

        /*if(gioco != null) {
            remove.setDisable(false);
            tic.setVisible(true);
        } else {
            remove.setDisable(true);
            tic.setVisible(false);
        }*/
    }

    @FXML
    private void removeUser() {

    }

}
