package org.openjfx.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import org.openjfx.App;
import org.openjfx.Controller.HomepageController;
import org.openjfx.Entities.Article;

import java.io.IOException;
import java.util.List;

public class homePage {

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
    void logout() throws IOException {
        App.setRoot("login");
        login.logout();
    }

    @FXML
    void setSuggestedArticles() throws IOException {
        if(giàCaricato == -1) {
            HomepageController home = new HomepageController();
            List<Article> list = home.showSuggestedArticlesController(login.getLoggedUser());

            if (list != null) {
                System.out.println("Lunghezza lista " + list.size());
                for (int i = 0; i < list.size(); i++) {
                    Article a = list.get(i);
                    TitledPane articolo = (TitledPane) App.getScene().lookup("#articolo" + (i + 1));
                    Text author = (Text) App.getScene().lookup("#author" + (i + 1));
                    Text timestamp = (Text) App.getScene().lookup("#timestamp" + (i + 1));

                    articolo.setText(a.getTitle());
                    author.setText(a.getAuthor());
                    timestamp.setText(String.valueOf(a.getTimestamp()));
                }
            }

            giàCaricato = 1;
        }
    }

    @FXML
    void goToArticle(final ActionEvent event) throws IOException {
        App.setRoot("article");
    }
}
