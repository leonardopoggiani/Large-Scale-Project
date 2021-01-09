package org.openjfx.View;

import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.openjfx.App;
import org.openjfx.Controller.ArticlesCommentsLikesDBController;
import org.openjfx.Controller.UpdateDatabaseDBController;
import org.openjfx.Entities.Article;
import org.openjfx.Entities.InfoComment;
import org.openjfx.Entities.InfoLike;
import org.openjfx.Entities.InfoReview;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

public class article {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    int giàCaricato = -1;

    @FXML
    void setArticleFields() throws IOException {

       if(giàCaricato == -1) {
           ArticlesCommentsLikesDBController article = new ArticlesCommentsLikesDBController();
           Article a = article.mongoDBshowArticle(homePage.getTitolo(),homePage.getAuthor());

           Text titolo = (Text) App.getScene().lookup("#titolo");
           TextArea body = (TextArea) App.getScene().lookup("#articlebody");
           Text author = (Text) App.getScene().lookup("#author");
           Text like = (Text) App.getScene().lookup("#numberlike");
           Text unlike = (Text) App.getScene().lookup("#numberunlike");
           Text date = (Text) App.getScene().lookup("#data");

           date.setText(homePage.getTimestamp());
           author.setText(homePage.getAuthor());
           titolo.setText(homePage.getTitolo());
           like.setText(String.valueOf(article.neo4jCountLikes(homePage.getTitolo(),homePage.getAuthor(),"like")));
           unlike.setText(String.valueOf(article.neo4jCountLikes(homePage.getTitolo(),homePage.getAuthor(),"dislike")));
           System.out.println("testo" + a.getText());
           body.setText(a.getText());

           List<InfoComment> infoComments = null;
           infoComments = article.neo4jListArticlesComment(homePage.getTitolo(),homePage.getAuthor());
           System.out.println("Numero di commenti " + infoComments.size());

           for(int i = 0; i < infoComments.size() && i < 3; i++){
               TextArea commento = (TextArea) App.getScene().lookup("#comment" + (i + 1));
               commento.setText(infoComments.get(i).getText());
               TextField autore = (TextField) App.getScene().lookup("#author" + (i + 1));
               autore.setText(infoComments.get(i).getAuthor());
               TextField timestamp = (TextField) App.getScene().lookup("#timestamp" + (i + 1));
               timestamp.setText(String.valueOf(infoComments.get(i).getTimestamp()));

               if(infoComments.get(i).getAuthor().equals(login.getLoggedUser())){
                   // se sono l'autore del messaggio abilita il pulsante della cancellazione del commento
                   Button delete = (Button) App.getScene().lookup("#button" + (i + 1));
                   delete.setDisable(false);
                   delete.setVisible(true);
               }
           }

           setSuggestedArticles();
           giàCaricato = 1;
       }
    }

