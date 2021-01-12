package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.bean.CommentBean;
import it.unipi.dii.LSMDB.project.group5.bean.LikeBean;
import it.unipi.dii.LSMDB.project.group5.cache.ArticlesCache;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.controller.ArticlesCommentsLikesDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UpdateDatabaseDBController;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticlePageView {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    int giàCaricato = -1;
    ArticlesCache cache = ArticlesCache.getInstance();

    @FXML
    void setArticleFields() throws IOException, ExecutionException {
        ArticlesCommentsLikesDBController article = new ArticlesCommentsLikesDBController();

       if(giàCaricato == -1) {

           cache.setAuthor(HomepageArticles.getAuthor());
           ArticleBean a = cache.getDataIfPresent(HomepageArticles.getTitolo());

           if(a == null || a.getTitle() == null) {
               logger.log(Level.WARNING, "Recupero da db");
               a = article.mongoDBshowArticle(HomepageArticles.getTitolo(), HomepageArticles.getAuthor());
           }

           Text titolo = (Text) App.getScene().lookup("#titolo");
           TextArea body = (TextArea) App.getScene().lookup("#articlebody");
           Text author = (Text) App.getScene().lookup("#autorearticolo");
           Text like = (Text) App.getScene().lookup("#numberlike");
           Text unlike = (Text) App.getScene().lookup("#numberunlike");

           author.setText(HomepageArticles.getAuthor());
           titolo.setText(HomepageArticles.getTitolo());
           like.setText(String.valueOf(article.neo4jCountLikes(HomepageArticles.getTitolo(), HomepageArticles.getAuthor(),"like")));
           unlike.setText(String.valueOf(article.neo4jCountLikes(HomepageArticles.getTitolo(), HomepageArticles.getAuthor(),"dislike")));
           System.out.println("testo" + a.getText());

           body.setText(a.getText());

           setComments();
           setSuggestedArticlesBelow();

           giàCaricato = 1;
       }
    }

    private void setComments() {
        ArticlesCommentsLikesDBController article = new ArticlesCommentsLikesDBController();

        List<CommentBean> infoComments = null;
        infoComments = article.neo4jListArticlesComment(HomepageArticles.getTitolo(), HomepageArticles.getAuthor());
        System.out.println("Numero di commenti " + infoComments.size() + ", autore:" + HomepageArticles.getAuthor() + ", titolo: " + HomepageArticles.getTitolo());

        for(int i = 0; i < infoComments.size() && i < 3; i++){
            TextArea commento = (TextArea) App.getScene().lookup("#comment" + (i + 1));
            commento.setText(infoComments.get(i).getText());
            TextField autore = (TextField) App.getScene().lookup("#author" + (i + 1));
            autore.setText(infoComments.get(i).getAuthor());
            TextField timestamp = (TextField) App.getScene().lookup("#timestamp" + (i + 1));
            timestamp.setText(String.valueOf(infoComments.get(i).getTimestamp()));

            if(infoComments.get(i).getAuthor().equals(LoginPageView.getLoggedUser())){
                // se sono l'autore del messaggio abilita il pulsante della cancellazione del commento
                Button delete = (Button) App.getScene().lookup("#delete" + (i + 1));
                delete.setDisable(false);
                delete.setVisible(true);
            }
        }
    }

    private boolean isUserModerator() {
        return true;
    }

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
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
                LikeBean aLike = new LikeBean("like", LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), HomepageArticles.getAuthor(), HomepageArticles.getTitolo());
                update.Neo4jAddLike(aLike);
            } else {
                like.setText(String.valueOf(numberOfLike - 1));

                likebutton.setStyle("-fx-background-color: green");
                likebutton.setText("Like");
                dislikebutton.setDisable(false);

                UpdateDatabaseDBController update = new UpdateDatabaseDBController();
                LikeBean aLike = new LikeBean("like", LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), HomepageArticles.getAuthor(), HomepageArticles.getTitolo());
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
                LikeBean anUnlike = new LikeBean("dislike", LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), HomepageArticles.getAuthor(), HomepageArticles.getTitolo());
                update.Neo4jAddLike(anUnlike);
            } else {
                unlikebutton.setStyle("-fx-background-color: red");
                unlikebutton.setText("Dislike");
                likebutton.setDisable(false);

                like.setText(String.valueOf(numberOfUnlike - 1));
                UpdateDatabaseDBController update = new UpdateDatabaseDBController();
                LikeBean anUnlike = new LikeBean("dislike", LoginPageView.getLoggedUser(),new Timestamp(System.currentTimeMillis()), HomepageArticles.getAuthor(), HomepageArticles.getTitolo());
                update.Neo4jAddLike(anUnlike);
            }
        }
    }

    @FXML
    void postComment() throws IOException {
        TextArea articlecomment = (TextArea) App.getScene().lookup("#articlecomment");
        UpdateDatabaseDBController update = new UpdateDatabaseDBController();
        CommentBean comment = new CommentBean(articlecomment.getText(), LoginPageView.getLoggedUser(),new Timestamp(System.currentTimeMillis()), HomepageArticles.getAuthor(), HomepageArticles.getTitolo());
        update.Neo4jAddComment(comment);

        for( int i = 0; i < 3; i++) {
            TextArea commentoI = (TextArea) App.getScene().lookup("#comment" + (i + 1));
            if(commentoI.getText().equals("")) {
                commentoI.setText(comment.getText());

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
        setComments();

    }

    @FXML
    void setSuggestedArticlesBelow() throws IOException {
        ArticlesCommentsLikesDBController home = new ArticlesCommentsLikesDBController();
        List<ArticleBean> list = home.neo4jListSuggestedArticles(LoginPageView.getLoggedUser());
        Text titolo = (Text) App.getScene().lookup("#titolo");

        if (list != null) {
            for (ArticleBean a : list) {
                if (!a.getTitle().equals(titolo.getText())) {
                    for (int j = 0; j < list.size() && j < 3; j++) {

                        TitledPane articolo = (TitledPane) App.getScene().lookup("#fullarticle" + (j + 1));
                        Text author = (Text) App.getScene().lookup("#authorarticle" + (j + 1));
                        Text timestamp = (Text) App.getScene().lookup("#timestamparticle" + (j + 1));

                        if (articolo.getText().equals("article" + (j + 1))) {
                            articolo.setText(a.getTitle());
                            author.setText(a.getAuthor());
                            timestamp.setText(String.valueOf(a.getTimestamp()));
                            break;
                        }
                    }
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
            HomepageArticles.setAuthor(a.getText());
            HomepageArticles.setTimestamp(t.getText());
            TitledPane tx = (TitledPane) App.getScene().lookup("#full" + idArticle);
            HomepageArticles.setTitle(tx.getText());
            App.setRoot("ArticlePageView");
        }
    }

    @FXML
    void deleteComment(MouseEvent event) throws IOException {
        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();

        Button target = (Button) event.getSource();
        String id = target.getId();

        Text titolo = (Text) App.getScene().lookup("#titolo");
        Text autore = (Text) App.getScene().lookup("#autorearticolo");

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

        CommentBean infocomment = new CommentBean(commentField.getText(), authorField.getText(), Timestamp.valueOf(timestampField.getText()), autore.getText(),titolo.getText());
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

        setComments();
    }
}
