package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.bean.PostBean;
import it.unipi.dii.lsmdb.project.group5.controller.GroupsPagesDBController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class PostPane extends Pane {

    public PostPane(PostBean post) {
        VBox vbox = new VBox();

        setPrefSize(220,90);
        setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: grey; -fx-border-radius: 15;");

        Text body = new Text(post.getText());
        body.setStyle("-fx-border-style: solid; -fx-border-width: 4px; -fx-background-color: grey");
        Text author = new Text(post.getAuthor());
        Text timestamp = new Text(String.valueOf(post.getTimestamp()));

        Button delete = new Button();
        delete.setText("Delete");
        delete.setStyle("-fx-border-radius: 25; -fx-background-color: red;");
        delete.setId(post.getAuthor() + post.getAuthor() + post.getTimestamp().toString());
        delete.setOnAction(λ -> {
            vbox.getChildren().removeAll(body,author,timestamp,delete);
            GroupsPagesDBController controller = new GroupsPagesDBController();
            controller.addDeletePost(post,"remove");
            getChildren().remove(vbox);
        });

        vbox.getChildren().addAll(body,author,timestamp,delete);
        vbox.setSpacing(2);
        vbox.setAlignment(Pos.CENTER);
        getChildren().addAll(vbox);

    }

}

