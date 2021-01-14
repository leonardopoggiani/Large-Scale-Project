package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.bean.PostBean;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class PostPane extends Pane {

    public PostPane(PostBean post) {
        VBox vbox = new VBox();

        setPrefSize(200,150);
        setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: grey; -fx-border-radius: 15;");

        TextArea body = new TextArea(post.getText());
        body.setMaxHeight(20);
        body.setMaxWidth(180);
        body.setEditable(false);

        TextArea author = new TextArea(post.getAuthor());
        author.setMaxHeight(20);
        author.setMaxWidth(180);
        author.setEditable(false);

        TextArea timestamp = new TextArea(String.valueOf(post.getTimestamp()));
        timestamp.setMaxHeight(20);
        timestamp.setMaxWidth(180);
        timestamp.setEditable(false);

        vbox.getChildren().addAll(body,author,timestamp);
        vbox.setSpacing(2);
        vbox.setAlignment(Pos.BASELINE_CENTER);
        getChildren().addAll(vbox);

    }

}

