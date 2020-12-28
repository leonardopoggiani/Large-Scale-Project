package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;
import org.openjfx.Controller.Neo4jDBController;

import java.io.IOException;
import java.util.logging.Logger;

public class signup {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    Neo4jDBController neo = new Neo4jDBController();

    @FXML
    void registerUser() throws IOException {
        logger.info("Registrazione iniziata");
        // User u = new User();
        // neo.registerUserController(u);
        logger.info("Registrazione completata");
        goToLogin();
    }

    void goToLogin() throws IOException {
        App.setRoot("login");
    }

}
