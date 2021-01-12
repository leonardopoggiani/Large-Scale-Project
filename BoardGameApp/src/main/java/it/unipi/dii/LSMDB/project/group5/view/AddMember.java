package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.controller.GroupsPostsDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UsersDBController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddMember implements Initializable {
    UsersDBController controller = new UsersDBController();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> utenti = controller.neo4jListUsers(HomepageGroups.getGroup(),"followersAll");
    }

}
