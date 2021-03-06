package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.bean.PostBean;
import it.unipi.dii.lsmdb.project.group5.controller.GroupsPagesDBController;
import it.unipi.dii.lsmdb.project.group5.view.javafxutils.PostPane;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/** The type Post view. */
public class PostView {

  @FXML
  ScrollPane posts;

  @FXML
  ScrollPane scrollingPane;

  @FXML
  Text message;

  @FXML
  TextArea newmessage;

  @FXML
  Button post;

  @FXML
  Button home;

  @FXML
  AnchorPane ancora;

  @FXML
  ImageView tic;

  @FXML
  ImageView ics;

  /** The Controller. */
  GroupsPagesDBController controller = new GroupsPagesDBController();

    private int LIMIT_QUERY_SIZE = 10;

  /** Initialize. */
  @FXML
  void initialize() {
        tic.setVisible(false);
        ics.setVisible(false);
        showPosts();
    }

    private void showPosts(){
        List<PostBean> postList = controller.showGroupsPost(HomepageGroups.getGroup(),HomepageGroups.getAdminGroup(), LIMIT_QUERY_SIZE);

        VBox vertical = new VBox(5);
        for(int i = 0; i < postList.size(); i++) {
            PostPane newPane = new PostPane(postList.get(i));
            vertical.getChildren().add(newPane);
        }

        posts.setMaxSize(400,800);
        scrollingPane.setContent(vertical);
        posts.setContent(ancora);
    }

  /** Post. */
  @FXML
  void post() {
        GroupsPagesDBController controller = new GroupsPagesDBController();

        PostBean newPost = new PostBean(LoginPageView.getLoggedUser(),newmessage.getText(),String.valueOf(new Timestamp(System.currentTimeMillis())),HomepageGroups.getGroup(),HomepageGroups.getAdminGroup());

        boolean ret = false;
        if(!newPost.getText().equals("")) {
            ret = controller.addDeletePost(newPost,"add");
        }

        if(ret) {
            tic.setVisible(true);
            newmessage.setText("");
            HomepageGroups home = new HomepageGroups();
        } else {
            ics.setVisible(true);
            newmessage.setStyle("-fx-text-fill: red");
        }

        showPosts();
    }

  /**
   * Return to homepage.
   *
   * @throws IOException the io exception
   */
  @FXML
  void returnToHomepage() throws IOException {
        App.setRoot("HomepageGroups");
    }
}
