package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.openjfx.App;

import java.io.IOException;

public class loginResult {


    @FXML
    void goToHomepage() throws IOException {
        App.setRoot("homePage");
    }

    @FXML
    void setUsername() {
        Scene scene = App.getScene();
        Label user = (Label) scene.lookup("#loggedUser");
        user.setText(login.getLoggedUser() + ", welcome!");
    }
}
