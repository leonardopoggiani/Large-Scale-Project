package org.openjfx.View;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.openjfx.App;

import java.io.IOException;

public class loginResult {


    @FXML
    void goToHomepage() throws IOException {
        App.setRoot("homePage");
    }

    @FXML
    void setUsername() throws IOException {
        Scene scene = App.getScene();
        Label user = (Label) scene.lookup("#loggedUser");
        if(login.getLogged() == 1) {
            user.setText(login.getLoggedUser() + ", welcome!");
        } else {
            user.setText("Login fallito!");
            Button btn = (Button) scene.lookup("#homebutton");
            btn.setText("Riprova il login");
            btn.setOnMouseClicked(event ->  {
                try {
                    goToLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    EventHandler<? super MouseEvent> goToLogin() throws IOException {
        App.setRoot("login");
        return null;
    }
}
