package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;
import org.openjfx.Controller.Neo4jDBController;

import java.io.IOException;
import java.util.logging.Logger;

public class signup {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    @FXML
    void registerUser() throws IOException {
        logger.info("Registrazione iniziata");
        Neo4jDBController neo = new Neo4jDBController();
        neo.registerNewUser();
        logger.info("Registrazione completata");
    }

}
