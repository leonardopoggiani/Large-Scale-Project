package org.openjfx.View;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.openjfx.App;
import org.openjfx.Controller.GroupsPostsDBController;
import org.openjfx.Controller.UpdateDatabaseDBController;
import org.openjfx.Entities.InfoGroup;
import org.openjfx.Entities.TableGroup;

import java.io.IOException;
import java.sql.Timestamp;

public class groups {

    int giaCaricato = -1;
    private TableView<TableGroup> tableAdmin;
    private TableView<TableGroup> tableMembro;
    private final ObservableList<TableGroup> gruppiAdmin = FXCollections.observableArrayList();
    private ObservableList<TableGroup> gruppiMembro = FXCollections.observableArrayList();
    private TableColumn groupName;
    private TableColumn timestampCreation;
    private TableColumn admin;
    private TableColumn gameReferred;

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("homePage");
    }

    @FXML
    void goToGames() throws IOException {
        App.setRoot("games");
    }

    @FXML
    void goToGroups() throws IOException {
        App.setRoot("groups");
    }

    @FXML
    void goToFriends() throws IOException {
        App.setRoot("users");
    }

    @FXML
    void goToSettings() throws IOException {
        App.setRoot("profileSettings");
    }

    @FXML
    void setGroups() throws IOException {
        if(giaCaricato == -1) {
             tableAdmin = (TableView<TableGroup>) App.getScene().lookup("#table");
             tableAdmin.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
             tableMembro = (TableView<TableGroup>) App.getScene().lookup("#table2");
             tableMembro.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

             groupName = new TableColumn("Name");
             timestampCreation = new TableColumn("Timestamp");
             admin = new TableColumn("Admin");
             gameReferred = new TableColumn("Game");

            groupName.setCellValueFactory(new PropertyValueFactory<>("groupName"));
            timestampCreation.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
            admin.setCellValueFactory(new PropertyValueFactory<>("admin"));
            gameReferred.setCellValueFactory(new PropertyValueFactory<>("gameReferred"));

            tableAdmin.getColumns().addAll(groupName, timestampCreation, admin, gameReferred);
            tableMembro.getColumns().addAll(groupName, timestampCreation, admin, gameReferred);

            giaCaricato = 1;
        }
    }

    @FXML
    void createGroup() throws IOException {
        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();

        TextField tf = (TextField) App.getScene().lookup("#groupname");
        TextField tf2 = (TextField) App.getScene().lookup("#referredgame");
        TextField tf3 = (TextField) App.getScene().lookup("#description");

        String groupname = tf.getText();
        String game = tf2.getText();
        String description = tf3.getText();

        if(groupname.equals("")){
            tf.setStyle("-fx-background-color: #ff0000; -fx-font-size: 12;");
            return;
        }

        if(game.equals("")) {
            tf2.setStyle("-fx-background-color: #ff0000; -fx-font-size: 12;");
            return;
        }

        if(description.equals("")){
            description = "No description provided";
        }

        tf.setText("");
        tf2.setText("");
        tf3.setText("");

        InfoGroup group = new InfoGroup(groupname, new Timestamp(System.currentTimeMillis()), login.getLoggedUser(), description, game);
        System.out.println(group);
        controller.Neo4jAddGroup(group);

        TableGroup tableGroup = new TableGroup(group.getName(),group.getTimestamp(),group.getAdmin(),group.getGame());
        System.out.println(tableGroup);

        gruppiAdmin.add(tableGroup);
        tableAdmin.setItems(gruppiAdmin);
    }
    
}
