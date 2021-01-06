package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.openjfx.App;
import org.openjfx.Controller.GamesReviewsRatesDBController;
import org.openjfx.Controller.UpdateDatabaseDBController;
import org.openjfx.Entities.InfoGame;
import org.openjfx.Entities.InfoReview;
import javafx.scene.input.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

public class game {
    Logger logger = Logger.getLogger(this.getClass().getName());
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
        if (giàCaricato == -1) {
            String game = games.getGame();
            logger.info("Carico " + game);
            GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();

            InfoGame currentGame = controller.showGame(game);
            System.out.println(currentGame);

            Text title = (Text) App.getScene().lookup("#title");
            title.setText(game);

            Text vote = (Text) App.getScene().lookup("#votes");
            vote.setText(String.valueOf(currentGame.getAvgRating()));
            ProgressBar votes = (ProgressBar) App.getScene().lookup("#votes1");
            votes.setProgress(currentGame.getAvgRating()*100);

            TextArea rule = (TextArea) App.getScene().lookup("#rules");
            rule.setText(currentGame.getRules());

            List<InfoReview> reviews = controller.neo4jListGamesReviews(game);

            if(reviews.size() != 0) {
                for (int i = 0; i < reviews.size() || i < 3; i++) {
                    TextField review = (TextField) App.getScene().lookup("#review" + i);
                    review.setText(reviews.get(i).getText());

                    TextField author = (TextField) App.getScene().lookup("#author" + i);
                    author.setText(reviews.get(i).getAuthor());

                    TextField timestamp = (TextField) App.getScene().lookup("#timestamp" + i);
                    timestamp.setText(String.valueOf(reviews.get(i).getTimestamp()));

                    if (reviews.get(i).getAuthor().equals(login.getLoggedUser())) {
                        Button delete = (Button) App.getScene().lookup("#delete" + i);
                        delete.setDisable(false);
                        delete.setVisible(true);
                    }
                }
            }

            giàCaricato = 1;
        }
    }

    @FXML
    void postReview() {
        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();
        TextField review = (TextField) App.getScene().lookup("#review");
        Text title = (Text) App.getScene().lookup("#title");

        InfoReview toAdd = new InfoReview(review.getText(), title.getText(), login.getLoggedUser(), new Timestamp(System.currentTimeMillis()));
        controller.Neo4jAddReview(toAdd);

        TextField reviewList = (TextField) App.getScene().lookup("#review1");
        reviewList.setText(review.getText());
        TextField autore = (TextField) App.getScene().lookup("#author1");
        autore.setText(login.getLoggedUser());
        TextField timestamp = (TextField) App.getScene().lookup("#timestamp1");
        timestamp.setText(String.valueOf(new Timestamp(System.currentTimeMillis())));
        Button bottone = (Button) App.getScene().lookup("#delete1");
        bottone.setVisible(true);
        bottone.setDisable(false);

    }

    @FXML
    void goToGame() {

    }

    @FXML
    public void deleteReview(MouseEvent event) {
        Button deleteButton = (Button) event.getTarget();
        String id = deleteButton.getId();

        TextField reviewDaCancellare = null;
        TextField autore = null;
        TextField timestamp = null;

        if (id.equals("delete1")) {
            reviewDaCancellare = (TextField) App.getScene().lookup("#review1");
            autore = (TextField) App.getScene().lookup("#author1");
            timestamp = (TextField) App.getScene().lookup("#timestamp1");
        } else if (id.equals("delete2")) {
            reviewDaCancellare = (TextField) App.getScene().lookup("#review2");
            autore = (TextField) App.getScene().lookup("#author2");
            timestamp = (TextField) App.getScene().lookup("#timestamp2");
        } else {
            reviewDaCancellare = (TextField) App.getScene().lookup("#review3");
            autore = (TextField) App.getScene().lookup("#author3");
            timestamp = (TextField) App.getScene().lookup("#timestamp3");
        }

        InfoReview review = new InfoReview(reviewDaCancellare.getText(), games.getGame(), autore.getText(), new Timestamp(Long.parseLong(timestamp.getText())));

        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();
        controller.Neo4jDeleteReview(review);
    }

}
