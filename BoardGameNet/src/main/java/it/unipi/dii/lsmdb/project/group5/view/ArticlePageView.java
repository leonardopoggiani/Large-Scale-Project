package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.bean.ArticleBean;
import it.unipi.dii.lsmdb.project.group5.bean.CommentBean;
import it.unipi.dii.lsmdb.project.group5.bean.LikeBean;
import it.unipi.dii.lsmdb.project.group5.cache.ArticlesCache;
import it.unipi.dii.lsmdb.project.group5.controller.ArticlesPagesDBController;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * The type Article page view.
 * @author leonardopoggiani  Gestore della pagina degli articoli.
 */
public class ArticlePageView {

    /**
     * The Cache.
     */
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

    @FXML
    Button deletearticle;

    private int articleSelected;
    private ArticleBean showed;

    /**
     * choose the textArea of the comment
     */
    private TextArea chooseComment(int i){
        return switch (i) {
            case 1 -> comment1;
            case 2 -> comment2;
            case 3 -> comment3;
            default -> new TextArea();
        };
    }

    /**
     * choose the textField of the author
     */
    private TextField chooseAuthor(int i){
        return switch (i) {
            case 1 -> author1;
            case 2 -> author2;
            case 3 -> author3;
            default -> new TextField();
        };
    }

    /**
     * choose the textField of the timestamp
     */
    private TextField chooseTimestamp(int i){
        return switch (i) {
            case 1 -> timestamp1;
            case 2 -> timestamp2;
            case 3 -> timestamp3;
            default -> new TextField();
        };
    }

    /**
     * choose the right delete button
     */
    private Button chooseDeleteButton(int i){
        return switch (i) {
            case 1 -> delete1;
            case 2 -> delete2;
            case 3 -> delete3;
            default -> new Button();
        };
    }

    /**
     * choose right id
     */
    private Text chooseId(int i){
        return switch (i) {
            case 1 -> id1;
            case 2 -> id2;
            case 3 -> id3;
            default -> new Text();
        };
    }

    /**
     * choose right article
     */
    private TitledPane chooseArticle(int j) {
        return switch (j) {
            case 1 -> fullarticle1;
            case 2 -> fullarticle2;
            case 3 -> fullarticle3;
            default -> new TitledPane();
        };
    }

    /**
     * choose right author of article
     */
    private Text chooseAuthorArticle(int i) {
        return switch (i) {
            case 1 -> authorarticle1;
            case 2 -> authorarticle2;
            case 3 -> authorarticle3;
            default -> new Text();
        };
    }

    /**
     * choose right timestamp of article
     */
    private Text chooseTimestampArticle(int i) {
        return switch (i) {
            case 1 -> timestamparticle1;
            case 2 -> timestamparticle2;
            case 3 -> timestamparticle3;
            default -> new Text();
        };
    }

    /**
     * Initialize.
     * Only moderator and the author of articles can delete the showed articles.
     * @throws IOException the io exception
     * @throws ExecutionException the execution exception
     */
    @FXML
    void initialize() throws IOException, ExecutionException {
        articleSelected = HomepageArticles.getId();
        setArticleFields();
        setArticleFields();

        if(LoginPageView.getLoggedRole().equals("moderator") || author.getText().equals(LoginPageView.getLoggedUser())){
            deletearticle.setDisable(false);
        } else {
            deletearticle.setDisable(true);
        }
    }

    /**
     * Sets article fields.
     *
     * @throws IOException the io exception
     * @throws ExecutionException the execution exception
     */
    @FXML
    void setArticleFields() throws ExecutionException {
        ArticlesPagesDBController article = new ArticlesPagesDBController();

        ArticleBean a = cache.getDataIfPresent(articleSelected);
        showed = a;

        if(a == null || a.getTitle() == null) {
           Logger.log("cache miss");
           a = article.showArticleDetails(articleSelected);
           showed = a;
        } else {
           Logger.log("cache hit");
        }

        author.setText(a.getAuthor());
        titolo.setText(a.getTitle());
        data.setText((a.getTimestamp() == null) ? new Timestamp(System.currentTimeMillis()).toString() : a.getTimestamp().toString());
        numberlike.setText(String.valueOf(article.countLikes("like", showed.getId())));
        numberunlike.setText(String.valueOf(article.countLikes("dislike", showed.getId())));
        articlebody.setText(a.getText());

        setComments();
        setSuggestedArticlesBelow();
    }

