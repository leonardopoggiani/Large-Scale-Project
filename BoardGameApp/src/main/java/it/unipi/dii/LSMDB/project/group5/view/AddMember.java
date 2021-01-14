package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.controller.GroupsPagesDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UsersPageDBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AddMember {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    UsersPageDBController controller = new UsersPageDBController();
    GroupsPagesDBController controller2 = new GroupsPagesDBController();

    ObservableList<String> usersList = FXCollections.observableArrayList();

    @FXML
    ComboBox users;

    @FXML
    void initialize() {
        List<String> utenti = controller.listUsers(HomepageGroups.getGroup(),"all");
        logger.info("size " + utenti.size());
        usersList.addAll(utenti);
        users.setItems(usersList);
    }

    @FXML
    public void addMember() {
        String toInsert = usersList.get(users.getSelectionModel().getSelectedIndex());
        logger.info("inserito " + toInsert);
        controller2.addDeleteGroupMember(toInsert,HomepageGroups.getGroup(),HomepageGroups.getAdminGroup(), "add");
    }

    @FXML
    public void returnToHomepageGroups() throws IOException {
        App.setRoot("HomepageGroups");
    }

}
