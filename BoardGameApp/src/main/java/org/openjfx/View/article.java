package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.openjfx.App;

import java.io.IOException;
import java.util.logging.Logger;

public class article {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("homePage");
    }

    @FXML
    void like() throws IOException {
        Text like = (Text) App.getScene().lookup("#numberlike");
        int numberOfLike = Integer.parseInt(like.getText());
        like.setText(String.valueOf(numberOfLike + 1));
        // salvare numero like
    }

    @FXML
    void unlike() throws IOException {
        Text like = (Text) App.getScene().lookup("#numberunlike");
        int numberOfUnlike = Integer.parseInt(like.getText());
        like.setText(String.valueOf(numberOfUnlike + 1));
        // salvare numero unlike
    }

    @FXML
    void postComment() throws IOException {
        logger.info("Commento");
    }
}
