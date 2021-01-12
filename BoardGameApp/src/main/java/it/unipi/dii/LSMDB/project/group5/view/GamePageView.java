package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import it.unipi.dii.LSMDB.project.group5.bean.RateBean;
import it.unipi.dii.LSMDB.project.group5.bean.ReviewBean;
import it.unipi.dii.LSMDB.project.group5.cache.ArticlesCache;
import it.unipi.dii.LSMDB.project.group5.cache.GamesCache;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.controller.GamesReviewsRatesDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UpdateDatabaseDBController;
import javafx.scene.input.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePageView {
    Logger logger = Logger.getLogger(this.getClass().getName());
    int giàCaricato = -1;
    GamesCache cache = GamesCache.getInstance();

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

    @FXML
    void goToGames() throws IOException {
        App.setRoot("HomepageGames");
    }

    @FXML
    void goToGroups() throws IOException {
        App.setRoot("HomepageGroups");
    }

    @FXML
    void goToFriends() throws IOException {
        App.setRoot("HomepageGroups");
    }

    @FXML
    void setGameFields() throws ExecutionException {
        // setto i campi del gioco
        if (giàCaricato == -1) {
            ImageView image = (ImageView) App.getScene().lookup("#image");
            String game = HomepageGames.getGame();
            GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();

            GameBean currentGame = cache.getDataIfPresent(game);

            if(currentGame == null || currentGame.getName() == null) {
                logger.log(Level.WARNING, "Recupero da db");
                currentGame = controller.showGame(game);
            }

            System.out.println(currentGame);

            if( (currentGame.getImageUrl() != null) && !currentGame.getImageUrl().equals("") ) {
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
            rule.setText(currentGame.getRules());

            TextArea description = (TextArea) App.getScene().lookup("#description");
            description.setText(currentGame.getDescription());

            Text categories = (Text) App.getScene().lookup("#categories");
            String cat = "";
            if(currentGame.getListCategory() != null) {
                for (int i = 0; i < currentGame.getListCategory().size(); i++) {
                    cat += currentGame.getListCategory().get(i);
                    cat += ", ";
                }
            }

            categories.setText(cat);

            setReviews(game);

            giàCaricato = 1;
        }
    }

    private void setReviews(String game) {
        GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();

        List<ReviewBean> reviews = controller.neo4jListGamesReviews(game);
        System.out.println("Numero di review: " + reviews.size());

        if(!reviews.isEmpty()) {
            for (int i = 0; i < reviews.size() && i < 3; i++) {
                TextField review = (TextField) App.getScene().lookup("#review" + (i + 1));
                review.setText(reviews.get(i).getText());

                TextField author = (TextField) App.getScene().lookup("#author" + (i + 1));
                author.setText(reviews.get(i).getAuthor());

                TextField timestamp = (TextField) App.getScene().lookup("#timestamp" + (i + 1));
                timestamp.setText(String.valueOf(reviews.get(i).getTimestamp()));

                if (reviews.get(i).getAuthor().equals(LoginPageView.getLoggedUser())) {
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

        ReviewBean toAdd = new ReviewBean(review.getText(), title.getText(), LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()));
        controller.Neo4jAddReview(toAdd);

        TextField reviewList = (TextField) App.getScene().lookup("#review1");
        reviewList.setText(review.getText());
        TextField autore = (TextField) App.getScene().lookup("#author1");
        autore.setText(LoginPageView.getLoggedUser());
        TextField timestamp = (TextField) App.getScene().lookup("#timestamp1");
        timestamp.setText(String.valueOf(new Timestamp(System.currentTimeMillis())));
        Button bottone = (Button) App.getScene().lookup("#delete1");
        bottone.setVisible(true);
        bottone.setDisable(false);
        review.setText("");
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

        ReviewBean review = new ReviewBean(reviewDaCancellare.getText(), HomepageGames.getGame(), autore.getText(), Timestamp.valueOf(timestamp.getText()));

        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();
        controller.Neo4jDeleteReview(review);

        setReviews(HomepageGames.getGame());
    }

    @FXML
    void addVote() {
        Slider voti = (Slider) App.getScene().lookup("#rate");
        Text game = (Text) App.getScene().lookup("#title");
        ProgressBar progress = (ProgressBar) App.getScene().lookup("#votes1");
        Text voting = (Text) App.getScene().lookup("#votes");

        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();

        RateBean rate = new RateBean(LoginPageView.getLoggedUser(), voti.getValue(), game.getText(), new Timestamp(System.currentTimeMillis()));
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
