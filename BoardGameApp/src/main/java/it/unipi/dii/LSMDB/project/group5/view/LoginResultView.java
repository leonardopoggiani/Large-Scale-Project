package it.unipi.dii.LSMDB.project.group5.view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import it.unipi.dii.LSMDB.project.group5.App;

import java.io.IOException;

public class LoginResultView {

    @FXML
    Button homebutton;

    @FXML
    Label loggedUser;

    @FXML
    void goToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

    @FXML
    void initialize() {
        setUsername();
    }

    @FXML
    void setUsername() {
        if(LoginPageView.getLogged() == 1) {
            loggedUser.setText(LoginPageView.getLoggedUser() + ", welcome!");
        } else {
            loggedUser.setText("Login fallito!");
            homebutton.setText("Riprova il login");
            homebutton.setOnMouseClicked(event ->  {
                try {
                    goToLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    EventHandler<? super MouseEvent> goToLogin() throws IOException {
        App.setRoot("LoginPageView");
        return null;
    }
}
