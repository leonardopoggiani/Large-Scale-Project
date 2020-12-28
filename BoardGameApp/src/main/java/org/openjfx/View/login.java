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
        boolean ret = false;

        if(username != null && password != null ){
             ret = neo.neo4jLoginUserController(username,password);

            if(ret){
                logged = 1;
                loggedUser = username;
            }
        } else {
            return false;
        }

        return ret;
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
            TextField u = (TextField) App.getScene().lookup("#username");
            TextField p = (TextField) App.getScene().lookup("#password");
            u.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
            p.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
        }
    }

    public static String getLoggedUser(){
        return loggedUser;
    }

    public static int getLogged() { return logged ; }
    public static void logout() { logged = -1; }
}
