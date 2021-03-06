package it.unipi.dii.lsmdb.project.group5.view;

import com.google.common.collect.Lists;
import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.bean.InfluencerInfoBean;
import it.unipi.dii.lsmdb.project.group5.bean.UserBean;
import it.unipi.dii.lsmdb.project.group5.bean.VersatileUser;
import it.unipi.dii.lsmdb.project.group5.controller.AnalyticsDBController;
import it.unipi.dii.lsmdb.project.group5.controller.UsersPagesDBController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

/**
 * The type Statistics moderator page view.
 */
public class StatisticsModeratorPageView {

    /**
     * The Controller.
     */
    AnalyticsDBController controller = new AnalyticsDBController();

    @FXML
    Text influencer1;

    @FXML
    Text influencer2;

    @FXML
    Text influencer3;

    @FXML
    Text dislike1;

    @FXML
    Text dislike2;

    @FXML
    Text dislike3;

    @FXML
    Text versatile1;

    @FXML
    Text versatile2;

    @FXML
    Text versatile3;

    @FXML
    TextField promotion;

    @FXML
    TextField declass;

    @FXML
    Text promotetext;

    @FXML
    ImageView promotetic;

    @FXML
    Button promotebutton;

    @FXML
    Text declasstext;

    @FXML
    ImageView declasstic;

    @FXML
    Button downgradebutton;

    @FXML
    DatePicker date;

    @FXML
    DatePicker date1;

    @FXML
    Text articles1;

    @FXML
    Text articles2;

    @FXML
    Text articles3;

    @FXML
    Text game1;

    @FXML
    Text game2;

    @FXML
    Text game3;

    @FXML
    Text game4;

    @FXML
    Text game5;

    /**
     * Return to homepage.
     *
     * @throws IOException the io exception
     */
@FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

    private Text chooseInfluencer(int i){
        return switch (i) {
            case 1 -> influencer1;
            case 2 -> influencer2;
            case 3 -> influencer3;
            default -> new Text();
        };
    }

    private Text chooseDislike(int i){
        return switch (i) {
            case 1 -> dislike1;
            case 2 -> dislike2;
            case 3 -> dislike3;
            default -> new Text();
        };

    }

    private Text chooseVersatile(int i){
        return switch (i) {
            case 1 -> versatile1;
            case 2 -> versatile2;
            case 3 -> versatile3;
            default -> new Text();
        };

    }

    private Text chooseArticles(int i){
        return switch (i) {
            case 1 -> articles1;
            case 2 -> articles2;
            case 3 -> articles3;
            default -> new Text();
        };

    }

    private Text chooseGame(int i){
        return switch (i) {
            case 1 -> game1;
            case 2 -> game2;
            case 3 -> game3;
            case 4 -> game4;
            case 5 -> game5;
            default -> new Text();
        };

    }

    private static int giaCaricato = 0;

    /**
     * Initialize.
     */
@FXML
    void initialize() {

        if(giaCaricato == 0) {
            refreshPage();
            giaCaricato = 1;
        }

    }

    /**
     * Refresh page.
     */
@FXML
    void refreshPage(){
        List<InfluencerInfoBean> list = controller.getNumLikeForEachInfluencer();

        for(int i = 0; i < 3; i++){
            Text modify = chooseInfluencer(i + 1);
            if(i < list.size()) {
                modify.setText(list.get(i).getInfluencer() + " - " + list.get(i).getCount());
            } else {
                modify.setText("");
            }
        }

        List<InfluencerInfoBean> list1 = controller.getNumDisLikeForEachInfluencer();

        for(int i = 0; i < 3; i++){
            Text modify = chooseDislike(i + 1);
            if(i < list1.size()) {
                modify.setText(list1.get(i).getInfluencer() + " - " + list1.get(i).getCount());
            } else {
                modify.setText("");
            }
        }

        List<VersatileUser> listUser = controller.mostVersatileUsers("normalUser");


        for(int i = 0; i < 3; i++){
            Text modify = chooseVersatile(i + 1);
            if(i < listUser.size()) {
                modify.setText(listUser.get(i).getUsername() + " - " + listUser.get(i).getHowManyCategories());
            } else {
                modify.setText("");
            }
        }

        numberPublished();
        gameCovered();
    }

    /**
     * Number published.
     */
@FXML
    void numberPublished() {
        List<InfluencerInfoBean> lista = Lists.newArrayList();

        if(date.getValue() != null) {
            lista = controller.numberOfArticlesPublishedInASpecifiedPeriod(date.getValue().toString());
        }

        for(int i = 0; i < 3; i ++) {
            Text articolo = chooseArticles(i + 1);
            if(i < lista.size()) {
                articolo.setText(lista.get(i).getInfluencer() + ": " + lista.get(i).getCount());
            } else {
                articolo.setText("");
            }
        }
    }

    /**
     * Game covered.
     */
@FXML
    void gameCovered() {
        List<InfluencerInfoBean> lista = Lists.newArrayList();

        if(date1.getValue() != null) {
            lista= controller.distinctGamesInArticlesPublishedInASpecifiedPeriod(date1.getValue().toString());
        }

        for(int i = 0; i < 5; i ++) {
            Text gioco = chooseGame(i + 1);
            if(i < lista.size()) {
                gioco.setText(lista.get(i).getInfluencer() + ": " + lista.get(i).getCount());

            } else {
                gioco.setText("");
            }
        }
    }

    /**
     * Promote.
     */
@FXML
    void promote() {
        UsersPagesDBController controllerUser = new UsersPagesDBController();
        UserBean toBePromoted = controllerUser.showUser(promotion.getText());

        if(toBePromoted != null && toBePromoted.getUsername() != null) {
            if(controllerUser.promoteDemoteUser(toBePromoted.getUsername(), "influencer")){
                promotetext.setVisible(true);
                promotion.setText("");
                promotebutton.setDisable(true);
                promotetic.setVisible(false);
            }
        }
    }

    /**
     * Search user for promotion.
     */
@FXML
    void searchUserForPromotion() {
        UsersPagesDBController controllerUser = new UsersPagesDBController();
        UserBean toBePromoted = controllerUser.showUser(promotion.getText());

        if(toBePromoted != null && toBePromoted.getUsername() != null) {
            promotebutton.setDisable(false);
            promotetic.setVisible(true);
        } else {
            promotebutton.setDisable(true);
            promotetic.setVisible(false);
        }
    }

    /**
     * Search user for downgrade.
     */
@FXML
    void searchUserForDowngrade() {
        UsersPagesDBController controllerUser = new UsersPagesDBController();
        UserBean toBeDowngraded = controllerUser.showUser(declass.getText());

        if(toBeDowngraded != null && toBeDowngraded.getUsername() != null) {
            downgradebutton.setDisable(false);
            declasstic.setVisible(true);
        } else {
            downgradebutton.setDisable(true);
            declasstic.setVisible(false);
        }
    }

    /**
     * Downgrade.
     */
@FXML
    void downgrade() {
        UsersPagesDBController controllerUser = new UsersPagesDBController();
        UserBean toBeDowngraded = controllerUser.showUser(declass.getText());

        if(toBeDowngraded != null && toBeDowngraded.getUsername() != null) {
            if(controllerUser.promoteDemoteUser(toBeDowngraded.getUsername(), "normalUser")){
                declasstext.setVisible(true);
                declass.setText("");
                downgradebutton.setDisable(true);
                declasstic.setVisible(false);
            }
        }
    }
    
}
