package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;

import java.io.IOException;
import java.util.logging.Logger;

public class login {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    @FXML
    void validateLogin() throws IOException {
        App.setRoot("loginResult");
    }

    @FXML
    void switchSignup() throws IOException {
        App.setRoot("signup");
    }

    @FXML
    void loginResult() throws IOException {
        validateLogin();
        logger.info("Login correttamente effettuato");
    }
}
