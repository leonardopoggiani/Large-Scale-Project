package it.unipi.dii.lsmdb.project.group5.view;

import javafx.fxml.FXML;
import it.unipi.dii.lsmdb.project.group5.App;

import java.io.IOException;

public class RegisterResultPageView {

    @FXML
    void registerResult() throws IOException {
        App.setRoot("RegisterResultPageView");
    }

    @FXML
    void goToLogin() throws IOException {
        App.setRoot("LoginPageView");
    }

}
