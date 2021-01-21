package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.controller.GroupsPagesDBController;
import it.unipi.dii.lsmdb.project.group5.controller.UsersPagesDBController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/** The view of the Add member page. */
public class AddMember {

  /** The Logger. */
  Logger logger = Logger.getLogger(this.getClass().getName());

  /** The Controller. */
  UsersPagesDBController controller = new UsersPagesDBController();

  /** The Controller 2. */
  GroupsPagesDBController controller2 = new GroupsPagesDBController();

  /** The Users list. */
  ObservableList<String> usersList = FXCollections.observableArrayList();

  /** The Users. */
  @FXML ComboBox users;

  /** Initialize. */
  @FXML
  void initialize() {
        List<String> utenti = controller.listUsers(HomepageGroups.getGroup(),"friends");
        usersList.addAll(utenti);
        users.setItems(usersList);
    }

  /** Add member (if possible). */
  @FXML
  public void addMember() {
        String toInsert = usersList.get(users.getSelectionModel().getSelectedIndex());

        if(toInsert != null) {
            controller2.addDeleteGroupMember(toInsert,HomepageGroups.getGroup(),HomepageGroups.getAdminGroup(), "add");
        }
    }

  /**
   * Return to homepage groups.
   *
   * @throws IOException the io exception
   */
  @FXML
  public void returnToHomepageGroups() throws IOException {
        App.setRoot("HomepageGroups");
    }

}
