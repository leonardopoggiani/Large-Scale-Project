package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.bean.ArticleBean;
import it.unipi.dii.lsmdb.project.group5.controller.ArticlesPagesDBController;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Timestamp;

/** View of the page responsible for adding an article. */
public class AddArticlePageView {

  /** The Logger. */

  /** The Game. */
  @FXML TextField game;

  /** The Game 2. */
  @FXML TextField game2;

  /** The Title. */
  @FXML TextField title;

  /** The Body. */
  @FXML TextArea body;

  /** The Alert. */
  @FXML Text alert;

  /** Initialize. */
  @FXML
  void initialize() {
        alert.setText("Add your article!");
    }

  /**
   * Return to homepage.
   *
   * @throws IOException the io exception
   */
  @FXML
  void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

  /** Publish an article (if possible). */
  @FXML
  void publishArticle() {
        if(!title.getText().equals("") && !game.getText().equals("") && !body.getText().equals("")) {
            ArticleBean toPublish = new ArticleBean(title.getText(), LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), game.getText(),game2.getText());
            toPublish.setText(body.getText());

            ArticlesPagesDBController controller = new ArticlesPagesDBController();
            boolean ret = controller.addArticle(toPublish);

            if(ret) {
                alert.setText("Article published!");
                game.setText("");
                body.setText("");
                title.setText("");
                Logger.log("article added " + toPublish );
            }
        } else {
            Logger.warning("error on adding article ");
            alert.setText("There's a problem, try to fix your article");
            alert.setStyle("-fx-text-fill: red");
        }
    }

  /** Reset article fields. */
  @FXML
  void reset() {
        game.setText("");
        body.setText("");
    }
}
