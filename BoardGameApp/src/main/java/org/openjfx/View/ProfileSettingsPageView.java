package org.openjfx.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.openjfx.App;
import org.openjfx.Controller.LoginSignUpDBController;

import java.io.IOException;
import java.util.logging.Logger;

public class ProfileSettingsPageView {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    // le categorie dei due menù dropdown
    ObservableList<String> categorie = FXCollections.observableArrayList("Math:1104","Card InfoGame:1002","Humor:1079","Party InfoGame:1030",
            "Number:1098","Puzzle:1028","Dice:1017","Sports:1038",
            "Book:1117","Fantasy:1010","Miniatures:1047","Wargame:1019",
            "Napoleonic:1051","Children's InfoGame:1041","Memory:1045",
            "Educational:1094","Medical:2145","Animals:1089","Racing:1031",
            "Adventure:1022","Travel:1097","Abstact Strategy:1009",
            "Economic:1021","Trains:1034","Transportation:1011","Real-time:1037",
            "Action/Dexterity:1032","Ancient:1050","Collectible Components:1044",
            "Fighting:1046","Movies/TV/Radio Theme:1064","Bluffing:1023",
            "Zombies:2481","Medieval:1035","Negotiation:1026","World War II: 1049",
            "Spies/Secret Agents:1081","Deduction:1039","Murder/Mystery:1040",
            "Aviation/Flight:2650","Modern Warfare:1069","Territory Building:1086",
            "Print & Play:1120","Novel-Based:1093","Puzzle:1028","Science Fiction:1016",
            "Exploration:1020","Word-game:1025","Video InfoGame Theme:1101");


    @FXML
    void uploadNewImage() throws IOException {

    }

    @FXML
    void modifyUserSettings() throws IOException {
        Scene scene = App.getScene(); // recupero la scena delle settings
        TextField n = (TextField) scene.lookup("#name");
        TextField sn = (TextField) scene.lookup("#surname");
        TextField ps = (TextField) scene.lookup("#password");
        TextField a = (TextField) scene.lookup("#age");
        ComboBox cat1 = (ComboBox) scene.lookup("#favouritecategory1");
        ComboBox cat2 = (ComboBox) scene.lookup("#favouritecategory2");
    }

    @FXML
    void caricaCategorie() throws IOException {
        logger.info("Carico le categorie");
        Scene scene = App.getScene(); // recupero la scena delle settings
        ComboBox cat1 = (ComboBox) scene.lookup("#favouritecategory1");
        ComboBox cat2 = (ComboBox) scene.lookup("#favouritecategory2");

        cat1.setItems(categorie);
        cat2.setItems(categorie);
    }

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }
}
