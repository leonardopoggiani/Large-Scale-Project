package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.controller.ArticlesCommentsLikesDBController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class AddArticlePageView {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    @FXML
    TextField game;

    @FXML
    TextField game2;

    @FXML
    TextField title;

    @FXML
    TextArea body;

    @FXML
    Text alert;

    @FXML
    void initialize() {
        alert.setText("Add your article!");
    }

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }


    @FXML
    void publishArticle() {
        if(!title.getText().equals("") && !game.getText().equals("") && !body.getText().equals("")) {
            ArticleBean toPublish = new ArticleBean(title.getText(), LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), game.getText(),game2.getText());
            toPublish.setText(body.getText());
            ArticlesCommentsLikesDBController controller = new ArticlesCommentsLikesDBController();
            boolean ret = controller.addArticle(toPublish);

            if(ret) {
                alert.setText("Article published!");
                game.setText("");
                body.setText("");
                title.setText("");
                logger.info("article added " + toPublish );
            }
        } else {
            alert.setText("There's a problem, try to fix your article");
            alert.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    void reset() {
        game.setText("");
        body.setText("");
    }
}
