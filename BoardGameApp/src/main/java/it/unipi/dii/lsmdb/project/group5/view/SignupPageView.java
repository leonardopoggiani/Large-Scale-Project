package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.controller.LoginSignUpDBController;

import java.io.IOException;
import java.util.logging.Logger;

/** The type Signup page view. */
public class SignupPageView {

  /** The Logger. */
  Logger logger = Logger.getLogger(this.getClass().getName());

  /** The Neo. */
  LoginSignUpDBController neo = new LoginSignUpDBController();

  /** The Categorie. */
  ObservableList<String> categorie =
      FXCollections.observableArrayList(
          "Math:1104",
          "Card Game:1002",
          "Humor:1079",
          "Party Game:1030",
          "Number:1098",
          "Puzzle:1028",
          "Dice:1017",
          "Sports:1038",
          "Book:1117",
          "Fantasy:1010",
          "Miniatures:1047",
          "Wargame:1019",
          "Napoleonic:1051",
          "Children's Game:1041",
          "Memory:1045",
          "Educational:1094",
          "Medical:2145",
          "Animals:1089",
          "Racing:1031",
          "Adventure:1022",
          "Travel:1097",
          "Abstact Strategy:1009",
          "Economic:1021",
          "Trains:1034",
          "Transportation:1011",
          "Real-time:1037",
          "Action/Dexterity:1032",
          "Ancient:1050",
          "Collectible Components:1044",
          "Fighting:1046",
          "Movies/TV/Radio Theme:1064",
          "Bluffing:1023",
          "Zombies:2481",
          "Medieval:1035",
          "Negotiation:1026",
          "World War II: 1049",
          "Spies/Secret Agents:1081",
          "Deduction:1039",
          "Murder/Mystery:1040",
          "Aviation/Flight:2650",
          "Modern Warfare:1069",
          "Territory Building:1086",
          "Print & Play:1120",
          "Novel-Based:1093",
          "Puzzle:1028",
          "Science Fiction:1016",
          "Exploration:1020",
          "Word-game:1025",
          "Video Game Theme:1101");

  /**
   * Carica categorie.
   *
   * @throws IOException the io exception
   */
  @FXML
  void caricaCategorie() throws IOException {
        logger.info("Carico le categorie");
        Scene scene = App.getScene(); // recupero la scena della signup
        ComboBox cat1 = (ComboBox) scene.lookup("#favouritecategory1");
        ComboBox cat2 = (ComboBox) scene.lookup("#favouritecategory2");

        cat1.setItems(categorie);
        cat2.setItems(categorie);
    }

  /**
   * Register user.
   *
   * @throws IOException the io exception
   */
  @FXML
  void registerUser() throws IOException {
        logger.info("Registrazione iniziata");

        Scene scene = App.getScene();

        TextField us = (TextField) scene.lookup("#username");
        String username = us.getText();
        PasswordField ps = (PasswordField) scene.lookup("#password");
        String password = ps.getText();
        TextField n = (TextField) scene.lookup("#name");
        String name = n.getText();
        TextField s = (TextField) scene.lookup("#surname");
        String surname = s.getText();
        TextField a = (TextField) scene.lookup("#age");
        String ageText = a.getText();
        int age = Integer.parseInt(ageText);
        ComboBox cat1 = (ComboBox) scene.lookup("#favouritecategory1");
        int index1 = cat1.getSelectionModel().getSelectedIndex();
        String categoria1 = categorie.get(index1);
        ComboBox cat2 = (ComboBox) scene.lookup("#favouritecategory2");
        int index2 = cat2.getSelectionModel().getSelectedIndex();
        String categoria2 = categorie.get(index2);

        if(username == null || username.length() < 4) {
            us.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
            logger.info("Username non corretto");
            return;
        }

        if(password == null || password.length() < 6) {
            ps.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
            logger.info("Password troppo corta");
            return;
        }

        if(name == null) {
            ps.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
            logger.info("Nome non inserito");
            return;
        }

        if(surname == null) {
            ps.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
            logger.info("Cognome non inserito");
            return;
        }

        if(age < 18) {
            ps.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
            logger.info("Hai meno di 18 anni, non puoi registrarti!");
            return;
        }

        if(categoria1 == null || categoria2 == null) {
            cat1.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
            cat2.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
            logger.info("Categoria non valida!");
            return;
        }

        logger.info("Registrazione di " + username);
        UserBean u = new UserBean(username,password,categoria1,categoria2,age,"normalUser");
        neo.registerUser(u);
        logger.info("Registrazione completata");
        goToLogin();
    }

  /**
   * Go to login.
   *
   * @throws IOException the io exception
   */
  void goToLogin() throws IOException {
        App.setRoot("LoginPageView");
    }

}
