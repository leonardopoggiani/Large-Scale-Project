package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import it.unipi.dii.lsmdb.project.group5.controller.UsersPagesDBController;
import javafx.fxml.FXML;
import it.unipi.dii.lsmdb.project.group5.App;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.List;

/**
 * The type Homepage users.
 */
public class HomepageUsers {

    /**
     * The Logger.
     */
    private static String filter = "";

    /**
     * Gets filter.
     *
     * @return the filter
     */
public static String getFilter() {
        return filter;
    }


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

    @FXML
    Button statisticsButton;

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

    private Text chooseInfluencer(int i) {
        return switch (i){
            case 0 -> influencer1;
            case 1 -> influencer2;
            case 2 -> influencer3;
            case 3 -> influencer4;
            default -> new Text();
        };
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

    /**
     * Go to games.
     *
     * @throws IOException the io exception
     */
@FXML
    void goToGames() throws IOException {
        App.setRoot("HomepageGames");
    }

    /**
     * Go to groups.
     *
     * @throws IOException the io exception
     */
@FXML
    void goToGroups() throws IOException {
        App.setRoot("HomepageGroups");
    }

    /**
     * Go to friends.
     *
     * @throws IOException the io exception
     */
@FXML
    void goToFriends() throws IOException {
        App.setRoot("HomepageUsers");
    }

    /**
     * Go to settings.
     *
     * @throws IOException the io exception
     */
@FXML
    void goToSettings() throws IOException {
        App.setRoot("ProfileSettingsPageView");
    }

    /**
     * Go to statistics.
     *
     * @throws IOException the io exception
     */
@FXML
    void goToStatistics() throws IOException {
        App.setRoot("HomepageModeratorAnalytics");
    }

    /**
     * Logout.
     *
     * @throws IOException the io exception
     */
@FXML
    void logout() throws IOException {
        App.setRoot("LoginPageView");
        LoginPageView.logout();
    }


    /**
     * Initialize.
     */
@FXML
    void initialize() {
        showUsers();

        if(LoginPageView.getLoggedRole().equals("moderator")) {
            statisticsButton.setDisable(false);
        } else {
            statisticsButton.setDisable(true);
        }
    }

    private void showUsers() {
        username = LoginPageView.getLoggedUser();
        UsersPagesDBController controller = new UsersPagesDBController();

        List<String> user = controller.listUsers(username,"followingAll");

        Text nomeuser;
        for(int i = 0; i < 8; i++) {
            nomeuser = chooseUser(i);
            if(i < user.size()) {
                nomeuser.setText(user.get(i));
            } else {
                nomeuser.setText("");
            }
        }

        List<String> user2 = controller.listSuggestingFollowing(username,"normalUser");
        if(user2.size() == 0){
            user2 = controller.listUsers(username,"all");
            user2.removeAll(user);
        }

        for(int i = 0; i < 4; i++) {
            nomeuser = chooseSuggestUser(i);
            if(i < user2.size()) {
                nomeuser.setText(user2.get(i));
            } else {
                nomeuser.setText("");
            }
        }

        List<String> user3 = controller.listSuggestingFollowing(username,"influencer");

        for(int i = 0; i < 4; i++) {
            nomeuser = chooseInfluencer(i);
            if(i < user3.size()) {
                nomeuser.setText(user3.get(i));
            } else {
                nomeuser.setText("");
            }
        }
    }


    /**
     * Filter users.
     */
@FXML
    void filterUsers() {
        String filter = name.getText();
        UsersPagesDBController controller = new UsersPagesDBController();
        List<String> followed = controller.listUsers(username,"followingAll");

        if(filter != null && !filter.equals("")) {
            UserBean u = controller.showUser(filter);
            if(u != null && u.getUsername() != null) {
                if(followed.contains(u.getUsername())){
                    Text toShow = chooseUser(0);
                    toShow.setText(u.getUsername());
                    toShow.setFill(Color.YELLOW);
                } else {
                    Text toShow = chooseSuggestUser(0);
                    toShow.setText(u.getUsername());
                    toShow.setFill(Color.YELLOW);
                }
            }
        } else {
            showUsers();
        }
    }

    /**
     * Show followed users.
     *
     * @throws IOException the io exception
     */
@FXML
    void showFollowedUsers() throws IOException {
        filter = "followed";
        App.setRoot("UsersFilterPageView");
    }

    /**
     * Show suggested users.
     *
     * @throws IOException the io exception
     */
@FXML
    void showSuggestedUsers() throws IOException {
        filter = "suggested";
        App.setRoot("UsersFilterPageView");
    }

    /**
     * Show suggested influencers.
     *
     * @throws IOException the io exception
     */
@FXML
    void showSuggestedInfluencers() throws IOException {
        filter = "influencer";
        App.setRoot("UsersFilterPageView");
    }

    /**
     * Follow.
     *
     * @param event the event
     */
@FXML
    void follow(MouseEvent event) {
        UsersPagesDBController controller = new UsersPagesDBController();
        Button target = (Button) event.getSource();
        int id = Integer.parseInt(target.getId().substring(target.getId().length() - 1));

        Text user = chooseSuggestUser(id - 1);
        if(!user.getText().equals("")){
            boolean ret = controller.addRemoveFollow(username,user.getText(),"add");
            if(ret) {
                target.setStyle("-fx-background-color: green");
                user.setText("");
            }
        }

    }

    /**
     * Follow influencer.
     *
     * @param event the event
     */
@FXML
    void followInfluencer(MouseEvent event) {
        UsersPagesDBController controller = new UsersPagesDBController();
        Button target = (Button) event.getSource();
        int id = Integer.parseInt(target.getId().substring(target.getId().length() - 1));

        Text user = chooseInfluencer(id - 1);

        if(!user.getText().equals("")){
            boolean ret = controller.addRemoveFollow(username,user.getText(),"add");
            if(ret) {
                target.setStyle("-fx-background-color: green");
                user.setText("");
            }
        }

    }

    /**
     * Unfollow.
     *
     * @param event the event
     */
@FXML
    void unfollow(MouseEvent event) {

        UsersPagesDBController controller = new UsersPagesDBController();
        Button target = (Button) event.getSource();

        int id = Integer.parseInt(target.getId().substring(target.getId().length() - 1));

        Text user = chooseUser(id - 1);

        if(!user.getText().equals("")){
            boolean ret = controller.addRemoveFollow(username,user.getText(),"remove");
            if(ret) {
                target.setStyle("-fx-background-color: green");
                user.setText("");
            }
        }
    }
}
