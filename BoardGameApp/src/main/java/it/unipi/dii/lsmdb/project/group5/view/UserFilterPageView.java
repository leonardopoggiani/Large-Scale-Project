package it.unipi.dii.lsmdb.project.group5.view;

import com.google.common.collect.Lists;
import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.controller.UsersPagesDBController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class UserFilterPageView {

    Logger logger =  Logger.getLogger(this.getClass().getName());


    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageUsers");
    }

    @FXML
    AnchorPane ancora;

    @FXML
    void initialize() {
        VBox lista = new VBox();

        UsersPagesDBController controller = new UsersPagesDBController();
        List<String> listaUtenti = Lists.newArrayList();

        boolean unfollowing = false;

        switch (HomepageUsers.getFilter()) {
            case "followed":
                listaUtenti = controller.listUsers(LoginPageView.getLoggedUser(),"followingAll");
                unfollowing = true;
                break;
            case "suggested":
                listaUtenti = controller.listSuggestingFollowing(LoginPageView.getLoggedUser(),"normalUser");
                break;
            case "influencer":
                listaUtenti = controller.listSuggestingFollowing(LoginPageView.getLoggedUser(),"influencer");
                break;
            default:
                break;
        }

        for(int i = 0; i < listaUtenti.size(); i++) {
            HBox nuovoUtente = new HBox();
            TextArea utente = new TextArea(listaUtenti.get(i));
            Button follow;
            if (unfollowing) {
                follow = new Button("Unfollow");
                follow.setOnAction(lambda -> {
                    boolean ret = controller.addRemoveFollow(LoginPageView.getLoggedUser(), utente.getText(), "remove");
                    utente.setText("Unfollow user!");
                    follow.setText(":'(");
                    follow.setDisable(true);
                });
            } else {
                follow = new Button("Follow");
                follow.setOnAction(
                lambda -> {
                    boolean ret =
                        controller.addRemoveFollow(
                            LoginPageView.getLoggedUser(), utente.getText(), "add");
                        utente.setText("Followed user!");
                        follow.setText("Great!");
                        follow.setDisable(true);
                    });
            }

            utente.setMaxHeight(40);
            utente.setMaxWidth(100);
            nuovoUtente.getChildren().addAll(utente, follow);
            lista.getChildren().add(nuovoUtente);

        }

        ancora.getChildren().add(lista);
    }

}
