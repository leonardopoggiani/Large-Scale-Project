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
    Text id1;

    @FXML
    Text id2;

    @FXML
    Text id3;

    private int articleSelected;
    private ArticleBean questo;

    @FXML
    void initialize() throws IOException, ExecutionException {
        articleSelected = HomepageArticles.getId();
        setArticleFields();
        setArticleFields();
    }

    @FXML
    void setArticleFields() throws IOException, ExecutionException {
        ArticlesPagesDBController article = new ArticlesPagesDBController();

        ArticleBean a = cache.getDataIfPresent(articleSelected);
        questo = a;

        if(a == null || a.getTitle() == null) {
           logger.log(Level.WARNING, "cache miss");
           a = article.showArticleDetails(articleSelected);
           questo = a;
        } else {
           logger.log(Level.WARNING, "cache hit");
        }

        author.setText(a.getAuthor());
        titolo.setText(a.getTitle());
        data.setText((a.getTimestamp() == null) ? new Timestamp(System.currentTimeMillis()).toString() : a.getTimestamp().toString());
        numberlike.setText(String.valueOf(article.countLikes("like", questo.getId())));
        numberunlike.setText(String.valueOf(article.countLikes("dislike", questo.getId())));
        articlebody.setText(a.getText());

        setComments();
        setSuggestedArticlesBelow();
    }

    private void setComments() {
        ArticlesPagesDBController article = new ArticlesPagesDBController();

        List<CommentBean> infoComments = null;
        infoComments = article.listArticlesComments(questo.getId(), 3);

        for(int i = 0; i < 3; i++){
            TextArea commento = chooseComment(i + 1);
            TextField autore = chooseAuthor(i + 1);
            TextField timestamp = chooseTimestamp(i + 1);

            if(i < infoComments.size()) {
                commento.setText(infoComments.get(i).getText());
                autore.setText(infoComments.get(i).getAuthor());
                timestamp.setText(String.valueOf(infoComments.get(i).getTimestamp()));

                if(infoComments.get(i).getAuthor().equals(LoginPageView.getLoggedUser())){
                    // se sono l'autore del messaggio abilita il pulsante della cancellazione del commento
                    Button delete = chooseDeleteButton(i + 1);
                    delete.setDisable(false);
                    delete.setVisible(true);
                }
            } else {
                commento.setText("");
                autore.setText("");
                timestamp.setText("");
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

    private Text chooseId(int i){
        return switch (i) {
            case 1 -> id1;
            case 2 -> id2;
            case 3 -> id3;
            default -> new Text();
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
        ArticlesPagesDBController update = new ArticlesPagesDBController();
        LikeBean aLike = new LikeBean("like", LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), questo.getAuthor(), questo.getTitle());
        int ret = update.addLike(aLike);

        // like button disabled vuol dire che ho pigiato dislike
        if(ret == 2){
            likebutton.setStyle("-fx-background-color: red");
            likebutton.setText("Remove like");
            dislikebutton.setDisable(true);
        } else if (ret == 1){
            likebutton.setStyle("-fx-background-color: green");
            likebutton.setText("Like");
            dislikebutton.setDisable(false);

        }

        like.setText(String.valueOf(update.countLikes("like", questo.getId())));


    }

    @FXML
    void unlike() throws IOException {
        Text like = (Text) App.getScene().lookup("#numberunlike");
        Button unlikebutton = (Button) App.getScene().lookup("#unlikebutton");
        Button likebutton = (Button) App.getScene().lookup("#likebutton");
        ArticlesPagesDBController update = new ArticlesPagesDBController();
        LikeBean anUnlike = new LikeBean("dislike", LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), questo.getAuthor(), questo.getTitle());
        int ret = update.addLike(anUnlike);

        // unlike button disabled vuol dire che ho pigiato like
        if(ret == 2){
            unlikebutton.setStyle("-fx-background-color: green");
            unlikebutton.setText("Remove dislike");
            likebutton.setDisable(true);
        } else {
            unlikebutton.setStyle("-fx-background-color: red");
            unlikebutton.setText("Dislike");
            likebutton.setDisable(false);
        }

        like.setText(String.valueOf(update.countLikes("dislike", questo.getId())));

    }

    @FXML
    void postComment() throws IOException {
        ArticlesPagesDBController update = new ArticlesPagesDBController();
        CommentBean comment = new CommentBean(articlecomment.getText(), LoginPageView.getLoggedUser(),new Timestamp(System.currentTimeMillis()), questo.getAuthor(), questo.getTitle());
        update.addComment(comment);
        articlecomment.setText("");
        setComments();
    }

    @FXML
    void setSuggestedArticlesBelow() throws IOException {
        ArticlesPagesDBController home = new ArticlesPagesDBController();
        List<ArticleBean> list = home.listSuggestedArticles(LoginPageView.getLoggedUser(), 6);

        list.remove(questo);

        if (list != null) {
            for( int i = 0; i < 3; i++) {
                ArticleBean a = list.get(i);
                System.out.println(a);
                TitledPane articolo = chooseArticle(i + 1);
                Text author = chooseAuthorArticle(i + 1);
                Text timestamp = chooseTimestampArticle(i + 1);
                Text identificatore = chooseId(i + 1);
                if(i < list.size()) {
                    articolo.setText(a.getTitle());
                    author.setText(a.getAuthor());
                    timestamp.setText(String.valueOf(a.getTimestamp()));
                    identificatore.setText(String.valueOf(a.getId()));
                } else {
                    articolo.setText("");
                    author.setText("");
                    timestamp.setText("");
                    identificatore.setText("");
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
    void goToArticle (MouseEvent event) throws IOException, ExecutionException {
        AnchorPane articolo = (AnchorPane) event.getSource();
        String idArticle = articolo.getId();

        int index = Integer.parseInt(idArticle.substring(idArticle.length() - 1));
        Text identificatore = chooseId(index);
        logger.info("identificatore " + identificatore);
        HomepageArticles.setId(Integer.parseInt(identificatore.getText()) );
        App.setRoot("ArticlePageView");

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
