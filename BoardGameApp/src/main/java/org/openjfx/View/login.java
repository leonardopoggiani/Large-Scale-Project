package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import org.openjfx.App;
import org.openjfx.Controller.LoginSignUpDBController;

import java.io.IOException;
import java.util.logging.Logger;

public class login {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    private static String loggedUser;
    LoginSignUpDBController neo = new LoginSignUpDBController();
    private static int logged = -1;

    @FXML
    boolean validateLogin() throws IOException {
        Scene scene = App.getScene(); // recupero la scena della login
        TextField us = (TextField) scene.lookup("#username");
        String username = us.getText();
        TextField ps = (TextField) scene.lookup("#password");
        String password = ps.getText();

        if(username != null && password != null ){
            // controllo username, se controllo va male check = false
            loggedUser = username;
            boolean ret = neo.neo4jLoginUserController(username,password);

            if(ret) logged = 1;
        } else {
            return false;
        }

        return true;
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

    public static int getLogged() { return logged ; }
    public static void logout() { logged = -1; }
}
