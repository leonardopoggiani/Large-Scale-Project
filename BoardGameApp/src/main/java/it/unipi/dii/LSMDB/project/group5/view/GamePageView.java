package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import it.unipi.dii.LSMDB.project.group5.bean.RateBean;
import it.unipi.dii.LSMDB.project.group5.bean.ReviewBean;
import it.unipi.dii.LSMDB.project.group5.cache.GamesCache;
import it.unipi.dii.LSMDB.project.group5.controller.GamesReviewsRatesDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UpdateDatabaseDBController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GamePageView {

    Logger logger = Logger.getLogger(this.getClass().getName());
    GamesCache cache = GamesCache.getInstance();
    private static String game;

    @FXML
    ImageView image;

    @FXML
    Text title;

    @FXML
    Text votes;

    @FXML
    ProgressBar votes1;

    @FXML
    TextArea rules;

    @FXML
    TextArea description;

    @FXML
    Text categories;

    @FXML
    Text publisher;

    @FXML
    Hyperlink url;

    @FXML
    TextField review1;

    @FXML
    TextField review2;

    @FXML
    TextField review3;

    @FXML
    TextField author1;

    @FXML
    TextField author2;

    @FXML
    TextField author3;

    @FXML
    TextField timestamp1;

    @FXML
    TextField timestamp2;

    @FXML
    TextField timestamp3;

    @FXML
    Button delete1;

    @FXML
    Button delete2;

    @FXML
    Button delete3;

    @FXML
    TextField review;

    @FXML
    Slider rate;

    @FXML
    Button buttonrate;

    @FXML
    void initialize() throws ExecutionException {
        game = HomepageGames.getGame();
        setGameFields();
    }

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

        GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();
        GameBean currentGame = cache.getDataIfPresent(game);
        logger.info("show " + currentGame);
        if(currentGame == null || currentGame.getName() == null) {
            logger.log(Level.WARNING, "cache miss");
            currentGame = controller.showGame(game);
        } else {
            logger.log(Level.WARNING, "cache hit");
        }

        if( (currentGame.getImageUrl() != null) && !currentGame.getImageUrl().equals("") ) {
            image.setImage(new Image(currentGame.getImageUrl()));
        } else {
            logger.info("immagine di default");
            image.setImage(new Image("file:src/main/resources/img/defaultgioco.png"));
        }

        title.setText(currentGame.getName());
        votes.setText(String.valueOf(Math.round(currentGame.getAvgRating())));
        votes1.setProgress(currentGame.getAvgRating()/10);

        if (currentGame.getAvgRating() <= 5) {
            votes1.setStyle("-fx-background-color: red");
            votes1.setStyle("-fx-accent: red");
        } else if( (currentGame.getAvgRating() > 5) && (currentGame.getAvgRating() < 7) ){
            votes1.setStyle("-fx-background-color: blue");
            votes1.setStyle("-fx-accent: blue");
        } else if( (currentGame.getAvgRating() >= 7) ){
            votes1.setStyle("-fx-background-color: green");
            votes1.setStyle("-fx-accent: green");
        }

        rules.setText(currentGame.getRules());
        description.setText(currentGame.getDescription());
        String cat = "";
        if(currentGame.getListCategory() != null) {
            for (int i = 0; i < currentGame.getListCategory().size(); i++) {
                cat += currentGame.getListCategory().get(i);

                if(i == currentGame.getListCategory().size() - 1) {
                    cat += " ";
                } else {
                    cat += ", ";
                }
            }
        }

        categories.setText(cat);

        String pub = "";
        if(currentGame.getPublisher() != null) {
            for (int i = 0; i < currentGame.getPublisher().size(); i++) {
                pub += currentGame.getPublisher().get(i);

                if (i == currentGame.getPublisher().size() - 1) {
                    pub += " ";
                } else {
                    pub += ", ";
                }
            }
        }
        publisher.setText(pub);
        url.setText(currentGame.getUrl());

        setReviews(game);
    }

    private TextField chooseReview(int i){
        return switch (i) {
            case 1 -> review1;
            case 2 -> review2;
            case 3 -> review3;
            default -> new TextField();
        };
    }

    private TextField chooseAuthor(int i){
        return switch (i) {
            case 1 -> author1;
            case 2 -> author2;
            case 3 -> author3;
            default -> new TextField();
        };
    }

    private TextField chooseTimestamp(int i){
        return switch (i) {
            case 1 -> timestamp1;
            case 2 -> timestamp2;
            case 3 -> timestamp3;
            default -> new TextField();
        };
    }

    private Button chooseDeleteButton(int i){
        return switch (i) {
            case 1 -> delete1;
            case 2 -> delete2;
            case 3 -> delete3;
            default -> new Button();
        };
    }

    private void setReviews(String game) {
        GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();

        List<ReviewBean> reviews = controller.neo4jListGamesReviews(game,3);
        System.out.println("Numero di review: " + reviews.size());

        if(!reviews.isEmpty()) {
            for (int i = 0; i < reviews.size() && i < 3; i++) {
                TextField review = chooseReview(i + 1);
                TextField author = chooseAuthor(i + 1);
                TextField timestamp = chooseTimestamp(i + 1);

                review.setText(reviews.get(i).getText());
                author.setText(reviews.get(i).getAuthor());
                timestamp.setText(String.valueOf(reviews.get(i).getTimestamp()));

                if (reviews.get(i).getAuthor().equals(LoginPageView.getLoggedUser())) {
                    Button delete = chooseDeleteButton(i + 1);
                    delete.setDisable(false);
                    delete.setVisible(true);
                }
            }
        }
    }

    @FXML
    void postReview() {
        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();

        ReviewBean toAdd = new ReviewBean(review.getText(), title.getText(), LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()));
        controller.Neo4jAddReview(toAdd);

        review.setText("");
        setReviews(game);
    }

    @FXML
    public void deleteReview(MouseEvent event) {
        Button deleteButton = (Button) event.getSource();
        int index = Integer.parseInt(deleteButton.getId().substring(deleteButton.getId().length() - 1));

        TextField reviewDaCancellare = chooseReview(index);
        TextField autore = chooseAuthor(index);
        TextField timestamp = chooseTimestamp(index);

        ReviewBean review = new ReviewBean(reviewDaCancellare.getText(), game, autore.getText(), Timestamp.valueOf(timestamp.getText()));

        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();
        controller.Neo4jDeleteReview(review);

        setReviews(game);
    }

    @FXML
    void addVote() {
        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();

        RateBean newRate = new RateBean(LoginPageView.getLoggedUser(), rate.getValue(), game, new Timestamp(System.currentTimeMillis()));
        boolean ret = controller.Neo4jAddRating(newRate);

        if(ret) {
            double votoMedio = controller.MongoDBgetAvgRating(newRate.getGame());
            votes1.setProgress(votoMedio / 10);
            votes.setText(String.valueOf(Math.round(votoMedio)));
            rate.setValue(0);
            buttonrate.setStyle("-fx-background-color: green");
        }
    }

}
