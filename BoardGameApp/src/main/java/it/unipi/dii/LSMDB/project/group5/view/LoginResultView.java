package it.unipi.dii.LSMDB.project.group5.view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import it.unipi.dii.LSMDB.project.group5.App;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginResultView implements Initializable {


    @FXML
    void goToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

    @FXML
    void setUsername() throws IOException {
        Scene scene = App.getScene();
        Label user = new Label();
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
        AnchorPane borderPane = new AnchorPane();



    }

    EventHandler<? super MouseEvent> goToLogin() throws IOException {
        App.setRoot("LoginPageView");
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setUsername();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
