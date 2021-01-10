package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.openjfx.App;
import org.openjfx.Controller.GamesReviewsRatesDBController;
import org.openjfx.Controller.UpdateDatabaseDBController;
import org.openjfx.Entities.InfoGame;
import org.openjfx.Entities.InfoRate;
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
        // setto i campi del gioco
        if (giàCaricato == -1) {
            ImageView image = (ImageView) App.getScene().lookup("#image");
            String game = games.getGame();
            logger.info("Carico " + game);
            GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();

            InfoGame currentGame = controller.showGame(game);
            System.out.println(currentGame);

            if( (currentGame.getImageUrl() != null) && !(currentGame.getImageUrl().equals("")) ) {
                System.out.println("Immagine " + currentGame.getImageUrl());
                image.setImage(new Image(currentGame.getImageUrl()));
            }

            Text title = (Text) App.getScene().lookup("#title");
            title.setText(currentGame.getName());

            Text vote = (Text) App.getScene().lookup("#votes");
            vote.setText(String.valueOf(Math.round(currentGame.getAvgRating())));
            ProgressBar votes = (ProgressBar) App.getScene().lookup("#votes1");
            votes.setProgress(currentGame.getAvgRating()/10);
            System.out.println(" Progress " + currentGame.getAvgRating());
            if (currentGame.getAvgRating() <= 5) {
                votes.setStyle("-fx-background-color: red");
                votes.setStyle("-fx-accent: red");
            } else if( (currentGame.getAvgRating() > 5) && (currentGame.getAvgRating() < 7) ){
                votes.setStyle("-fx-background-color: blue");
                votes.setStyle("-fx-accent: blue");
            } else if( (currentGame.getAvgRating() >= 7) ){
                votes.setStyle("-fx-background-color: green");
                votes.setStyle("-fx-accent: green");
            }

            TextArea rule = (TextArea) App.getScene().lookup("#rules");
            System.out.println("Regole" + currentGame.getRules());
            rule.setText(currentGame.getRules());

            TextArea description = (TextArea) App.getScene().lookup("#description");
            System.out.println("Descrizione" + currentGame.getDescription());
            description.setText(currentGame.getDescription());

            setReviews(game);

            giàCaricato = 1;
        }
    }

    private void setReviews(String game) {
        GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();

        List<InfoReview> reviews = controller.neo4jListGamesReviews(game);
        System.out.println("Numero di review: " + reviews.size());

        if(reviews.size() != 0) {
            for (int i = 0; i < reviews.size() || i < 3; i++) {
                TextField review = (TextField) App.getScene().lookup("#review" + (i + 1));
                review.setText(reviews.get(i).getText());

                TextField author = (TextField) App.getScene().lookup("#author" + (i + 1));
                author.setText(reviews.get(i).getAuthor());

                TextField timestamp = (TextField) App.getScene().lookup("#timestamp" + (i + 1));
                timestamp.setText(String.valueOf(reviews.get(i).getTimestamp()));

                if (reviews.get(i).getAuthor().equals(login.getLoggedUser())) {
                    Button delete = (Button) App.getScene().lookup("#delete" + (i + 1));
                    delete.setDisable(false);
                    delete.setVisible(true);
                }
            }
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
        Button deleteButton = (Button) event.getSource();
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

        InfoReview review = new InfoReview(reviewDaCancellare.getText(), games.getGame(), autore.getText(), Timestamp.valueOf(timestamp.getText()));

        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();
        controller.Neo4jDeleteReview(review);

        setReviews(games.getGame());
    }

    @FXML
    void addVote() {
        Slider voti = (Slider) App.getScene().lookup("#rate");
        Text game = (Text) App.getScene().lookup("#title");
        ProgressBar progress = (ProgressBar) App.getScene().lookup("#votes1");
        Text voting = (Text) App.getScene().lookup("#votes");

        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();

        InfoRate rate = new InfoRate(login.getLoggedUser(), voti.getValue(), game.getText(), new Timestamp(System.currentTimeMillis()));
        boolean ret = controller.Neo4jAddRating(rate);

        System.out.println("Aggiungo rate " + ret);

        if(ret) {
            double votoMedio = controller.MongoDBgetAvgRating(rate.getGame());
            System.out.println("Voto: " + votoMedio);
            progress.setProgress(votoMedio / 10);
            voting.setText(String.valueOf(Math.round(votoMedio)));
        }

    }
}
