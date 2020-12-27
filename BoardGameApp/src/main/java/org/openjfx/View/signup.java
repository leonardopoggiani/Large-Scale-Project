package org.openjfx.View;

import javafx.fxml.FXML;

import java.io.IOException;
import java.util.logging.Logger;

public class signup {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    @FXML
    void registerUser() throws IOException {
        logger.info("Registrazione iniziata");

        logger.info("Registrazione completata");
    }

}
