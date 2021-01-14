package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.bean.PostBean;
import it.unipi.dii.LSMDB.project.group5.controller.GroupsPostsDBController;
import javafx.fxml.FXML;

import java.util.List;

public class PostView {

    @FXML
    void initialize() {
        GroupsPostsDBController controller = new GroupsPostsDBController();
        List<PostBean> posts = controller.neo4jShowGroupsPost(HomepageGroups.getGroup(),HomepageGroups.getAdminGroup());

        for(int i = 0; i < posts.size(); i++) {
            PostPane post = new PostPane();
        }
    }
}
