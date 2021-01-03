package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import org.openjfx.App;
import org.openjfx.Controller.ArticlesCommentsLikesDBController;
import org.openjfx.Controller.GamesReviewsRatesDBController;
import org.openjfx.Entities.Article;
import org.openjfx.Entities.InfoGame;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class games {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    int giàCaricato = -1;

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("homePage");
    }

    @FXML
    void goToGames() throws IOException {
        App.setRoot("games");
    }

    @FXML
    void goToGroups() throws IOException {
        App.setRoot("groups");
    }

    @FXML
    void goToFriends() throws IOException {
        App.setRoot("users");
    }

    @FXML
    void goToSettings() throws IOException {
        App.setRoot("profileSettings");
    }

    @FXML
    void goToGame() throws IOException {
        App.setRoot("game");
    }

    @FXML
    void setSuggestedGames() throws IOException {

        if(giàCaricato == -1) {
            GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();
            List<InfoGame> list = controller.neo4jListSuggestedGames(login.getLoggedUser());

            if (list != null) {
                System.out.println("Lunghezza lista " + list.size());
                for (int i = 0; i < list.size() && i < 6; i++) {
                    InfoGame g = list.get(i);
                    TitledPane gioco = (TitledPane) App.getScene().lookup("#fullgame" + (i + 1));
                    Text name = (Text) App.getScene().lookup("#name" + (i + 1));

                    gioco.setText(g.getName());
                    // name.setText(g.getAuthor());
                }
            }

            giàCaricato = 1;
        }
    }
}
