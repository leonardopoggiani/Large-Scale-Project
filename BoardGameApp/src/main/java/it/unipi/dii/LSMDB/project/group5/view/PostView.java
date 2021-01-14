package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.bean.PostBean;
import it.unipi.dii.LSMDB.project.group5.controller.GroupsPagesDBController;
import javafx.fxml.FXML;

import java.util.List;

public class PostView {

    @FXML
    void initialize() {
        GroupsPagesDBController controller = new GroupsPagesDBController();
        List<PostBean> posts = controller.showGroupsPost(HomepageGroups.getGroup(),HomepageGroups.getAdminGroup(), 10);

        for(int i = 0; i < posts.size(); i++) {
            PostPane post = new PostPane();
        }
    }
}
