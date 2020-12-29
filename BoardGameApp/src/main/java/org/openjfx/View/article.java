package org.openjfx.View;

import javafx.fxml.FXML;
import org.openjfx.App;

import java.io.IOException;

public class article {
    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("homePage");
    }
}
