package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.controller.UpdateDatabaseDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UsersDBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AddMember {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    UsersDBController controller = new UsersDBController();
    UpdateDatabaseDBController controller2 = new UpdateDatabaseDBController();

    ObservableList<String> usersList = FXCollections.observableArrayList();

    @FXML
    ComboBox users;

    @FXML
    void initialize() {
        List<String> utenti = controller.neo4jListUsers(HomepageGroups.getGroup(),"all");
        logger.info("size " + utenti.size());
        usersList.addAll(utenti);
        users.setItems(usersList);
    }

    @FXML
    public void addMember() {
        String toInsert = usersList.get(users.getSelectionModel().getSelectedIndex());
        logger.info("inserito " + toInsert);
        controller2.Neo4jAddGroupMember(toInsert,HomepageGroups.getGroup(),HomepageGroups.getAdminGroup());
    }

    @FXML
    public void returnToHomepageGroups() throws IOException {
        App.setRoot("HomepageGroups");
    }

}
