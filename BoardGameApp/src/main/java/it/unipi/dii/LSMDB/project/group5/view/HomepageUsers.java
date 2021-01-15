package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.controller.UsersPageDBController;
import javafx.fxml.FXML;
import it.unipi.dii.LSMDB.project.group5.App;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class HomepageUsers {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    private static String filter = "";

    public static String getFilter() {
        return filter;
    }

    @FXML
    Text following;

    @FXML
    CheckBox showfollowed;

    @FXML
    CheckBox showusers;

    @FXML
    CheckBox showinfluencer;

    @FXML
    TextField name;

    @FXML
    Text username1;

    @FXML
    Text username2;

    @FXML
    Text username3;

    @FXML
    Text username4;

    @FXML
    Text username5;

    @FXML
    Text username6;

    @FXML
    Text username7;

    @FXML
    Text username8;

    @FXML
    Text suggested1;

    @FXML
    Text suggested2;

    @FXML
    Text suggested3;

    @FXML
    Text suggested4;

    @FXML
    Text influencer1;

    @FXML
    Text influencer2;

    @FXML
    Text influencer3;

    @FXML
    Text influencer4;

    @FXML
    Button unfollow1;

    @FXML
    Button unfollow2;

    @FXML
    Button unfollow3;

    @FXML
    Button unfollow4;

    @FXML
    Button unfollow5;

    @FXML
    Button unfollow6;

    @FXML
    Button unfollow7;

    @FXML
    Button unfollow8;

    @FXML
    Button followuser1;

    @FXML
    Button followuser2;

    @FXML
    Button followuser3;

    @FXML
    Button followuser4;

    @FXML
    Button followinfluencer1;

    @FXML
    Button followinfluencer2;

    @FXML
    Button followinfluencer3;

    @FXML
    Button followinfluencer4;

    private String username;

    private Button chooseUnfollowButton(int i){
        return switch (i) {
            case 0 -> unfollow1;
            case 1 -> unfollow2;
            case 2 -> unfollow3;
            case 3 -> unfollow4;
            case 4 -> unfollow5;
            case 5 -> unfollow6;
            case 6 -> unfollow7;
            case 7 -> unfollow8;
            default -> new Button();
        };
    }

    private Text chooseUser(int i){
        return switch (i) {
            case 0 -> username1;
            case 1 -> username2;
            case 2 -> username3;
            case 3 -> username4;
            case 4 -> username5;
            case 5 -> username6;
            case 6 -> username7;
            case 7 -> username8;
            default -> new Text();
        };
    }

    private Text chooseSuggestUser(int i) {
        return switch (i) {
            case 0 -> suggested1;
            case 1 -> suggested2;
            case 2 -> suggested3;
            case 3 -> suggested4;
            default -> new Text();
        };
    }

    private Text chooseSuggestInfluencer(int i) {
        return switch (i) {
            case 0 -> influencer1;
            case 1 -> influencer2;
            case 2 -> influencer3;
            case 3 -> influencer4;
            default -> new Text();
        };
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
        App.setRoot("HomepageUsers");
    }

    @FXML
    void goToSettings() throws IOException {
        App.setRoot("ProfileSettingsPageView");
    }

    @FXML
    void initialize() {
        username = LoginPageView.getLoggedUser();
        UsersPageDBController controller = new UsersPageDBController();

        List<String> user = controller.listUsers(username,"followingAll");
        following.setText(String.valueOf(user.size()));
        logger.info("size " + user.size());

        System.out.println(user.toString());
        Text nomeuser;
        for(int i = 0; i < 6; i++) {
            nomeuser = chooseUser(i);
            if(i < user.size()) {
                nomeuser.setText(user.get(i));
            } else {
                nomeuser.setText("");
            }
        }

        user.clear();
        user = controller.listSuggestingFollowing(username,"normalUser");
        logger.info("size " + user.size());

        for(int i = 0; i < 6; i++) {
            nomeuser = chooseSuggestUser(i);
            if(i < user.size()) {
                nomeuser.setText(user.get(i));
            } else {
                nomeuser.setText("");
            }
        }

        user.clear();
        user = controller.listSuggestingFollowing(username,"influencer");
        logger.info("size " + user.size());

        for(int i = 0; i < 6; i++) {
            nomeuser = chooseSuggestInfluencer(i);
            if(i < user.size()) {
                nomeuser.setText(user.get(i));
            } else {
                nomeuser.setText("");
            }
        }

    }

    @FXML
    void filterUsers() {

    }

    @FXML
    void showFollowedUsers() throws IOException {
        showusers.setSelected(false);
        showinfluencer.setSelected(false);
        filter = "followed";
        App.setRoot("UsersFilterPageView");
    }

    @FXML
    void showSuggestedUsers() throws IOException {
        showfollowed.setSelected(false);
        showinfluencer.setSelected(false);
        filter = "suggested";
        App.setRoot("UsersFilterPageView");

    }

    @FXML
    void showSuggestedInfluencers() throws IOException {
        showusers.setSelected(false);
        showfollowed.setSelected(false);
        filter = "influencer";
        App.setRoot("UsersFilterPageView");

    }

    @FXML
    void follow(MouseEvent event) {
        logger.info("follow");
        UsersPageDBController controller = new UsersPageDBController();
        Button target = (Button) event.getSource();
        int id = Integer.parseInt(target.getId().substring(target.getId().length() - 1));

        Text user = chooseSuggestInfluencer(id - 1);
        logger.info("user " + user);
        if(!user.getText().equals("")){
            boolean ret = controller.addRemoveFollow(username,user.getText(),"add");
            logger.info("ret " + ret);
            if(ret) {
                target.setStyle("-fx-background-color: green");
                user.setText("");
            }
        }

    }

    @FXML
    void unfollow(MouseEvent event) {
        logger.info("unfollow");

        UsersPageDBController controller = new UsersPageDBController();
        Button target = (Button) event.getSource();

        int id = Integer.parseInt(target.getId().substring(target.getId().length() - 1));

        Text user = chooseSuggestInfluencer(id - 1);

        if(!user.getText().equals("")){
            boolean ret = controller.addRemoveFollow(username,user.getText(),"remove");

            if(ret) {
                target.setStyle("-fx-background-color: green");
                user.setText("");
            }
        }
    }
}
