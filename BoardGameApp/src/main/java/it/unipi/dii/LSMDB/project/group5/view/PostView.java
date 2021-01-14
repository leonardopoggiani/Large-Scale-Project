package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.bean.PostBean;
import it.unipi.dii.LSMDB.project.group5.controller.GroupsPagesDBController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class PostView {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    @FXML
    ScrollPane posts;

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
    void initialize() {

        GroupsPostsDBController controller = new GroupsPostsDBController();
        List<PostBean> postList = controller.neo4jShowGroupsPost(HomepageGroups.getGroup(),HomepageGroups.getAdminGroup());
        logger.info("size " + postList.size());
        VBox vertical = new VBox();
        for(int i = 0; i < postList.size(); i++) {
            PostPane newPane = new PostPane(postList.get(i));
            vertical.getChildren().add(newPane);
        }

        posts.setMaxSize(400,800);
        ancora.getChildren().addAll(vertical);
        posts.setContent(ancora);
    }

    @FXML
    void post() {
        logger.info("add post");
    }

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }
}
