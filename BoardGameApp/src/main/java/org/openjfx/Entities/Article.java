package org.openjfx.Entities;



import javafx.scene.text.Text;

import java.util.Objects;
import java.util.List;
import java.util.Objects;


public class Article {
    private String title;
    private String author;
    private String timestamp;
    private String text;

    List<Comment> comments;


    public Article() {
    }


    public Article( String title, String author, String timestamp, List<Comment> comments) {
        this.title = title;
        this.author = author;
        this.timestamp = timestamp;
        this.comments = comments;
    }


    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getText(){return text;}

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setText (String text){ this.text = text;}
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(title, article.title) && Objects.equals(author, article.author) && Objects.equals(timestamp, article.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, timestamp);
    }
}
