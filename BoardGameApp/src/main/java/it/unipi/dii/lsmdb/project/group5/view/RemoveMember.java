package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.controller.GroupsPagesDBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Remove member.
 */
public class RemoveMember {

    /**
     * The Logger.
     */
Logger logger =  Logger.getLogger(this.getClass().getName());
    /**
     * The Controller 2.
     */
GroupsPagesDBController controller2 = new GroupsPagesDBController();

    /**
     * The Users list.
     */
ObservableList<String> usersList = FXCollections.observableArrayList();

@FXML
    ComboBox users;

    /**
     * Initialize.
     */
@FXML
    void initialize() {
        List<String> utenti = controller2.showGroupsMembers(HomepageGroups.getGroup(),HomepageGroups.getAdminGroup());
        usersList.addAll(utenti);
        users.setItems(usersList);
    }

    /**
     * Remove member.
     *
     * @param actionEvent the action event
     */
public void removeMember(ActionEvent actionEvent) {
        String toRemove = usersList.remove(users.getSelectionModel().getSelectedIndex());
        logger.log(Level.INFO,"removed " + toRemove);
        controller2.addDeleteGroupMember(toRemove,HomepageGroups.getGroup(),HomepageGroups.getAdminGroup(), "remove");
        users.setItems(usersList);
    }

    /**
     * Return to homepage groups.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
public void returnToHomepageGroups(ActionEvent actionEvent) throws IOException {
        App.setRoot("HomepageGroups");
    }
}
