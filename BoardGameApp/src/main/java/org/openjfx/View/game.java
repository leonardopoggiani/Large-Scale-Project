package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.openjfx.App;
import org.openjfx.Controller.GamesReviewsRatesDBController;
import org.openjfx.Entities.InfoGame;
import org.openjfx.Entities.InfoReview;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class game {
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
    void setGameFields() {
        if(giàCaricato == -1) {
            String game = games.getGame();
            logger.info("Carico " + game);
            GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();

            InfoGame currentGame = controller.showGame(game);
            System.out.println(currentGame);

            Text title = (Text) App.getScene().lookup("#title");
            title.setText(game);

            Text vote = (Text) App.getScene().lookup("#votes");
            vote.setText(String.valueOf(currentGame.getAvgRating()));

            TextArea rule = (TextArea) App.getScene().lookup("#rules");
            rule.setText(currentGame.getRules());

            List<InfoReview> reviews = controller.neo4jListGamesReviews(game);

            for(int i = 0; i < reviews.size() || i < 3; i++){
                TextField review = (TextField) App.getScene().lookup("#review" + i);
                review.setText(reviews.get(i).getText());

                if(reviews.get(i).getAuthor().equals(login.getLoggedUser())) {
                    Button delete = (Button) App.getScene().lookup("#delete" + i);
                    delete.setDisable(false);
                    delete.setVisible(true);
                }
            }

            giàCaricato = 1;
        }
    }

    @FXML
    void postReview() {
        TextField review = (TextField) App.getScene().lookup("review");

    }

    @FXML
    void goToGame() {

    }

    @FXML
    void deleteReview() {

    }

}
