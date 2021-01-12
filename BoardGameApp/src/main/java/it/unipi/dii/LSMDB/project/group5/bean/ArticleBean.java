package it.unipi.dii.LSMDB.project.group5.bean;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;


public class ArticleBean extends @NonNull CompletableFuture<ArticleBean> {
    private String title;
    private String author;
    private Timestamp timestamp;
    private String text;
    private String game; //Per Clarissa forse?
    private int numLikes;
    private int numDislikes;
    private int numCommentes;
    private List<String> ListGame;

    List<CommentBean> infoComments;

    public ArticleBean() {}

    public List<String> getListGame() {
        return ListGame;
    }

    public ArticleBean(String title, String author, Timestamp timestamp, String game) {
        this.author = author;
        this.timestamp = timestamp;
        this.infoComments = infoComments;
        this.game = game;
    }

    public int getNumCommentes() {
        return numCommentes;
    }

    public int getNumDislikes() {
        return numDislikes;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getText(){return text;}

    public String getGame() {
        return game;
    }

    public void setListGame(List<String> listgame) {
        ListGame = listgame;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setText (String text){ this.text = text;}
    public void setComments(List<CommentBean> infoComments) {
        this.infoComments = infoComments;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public List<CommentBean> getComments() {
        return infoComments;
    }


    public void setNumCommentes(int numCommentes) {
        this.numCommentes = numCommentes;
    }

    public void setNumDislikes(int numDislikes) {
        this.numDislikes = numDislikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", text='" + text + '\'' +
                ", infoComments=" + infoComments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArticleBean article = (ArticleBean) o;
        return Objects.equals(title, article.title) && Objects.equals(author, article.author) && Objects.equals(timestamp, article.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, timestamp);
    }
}
