package it.unipi.dii.lsmdb.project.group5.bean;

import com.google.common.collect.Lists;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ArticleBean extends @NonNull CompletableFuture<ArticleBean> {
    private String title;
    private String author;
    private Timestamp timestamp;
    private String text;
    private int numLikes;
    private int numDislikes;
    private int numComments;
    private List<String> ListGame;
    private int id;

    public ArticleBean() {}

    public List<String> getListGame() {
        return ListGame;
    }

    public ArticleBean(String title, String author, Timestamp timestamp, String game, String game2) {
        this.title = title;
        this.author = author;
        this.timestamp = timestamp;
        ListGame = Lists.newArrayList(game,game2);
    }

    public int getId (){return id;}

    public int getNumberComments() {
        return numComments;
    }

    public int getNumberDislike() {
        return numDislikes;
    }

    public int getNumberLikes() {
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

    public void setId (int id) {this.id =id;}

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

    public void setNumberComments(int numComments) {
        this.numComments = numComments;
    }

    public void setNumberDislikes(int numDislikes) {
        this.numDislikes = numDislikes;
    }

    public void setNumberLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    @Override
    public String toString() {
        return "Article{" +
                 ", author='" + author + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", text='" + text + '\'' +
                ", id=" + id +
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
