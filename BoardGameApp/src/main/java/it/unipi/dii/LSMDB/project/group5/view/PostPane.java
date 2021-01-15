package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.bean.PostBean;
import it.unipi.dii.LSMDB.project.group5.controller.GroupsPagesDBController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class PostPane extends Pane {

    public PostPane(PostBean post) {
        VBox vbox = new VBox();

        setPrefSize(200,200);
        setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: grey; -fx-border-radius: 15;");

        TextArea body = new TextArea(post.getText());
        body.setMaxHeight(20);
        body.setMaxWidth(180);
        body.setEditable(false);
        body.setStyle("-fx-border-color: white");

        TextArea author = new TextArea(post.getAuthor());
        author.setMaxHeight(20);
        author.setMaxWidth(180);
        author.setEditable(false);
        author.setStyle("-fx-border-color: white");

        TextArea timestamp = new TextArea(String.valueOf(post.getTimestamp()));
        timestamp.setMaxHeight(20);
        timestamp.setMaxWidth(180);
        timestamp.setEditable(false);
        timestamp.setStyle("-fx-border-color: white");

        Button delete = new Button();
        delete.setText("Delete");
        delete.setId(post.getAuthor() + post.getAuthor() + post.getTimestamp().toString());
        delete.setOnAction(λ -> {
            vbox.getChildren().removeAll(body,author,timestamp,delete);
            GroupsPagesDBController controller = new GroupsPagesDBController();
            controller.addDeletePost(post,"remove");
        });

        vbox.getChildren().addAll(body,author,timestamp,delete);
        vbox.setSpacing(2);
        vbox.setAlignment(Pos.BASELINE_CENTER);
        getChildren().addAll(vbox);

    }

}

