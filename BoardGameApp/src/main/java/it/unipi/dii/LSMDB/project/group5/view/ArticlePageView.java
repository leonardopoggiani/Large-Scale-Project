package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.bean.CommentBean;
import it.unipi.dii.LSMDB.project.group5.bean.LikeBean;
import it.unipi.dii.LSMDB.project.group5.cache.ArticlesCache;
import it.unipi.dii.LSMDB.project.group5.controller.ArticlesPagesDBController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticlePageView {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    ArticlesCache cache = ArticlesCache.getInstance();

    @FXML
    Text titolo;

    @FXML
    TextArea articlebody;

    @FXML
    Text author;

    @FXML
    Text data;

    @FXML
    Text numberlike;

    @FXML
    Text numberunlike;

    @FXML
    TextArea comment1;

    @FXML
    TextArea comment2;

    @FXML
    TextArea comment3;

    @FXML
    TextField author1;

    @FXML
    TextField author2;

    @FXML
    TextField author3;

    @FXML
    TextField timestamp1;

    @FXML
    TextField timestamp2;

    @FXML
    TextField timestamp3;

    @FXML
    Button delete1;

    @FXML
    Button delete2;

    @FXML
    Button delete3;

    @FXML
    TitledPane fullarticle1;

    @FXML
    TitledPane fullarticle2;

    @FXML
    TitledPane fullarticle3;

    @FXML
    Text authorarticle1;

    @FXML
    Text authorarticle2;

    @FXML
    Text authorarticle3;

    @FXML
    Text timestamparticle1;

    @FXML
    Text timestamparticle2;

    @FXML
    Text timestamparticle3;

    @FXML
    TextArea articlecomment;

    @FXML
    void initialize() throws IOException, ExecutionException {
        setArticleFields();
    }

    @FXML
    void setArticleFields() throws IOException, ExecutionException {
        ArticlesPagesDBController article = new ArticlesPagesDBController();

        cache.setAuthor(HomepageArticles.getAuthor());
        ArticleBean a = cache.getDataIfPresent(HomepageArticles.getTitolo());

        if(a == null || a.getTitle() == null) {
           logger.log(Level.WARNING, "cache miss");
           a = article.showArticleDetails(HomepageArticles.getTitolo(), HomepageArticles.getAuthor());
        } else {
           logger.log(Level.WARNING, "cache hit");
        }

        author.setText(HomepageArticles.getAuthor());
        titolo.setText(HomepageArticles.getTitolo());
        data.setText(HomepageArticles.getTimestamp());
        numberlike.setText(String.valueOf(article.countLikes(HomepageArticles.getTitolo(), HomepageArticles.getAuthor(),"like")));
        numberunlike.setText(String.valueOf(article.countLikes(HomepageArticles.getTitolo(), HomepageArticles.getAuthor(),"dislike")));
        articlebody.setText(a.getText());

        setComments();
        setSuggestedArticlesBelow();
    }

    private void setComments() {
        ArticlesPagesDBController article = new ArticlesPagesDBController();

        List<CommentBean> infoComments = null;
        infoComments = article.listArticlesComments(HomepageArticles.getTitolo(), HomepageArticles.getAuthor(), 3);
        System.out.println("Numero di commenti " + infoComments.size() + ", autore:" + HomepageArticles.getAuthor() + ", titolo: " + HomepageArticles.getTitolo());

        for(int i = 0; i < infoComments.size() && i < 3; i++){
            TextArea commento = chooseComment(i + 1);
            commento.setText(infoComments.get(i).getText());
            TextField autore = chooseAuthor(i + 1);
            autore.setText(infoComments.get(i).getAuthor());
            TextField timestamp = chooseTimestamp(i + 1);
            timestamp.setText(String.valueOf(infoComments.get(i).getTimestamp()));

            if(infoComments.get(i).getAuthor().equals(LoginPageView.getLoggedUser())){
                // se sono l'autore del messaggio abilita il pulsante della cancellazione del commento
                Button delete = chooseDeleteButton(i + 1);
                delete.setDisable(false);
                delete.setVisible(true);
            }
        }
    }

    private TextArea chooseComment(int i){
        return switch (i) {
            case 1 -> comment1;
            case 2 -> comment2;
            case 3 -> comment3;
            default -> new TextArea();
        };
    }

    private TextField chooseAuthor(int i){
        return switch (i) {
            case 1 -> author1;
            case 2 -> author2;
            case 3 -> author3;
            default -> new TextField();
        };
    }

    private TextField chooseTimestamp(int i){
        return switch (i) {
            case 1 -> timestamp1;
            case 2 -> timestamp2;
            case 3 -> timestamp3;
            default -> new TextField();
        };
    }

    private Button chooseDeleteButton(int i){
        return switch (i) {
            case 1 -> delete1;
            case 2 -> delete2;
            case 3 -> delete3;
            default -> new Button();
        };
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
        ArticlesPagesDBController update = new ArticlesPagesDBController();

        // like button disabled vuol dire che ho pigiato dislike
        if(!likebutton.isDisabled()) {
            if(likebutton.getText().equals("Like")){
                like.setText(String.valueOf(numberOfLike + 1));

                likebutton.setStyle("-fx-background-color: red");
                likebutton.setText("Remove like");
                dislikebutton.setDisable(true);
                LikeBean aLike = new LikeBean("like", LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), HomepageArticles.getAuthor(), HomepageArticles.getTitolo());
                update.addLike(aLike);
            } else {
                like.setText(String.valueOf(numberOfLike - 1));

                likebutton.setStyle("-fx-background-color: green");
                likebutton.setText("Like");
                dislikebutton.setDisable(false);
                LikeBean aLike = new LikeBean("like", LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), HomepageArticles.getAuthor(), HomepageArticles.getTitolo());
                update.addLike(aLike);
            }

        }
    }

    @FXML
    void unlike() throws IOException {
        Text like = (Text) App.getScene().lookup("#numberunlike");
        Button unlikebutton = (Button) App.getScene().lookup("#unlikebutton");
        Button likebutton = (Button) App.getScene().lookup("#likebutton");
        int numberOfUnlike = Integer.parseInt(like.getText());
        ArticlesPagesDBController update = new ArticlesPagesDBController();

        // unlike button disabled vuol dire che ho pigiato like
        if(!unlikebutton.isDisabled()) {
            if(unlikebutton.getText().equals("Dislike")){
                unlikebutton.setStyle("-fx-background-color: green");
                unlikebutton.setText("Remove dislike");
                likebutton.setDisable(true);
                like.setText(String.valueOf(numberOfUnlike + 1));
                LikeBean anUnlike = new LikeBean("dislike", LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), HomepageArticles.getAuthor(), HomepageArticles.getTitolo());
                update.addLike(anUnlike);
            } else {
                unlikebutton.setStyle("-fx-background-color: red");
                unlikebutton.setText("Dislike");
                likebutton.setDisable(false);

                like.setText(String.valueOf(numberOfUnlike - 1));
                LikeBean anUnlike = new LikeBean("dislike", LoginPageView.getLoggedUser(),new Timestamp(System.currentTimeMillis()), HomepageArticles.getAuthor(), HomepageArticles.getTitolo());
                update.addLike(anUnlike);
            }
        }
    }

    @FXML
    void postComment() throws IOException {
        ArticlesPagesDBController update = new ArticlesPagesDBController();
        CommentBean comment = new CommentBean(articlecomment.getText(), LoginPageView.getLoggedUser(),new Timestamp(System.currentTimeMillis()), HomepageArticles.getAuthor(), HomepageArticles.getTitolo());
        update.addComment(comment);
        articlecomment.setText("");
        setComments();
    }

    @FXML
    void setSuggestedArticlesBelow() throws IOException {
        ArticlesPagesDBController home = new ArticlesPagesDBController();
        List<ArticleBean> list = home.listSuggestedArticles(LoginPageView.getLoggedUser(), 10);

        if (list != null) {
            for (ArticleBean a : list) {
                if (!a.getTitle().equals(titolo.getText())) {
                    for (int j = 0; j < list.size() && j < 3; j++) {

                        TitledPane articolo = chooseArticle(j + 1);
                        Text author = chooseAuthorArticle(j + 1);
                        Text timestamp = chooseTimestampArticle(j + 1);

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

    private TitledPane chooseArticle(int j) {
        return switch (j) {
            case 1 -> fullarticle1;
            case 2 -> fullarticle2;
            case 3 -> fullarticle3;
            default -> new TitledPane();
        };
    }

    private Text chooseAuthorArticle(int i) {
        return switch (i) {
            case 1 -> authorarticle1;
            case 2 -> authorarticle2;
            case 3 -> authorarticle3;
            default -> new Text();
        };
    }

    private Text chooseTimestampArticle(int i) {
        return switch (i) {
            case 1 -> timestamparticle1;
            case 2 -> timestamparticle2;
            case 3 -> timestamparticle3;
            default -> new Text();
        };
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
        ArticlesPagesDBController controller = new ArticlesPagesDBController();

        Button target = (Button) event.getSource();
        String id = target.getId();

        int index = Integer.parseInt(id.substring(id.length() - 1));

        TextArea commentField = chooseComment(index);
        TextField authorField = chooseAuthor(index);
        TextField timestampField = chooseTimestamp(index);

        CommentBean infocomment = new CommentBean(commentField.getText(), authorField.getText(), Timestamp.valueOf(timestampField.getText()), author.getText(),titolo.getText());
        boolean ret = controller.deleteComment(infocomment);

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
