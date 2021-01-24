package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.controller.UsersPagesDBController;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import it.unipi.dii.lsmdb.project.group5.App;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

/** The type Profile settings page view. */
public class ProfileSettingsPageView {

  /** The Controller. */
  UsersPagesDBController controller = new UsersPagesDBController();

  @FXML Text text;

  @FXML ImageView tic;

    /**
     * The Categorie.
     */
    ObservableList<String> categorie = FXCollections.observableArrayList(
            "Math:1104","Card Game:1002","Humor:1079","Party Game:1030",
            "Number:1098","Puzzle:1028","Dice:1017","Sports:1038",
            "Book:1117","Fantasy:1010","Miniatures:1047","Wargame:1019",
            "Napoleonic:1051","Children's Game:1041","Memory:1045",
            "Educational:1094","Medical:2145","Animals:1089","Racing:1031",
            "Adventure:1022","Travel:1097","Abstact Strategy:1009",
            "Economic:1021","Trains:1034","Transportation:1011","Real-time:1037",
            "Action/Dexterity:1032","Ancient:1050","Collectible Components:1044",
            "Fighting:1046","Movies/TV/Radio Theme:1064","Bluffing:1023",
            "Zombies:2481","Medieval:1035","Negotiation:1026","World War II: 1049",
            "Spies/Secret Agents:1081","Deduction:1039","Murder/Mystery:1040",
            "Aviation/Flight:2650","Modern Warfare:1069","Territory Building:1086",
            "Print & Play:1120","Novel-Based:1093","Puzzle:1028","Science Fiction:1016",
            "Exploration:1020","Word-game:1025","Video Game Theme:1101", "None");

  /**
   * Modify user settings.
   *
   * @throws IOException the io exception
   */
  @FXML
  void modifyUserSettings() {
        Scene scene = App.getScene();
        TextField n = (TextField) scene.lookup("#name");
        TextField sn = (TextField) scene.lookup("#surname");
        TextField ps = (TextField) scene.lookup("#password");
        TextField a = (TextField) scene.lookup("#age");
        ComboBox cat1 = (ComboBox) scene.lookup("#favouritecategory1");
        ComboBox cat2 = (ComboBox) scene.lookup("#favouritecategory2");
        String categoria1 = "";
        String categoria2 = "";

        String name = n.getText();
        String surname = sn.getText();
        String password = ps.getText();
        String age = a.getText();

        if(cat1.getSelectionModel().getSelectedIndex() == -1) {
            categoria1 = "";
        } else {
            categoria1 = categorie.get(cat1.getSelectionModel().getSelectedIndex());
        }

        if(cat2.getSelectionModel().getSelectedIndex() == -1) {
            categoria2 = "";
        } else {
            categoria2 = categorie.get(cat2.getSelectionModel().getSelectedIndex());
        }

        if(controller.modifyProfile(LoginPageView.getLoggedUser(),name,surname,password,age,categoria1,categoria2) ) {
            Logger.log("profile updated");
            n.setText("");
            ps.setText("");
            sn.setText("");
            a.setText("");
            cat1.getSelectionModel().clearSelection();
            cat2.getSelectionModel().clearSelection();
            text.setVisible(true);
            tic.setVisible(true);
        } else {
            Logger.warning("profile not updated");
            text.setVisible(false);
            tic.setVisible(false);
        }
    }

  /**
   * Carica categorie.
   *
   * @throws IOException the io exception
   */
  @FXML
  void caricaCategorie() throws IOException {
        Scene scene = App.getScene(); // recupero la scena delle settings
        ComboBox cat1 = (ComboBox) scene.lookup("#favouritecategory1");
        ComboBox cat2 = (ComboBox) scene.lookup("#favouritecategory2");

        cat1.setItems(categorie);
        cat2.setItems(categorie);
    }

  /**
   * Return to homepage.
   *
   * @throws IOException the io exception
   */
  @FXML
  void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

}
