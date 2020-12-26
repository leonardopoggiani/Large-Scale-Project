package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;

import java.io.IOException;

public class signupController {

    @FXML
    void goToLogin() throws IOException {
        App.setRoot("login");
    }

}
