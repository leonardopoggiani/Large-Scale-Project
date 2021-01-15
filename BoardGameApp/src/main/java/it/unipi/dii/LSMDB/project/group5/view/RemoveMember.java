package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.controller.GroupsPagesDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UsersPagesDBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class RemoveMember {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    UsersPagesDBController controller = new UsersPagesDBController();
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

    public void removeMember(ActionEvent actionEvent) {
        String toRemove = usersList.remove(users.getSelectionModel().getSelectedIndex());
        logger.info("tolgo " + toRemove);
        controller2.addDeleteGroupMember(toRemove,HomepageGroups.getGroup(),HomepageGroups.getAdminGroup(), "remove");
        users.setItems(usersList);
    }

    public void returnToHomepageGroups(ActionEvent actionEvent) throws IOException {
        App.setRoot("HomepageGroups");
    }

}
