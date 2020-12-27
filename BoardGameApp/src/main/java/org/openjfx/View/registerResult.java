package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;

import java.io.IOException;

public class registerResult {

    @FXML
    void registerResult() throws IOException {
        App.setRoot("registerResult");
    }

    @FXML
    void goToLogin() throws IOException {
        App.setRoot("login");
    }

}
