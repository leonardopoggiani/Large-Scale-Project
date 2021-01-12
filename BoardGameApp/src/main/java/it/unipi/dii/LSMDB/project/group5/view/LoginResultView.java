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
    void goToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

    @FXML
    void setUsername() throws IOException {
        Scene scene = App.getScene();
        Label user = (Label) scene.lookup("#loggedUser");
        if(LoginPageView.getLogged() == 1) {
            user.setText(LoginPageView.getLoggedUser() + ", welcome!");
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
        App.setRoot("LoginPageView");
        return null;
    }
}
