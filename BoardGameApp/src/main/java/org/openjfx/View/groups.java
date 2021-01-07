package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.openjfx.App;
import org.openjfx.Controller.UpdateDatabaseDBController;
import org.openjfx.Entities.InfoGroup;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class groups {

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

        InfoGroup group = new InfoGroup(tf.getText(), new Timestamp(System.currentTimeMillis()),login.getLoggedUser(),tf3.getText(),tf2.getText());
        controller.Neo4jAddGroup(group);
        List<InfoGroup> list = null;
        list.add(group);

        TableView<List<InfoGroup>> table = new TableView<>();
        table.getItems().add(list);
    }
    
}
