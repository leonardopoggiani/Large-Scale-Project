package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.bean.InfluencerInfoBean;
import it.unipi.dii.LSMDB.project.group5.bean.VersatileUser;
import it.unipi.dii.LSMDB.project.group5.controller.AnalyticsDBController;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
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

    @FXML
    void initialize() {
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

    }
    
}
