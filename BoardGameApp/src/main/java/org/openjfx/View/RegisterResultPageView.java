package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;

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
