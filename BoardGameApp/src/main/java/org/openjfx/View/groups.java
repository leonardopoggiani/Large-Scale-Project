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
import org.openjfx.Controller.UpdateDatabaseDBController;
import org.openjfx.Entities.InfoGroup;
import org.openjfx.Entities.TableGroup;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class groups {

    int giaCaricato = -1;

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
            TableView<InfoGroup> table = (TableView<InfoGroup>) App.getScene().lookup("#table");
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            TableColumn groupName = new TableColumn("groupName");
            TableColumn timestampCreation = new TableColumn("timestamp");
            TableColumn admin = new TableColumn("admin");
            TableColumn gameReferred = new TableColumn("game");

            table.getColumns().addAll(groupName, timestampCreation, admin, gameReferred);
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

        tf.setText("");
        tf2.setText("");
        tf3.setText("");

        InfoGroup group = new InfoGroup(groupname, new Timestamp(System.currentTimeMillis()), login.getLoggedUser(), description, game);

        controller.Neo4jAddGroup(group);

        ObservableList<InfoGroup> values = FXCollections.observableArrayList();
        values.add(group);

        TableView<InfoGroup> table = (TableView<InfoGroup>) App.getScene().lookup("#table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        System.out.println(group);
        TableColumn groupName = new TableColumn("groupName");
        TableColumn timestampCreation = new TableColumn("timestamp");
        TableColumn admin = new TableColumn("admin");
        TableColumn gameReferred = new TableColumn("game");

        groupName.setCellValueFactory(param -> new ReadOnlyStringWrapper(group.getName()));
        timestampCreation.setCellValueFactory(param -> new ReadOnlyStringWrapper(String.valueOf(group.getTimestamp())));
        admin.setCellValueFactory(param -> new ReadOnlyStringWrapper(group.getAdmin()));
        gameReferred.setCellValueFactory(param -> new ReadOnlyStringWrapper(group.getGame()));

        table.getColumns().addAll(groupName, timestampCreation, admin, gameReferred);
        table.setItems(values);
    }
    
}
