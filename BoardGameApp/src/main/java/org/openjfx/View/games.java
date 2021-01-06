package org.openjfx.View;

import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private static String game;

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
    void goToGame(MouseEvent event) throws IOException {
        logger.info("Carico " + event.getTarget().getClass());
        AnchorPane foo = new AnchorPane();

        if(event.getTarget().getClass() == foo.getClass()){
            AnchorPane pane = (AnchorPane) event.getTarget();
            TitledPane tp = (TitledPane) App.getScene().lookup("#full" + pane.getId());

            game = tp.getText();
            App.setRoot("game");
        }
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
                    Text category1 = (Text) App.getScene().lookup("#category1" + (i + 1));
                    Text category2 = (Text) App.getScene().lookup("#category2" + (i + 1));

                    gioco.setText(g.getName());

                    if(!g.getCategory1().equals("null")) {
                        category1.setText(g.getCategory1());
                    } else {
                        category1.setText("");
                    }
                    if(!g.getCategory2().equals("null")) {
                        category2.setText(g.getCategory2());
                    } else {
                        category2.setText("");
                    }
                }
            }

            giàCaricato = 1;
        }
    }

    public static String getGame(){
        return game;
    }
}
