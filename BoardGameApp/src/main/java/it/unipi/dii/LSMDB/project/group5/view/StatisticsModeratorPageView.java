package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.bean.InfluencerInfoBean;
import it.unipi.dii.LSMDB.project.group5.bean.UserBean;
import it.unipi.dii.LSMDB.project.group5.bean.VersatileUser;
import it.unipi.dii.LSMDB.project.group5.controller.AnalyticsDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UsersPagesDBController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class StatisticsModeratorPageView {

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

    private static int giaCaricato = 0;

    @FXML
    void initialize() {

        if(giaCaricato == 0) {
            refreshPage();
            giaCaricato = 1;
        }

    }

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


        list = controller.getNumDisLikeForEachInfluencer();

        for(int i = 0; i < 3; i++){
            Text modify = chooseDislike(i + 1);
            if(i < list.size()) {
                modify.setText(list.get(i).getInfluencer() + " - " + list.get(i).getCount());
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
    }

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

    @FXML
    void searchUserForDowngrade() {

    }

    @FXML
    void downgrade() {

    }
    
}