    private boolean isUserModerator() {
        return true;
    }

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("homePage");
    }

    @FXML
    void like() throws IOException {
        Button likebutton = (Button) App.getScene().lookup("#likebutton");
        Button dislikebutton = (Button) App.getScene().lookup("#unlikebutton");
        Text like = (Text) App.getScene().lookup("#numberlike");
        int numberOfLike = Integer.parseInt(like.getText());

        // like button disabled vuol dire che ho pigiato dislike
        if(!likebutton.isDisabled()) {
            if(likebutton.getText().equals("Like")){
                like.setText(String.valueOf(numberOfLike + 1));

                likebutton.setStyle("-fx-background-color: red");
                likebutton.setText("Remove like");
                dislikebutton.setDisable(true);

                UpdateDatabaseDBController update = new UpdateDatabaseDBController();
                InfoLike aLike = new InfoLike("like", login.getLoggedUser(), new Timestamp(System.currentTimeMillis()), homePage.getAuthor(), homePage.getTitolo());
                update.Neo4jAddLike(aLike);
            } else {
                like.setText(String.valueOf(numberOfLike - 1));

                likebutton.setStyle("-fx-background-color: green");
                likebutton.setText("Like");
                dislikebutton.setDisable(false);

                UpdateDatabaseDBController update = new UpdateDatabaseDBController();
                InfoLike aLike = new InfoLike("like", login.getLoggedUser(), new Timestamp(System.currentTimeMillis()), homePage.getAuthor(), homePage.getTitolo());
                update.Neo4jAddLike(aLike);
            }

        }
    }

    @FXML
    void unlike() throws IOException {
        Text like = (Text) App.getScene().lookup("#numberunlike");
        Button unlikebutton = (Button) App.getScene().lookup("#unlikebutton");
        Button likebutton = (Button) App.getScene().lookup("#likebutton");
        int numberOfUnlike = Integer.parseInt(like.getText());

        // unlike button disabled vuol dire che ho pigiato like
        if(!unlikebutton.isDisabled()) {
            if(unlikebutton.getText().equals("Dislike")){
                unlikebutton.setStyle("-fx-background-color: green");
                unlikebutton.setText("Remove dislike");
                likebutton.setDisable(true);
                like.setText(String.valueOf(numberOfUnlike + 1));

                UpdateDatabaseDBController update = new UpdateDatabaseDBController();
                InfoLike anUnlike = new InfoLike("dislike",login.getLoggedUser(), new Timestamp(System.currentTimeMillis()), homePage.getAuthor(),homePage.getTitolo());
                update.Neo4jAddLike(anUnlike);
            } else {
                unlikebutton.setStyle("-fx-background-color: red");
                unlikebutton.setText("Dislike");
                likebutton.setDisable(false);

                like.setText(String.valueOf(numberOfUnlike - 1));
                UpdateDatabaseDBController update = new UpdateDatabaseDBController();
                InfoLike anUnlike = new InfoLike("dislike",login.getLoggedUser(),new Timestamp(System.currentTimeMillis()), homePage.getAuthor(),homePage.getTitolo());
                update.Neo4jAddLike(anUnlike);
            }
        } else {
            logger.warning("else");
        }

    }

    @FXML
    void postComment() throws IOException {
        TextArea articlecomment = (TextArea) App.getScene().lookup("#articlecomment");
        UpdateDatabaseDBController update = new UpdateDatabaseDBController();
        InfoComment comment = new InfoComment(articlecomment.getText(),login.getLoggedUser(),new Timestamp(System.currentTimeMillis()), homePage.getAuthor(),homePage.getTitolo());
        update.Neo4jAddComment(comment);

        for( int i = 0; i < 3; i++) {
            TextArea comment_i = (TextArea) App.getScene().lookup("#comment" + (i + 1));
            if(comment_i.getText().equals("")) {
                comment_i.setText(comment.getText());

                TextField author = (TextField) App.getScene().lookup("#author" + (i + 1));
                author.setText(comment.getAuthor());

                TextField timestamp = (TextField) App.getScene().lookup("#timestamp" + (i + 1));
                timestamp.setText(String.valueOf(comment.getTimestamp()));

                Button delete1 = (Button) App.getScene().lookup("#delete" + (i + 1));
                delete1.setVisible(true);

                break;
            }
        }

        articlecomment.setText("");

    }

    @FXML
    void setSuggestedArticles() throws IOException {
        ArticlesCommentsLikesDBController home = new ArticlesCommentsLikesDBController();
        List<Article> list = home.neo4jListSuggestedArticles(login.getLoggedUser());
        Text titolo = (Text) App.getScene().lookup("#titolo");

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Article a = list.get(i);
                if(a.getTitle().equals(titolo.getText())) {

                    TitledPane articolo = (TitledPane) App.getScene().lookup("#fullarticle" + (i + 1));
                    Text author = (Text) App.getScene().lookup("#authorarticle" + (i + 1));
                    Text timestamp = (Text) App.getScene().lookup("#timestamparticle" + (i + 1));

                    articolo.setText(a.getTitle());
                    author.setText(a.getAuthor());
                    timestamp.setText(String.valueOf(a.getTimestamp()));
                }
            }
        }
    }

    @FXML
    void goToArticle (MouseEvent event) throws IOException {
        Text testo = new Text();
        if(event.getTarget().getClass()!= testo.getClass()) {
            AnchorPane articolo = (AnchorPane) event.getTarget();
            String idArticle = articolo.getId();

            Text a = (Text) App.getScene().lookup("#author" + idArticle);
            Text t = (Text) App.getScene().lookup("#timestamp" + idArticle);
            homePage.setAuthor(a.getText());
            homePage.setTimestamp(t.getText());
            TitledPane tx = (TitledPane) App.getScene().lookup("#full" + idArticle);
            homePage.setTitle(tx.getText());
            App.setRoot("article");
        }
    }

    @FXML
    void deleteComment(MouseEvent event) throws IOException {
        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();

        Button target = (Button) event.getSource();
        String id = target.getId();

        Text titolo = (Text) App.getScene().lookup("#titolo");
        Text autore = (Text) App.getScene().lookup("#author");

        String comment = "";
        String author = "";
        String timestamp = "";

        String title = titolo.getText();
        author = autore.getText();
        TextArea commentField = null;
        TextField authorField = null;
        TextField timestampField = null;

        switch (id) {
            case "delete1" -> {
                commentField = (TextArea) App.getScene().lookup("#comment1");
                authorField = (TextField) App.getScene().lookup("#author1");
                timestampField = (TextField) App.getScene().lookup("#timestamp1");
            }
            case "delete2" -> {
                commentField = (TextArea) App.getScene().lookup("#comment2");
                authorField = (TextField) App.getScene().lookup("#author2");
                timestampField = (TextField) App.getScene().lookup("#timestamp2");
            }
            case "delete3" -> {
                commentField = (TextArea) App.getScene().lookup("#comment3");
                authorField = (TextField) App.getScene().lookup("#author3");
                timestampField = (TextField) App.getScene().lookup("#timestamp3");
            }
        }

        InfoComment infocomment = new InfoComment(commentField.getText(), authorField.getText(), Timestamp.valueOf(timestampField.getText()), autore.getText(),titolo.getText());
        boolean ret = controller.Neo4jDeleteComment(infocomment);

        commentField.setText("");
        authorField.setText("");
        timestampField.setText("");
        target.setVisible(false);

        if(ret){
            logger.info("Ho cancellato il commento");
        } else {
            logger.info("Non c'erano commenti da cancellare");
        }
    }
}
