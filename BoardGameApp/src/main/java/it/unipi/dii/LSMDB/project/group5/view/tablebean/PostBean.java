package it.unipi.dii.LSMDB.project.group5.view.tablebean;

import javafx.beans.property.SimpleStringProperty;

public class PostBean {

    SimpleStringProperty author;
    SimpleStringProperty timestamp;
    SimpleStringProperty message;

    public PostBean(String name, String time, String text){
        this.author = new SimpleStringProperty(name);
        this.timestamp = new SimpleStringProperty(time);
        this.message = new SimpleStringProperty(text);
    }
    public String getAuthor(){ return author.get(); }
    public String getTimestamp(){ return timestamp.get(); }
    public String getMessage(){ return message.get(); }

    public void setAuthor(String name) {this.author = new SimpleStringProperty(name); }
    public void setTimestamp(String name) {this.author = new SimpleStringProperty(name); }

}