    /**
     * Sets comments below article.
     * Only the author of comments can delete it.
     */
    private void setComments() {
        ArticlesPagesDBController article = new ArticlesPagesDBController();

        List<CommentBean> infoComments = null;
        infoComments = article.listArticlesComments(showed.getId(), 3);

        for(int i = 0; i < 3; i++){
            TextArea commento = chooseComment(i + 1);
            TextField autore = chooseAuthor(i + 1);
            TextField timestamp = chooseTimestamp(i + 1);

            if(i < infoComments.size()) {
                commento.setText(infoComments.get(i).getText());
                autore.setText(infoComments.get(i).getAuthor());
                timestamp.setText(String.valueOf(infoComments.get(i).getTimestamp()));

                if(infoComments.get(i).getAuthor().equals(LoginPageView.getLoggedUser())){
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

    /**
     * Return to homepage.
     *
     * @throws IOException the io exception
     */
    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

    /**
     * Give a like.
     *
     * @throws ExecutionException the execution exception
     */
    @FXML
    void like() throws ExecutionException {
        Button likebutton = (Button) App.getScene().lookup("#likebutton");
        Button dislikebutton = (Button) App.getScene().lookup("#unlikebutton");
        Text like = (Text) App.getScene().lookup("#numberlike");
        ArticlesPagesDBController update = new ArticlesPagesDBController();
        LikeBean aLike = new LikeBean("like", LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), showed.getId());
        int ret = update.addLike(aLike);

        if(ret == 2){
            likebutton.setStyle("-fx-background-color: red");
            likebutton.setText("Remove like");
            dislikebutton.setDisable(true);
            cache.addNumLike(showed.getId());
        } else if (ret == 1){
            likebutton.setStyle("-fx-background-color: green");
            likebutton.setText("Like");
            dislikebutton.setDisable(false);
            cache.dimNumLike(showed.getId());
        }

        like.setText(String.valueOf(update.countLikes("like", showed.getId())));
    }

    /**
     * Unlike.
     */
    @FXML
    void unlike() throws ExecutionException {
        Text like = (Text) App.getScene().lookup("#numberunlike");
        Button unlikebutton = (Button) App.getScene().lookup("#unlikebutton");
        Button likebutton = (Button) App.getScene().lookup("#likebutton");
        ArticlesPagesDBController update = new ArticlesPagesDBController();
        LikeBean anUnlike = new LikeBean("dislike", LoginPageView.getLoggedUser(), new Timestamp(System.currentTimeMillis()), showed.getId());
        int ret = update.addLike(anUnlike);

        if(ret == 2){
            unlikebutton.setStyle("-fx-background-color: green");
            unlikebutton.setText("Remove dislike");
            likebutton.setDisable(true);
            cache.addNumUnlike(showed.getId());
        } else {
            unlikebutton.setStyle("-fx-background-color: red");
            unlikebutton.setText("Dislike");
            likebutton.setDisable(false);
            cache.dimNumUnlike(showed.getId());
        }

        like.setText(String.valueOf(update.countLikes("dislike", showed.getId())));
    }

    /**
     * Post comment.
     */
    @FXML
    void postComment() throws ExecutionException {
        ArticlesPagesDBController update = new ArticlesPagesDBController();
        CommentBean comment = new CommentBean(articlecomment.getText(), LoginPageView.getLoggedUser(),new Timestamp(System.currentTimeMillis()), showed.getId());
        if(update.addComment(comment)){
            articlecomment.setText("");
            cache.addNumComment(showed.getId());
        }

        setComments();
    }

    /**
     * Sets suggested articles below.
     */
    @FXML
    void setSuggestedArticlesBelow() {
        ArticlesPagesDBController home = new ArticlesPagesDBController();
        List<ArticleBean> list = home.listSuggestedArticles(LoginPageView.getLoggedUser(), 6);

        list.remove(showed);

        if (list != null) {
            for( int i = 0; i < 3; i++) {
                ArticleBean a = list.get(i);
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

    /**
     * Go to article.
     *
     * @param event the event
     * @throws IOException the io exception
     * @throws ExecutionException the execution exception
     */
    @FXML
    void goToArticle (MouseEvent event) throws IOException{
        AnchorPane articolo = (AnchorPane) event.getSource();
        String idArticle = articolo.getId();

        int index = Integer.parseInt(idArticle.substring(idArticle.length() - 1));
        Text identificatore = chooseId(index);
        HomepageArticles.setId(Integer.parseInt(identificatore.getText()) );
        App.setRoot("ArticlePageView");

    }

    /**
     * Delete comment.
     *
     * @param event the event
     * @throws IOException the io exception
     */
    @FXML
    void deleteComment(MouseEvent event) throws ExecutionException {
        ArticlesPagesDBController controller = new ArticlesPagesDBController();

        Button target = (Button) event.getSource();
        String id = target.getId();

        int index = Integer.parseInt(id.substring(id.length() - 1));

        TextArea commentField = chooseComment(index);
        TextField authorField = chooseAuthor(index);
        TextField timestampField = chooseTimestamp(index);

        CommentBean infocomment = new CommentBean(commentField.getText(), authorField.getText(), Timestamp.valueOf(timestampField.getText()), showed.getId());
        boolean ret = controller.deleteComment(infocomment);

        commentField.setText("");
        authorField.setText("");
        timestampField.setText("");

        target.setVisible(false);

        if(ret){
            cache.dimNumComment(showed.getId());
            Logger.log("comment deleted");
        } else {
            Logger.log( "no comments to delete");
        }

        setComments();
    }

    /**
     * Delete article.
     *
     * @throws IOException the io exception
     */
    @FXML
    void deleteArticle() throws IOException {
        ArticlesPagesDBController controller = new ArticlesPagesDBController();
        if(controller.deleteArticle(showed.getId()))
        {
            Logger.log("article deleted " + showed.getId());
            App.setRoot("HomepageArticles");
        } else {
            Logger.error("article not deleted correctly " + showed.getId());
        }
    }
}
