package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import org.openjfx.App;

import java.io.IOException;
import java.util.logging.Logger;

public class login {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    private static String loggedUser;

    @FXML
    boolean validateLogin() throws IOException {
        Scene scene = App.getScene(); // recupero la scena della login
        TextField us = (TextField) scene.lookup("#username");
        String username = us.getText();
        boolean check = true;
        if(username != null){
            // controllo username, se controllo va male check = false
            loggedUser = username;
            logger.info("Loggato " + loggedUser);
        }
        TextField ps = (TextField) scene.lookup("#password");
        String password = ps.getText();
        if(password != null){
            // controllo password, se controllo va male check = false
        }

        return check;
    }

    @FXML
    void switchSignup() throws IOException {
        App.setRoot("signup");
    }

    @FXML
    void loginResult() throws IOException {
        if(validateLogin()) {
            App.setRoot("loginResult");
            logger.info("Login correttamente effettuato");
        } else {
            logger.info("Login non corretto");
        }
    }

    public static String getLoggedUser(){
        return loggedUser;
    }
}
