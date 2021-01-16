package it.unipi.dii.LSMDB.project.group5.view.tablebean;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;

public class PostTableBean {
    private SimpleStringProperty author;
    private SimpleStringProperty text;
    private SimpleStringProperty timestamp;

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = new SimpleStringProperty(String.valueOf(timestamp));
    }

    public void setAuthor(String author) {
        this.author = new SimpleStringProperty(author);
    }

    public void setText(String text) {
        this.text = new SimpleStringProperty(text);
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getText() {
        return text.get();
    }

}
