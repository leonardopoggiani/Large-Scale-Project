package org.openjfx.Entities;



import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;


public class ArticleBean {
    private String title;
    private String author;
    private Timestamp timestamp;
    private String text;

    List<CommentBean> infoComments;

    public ArticleBean() {}

    public ArticleBean(String title, String author, Timestamp timestamp, List<CommentBean> infoComments) {
        this.title = title;
        this.author = author;
        this.timestamp = timestamp;
        this.infoComments = infoComments;
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

    public List<CommentBean> getComments() {
        return infoComments;
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
