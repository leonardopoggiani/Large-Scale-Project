package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.bean.InfluencerInfoBean;
import it.unipi.dii.LSMDB.project.group5.bean.VersatileUser;
import it.unipi.dii.LSMDB.project.group5.controller.AnalyticsDBController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

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
    void initialize() {
        List<InfluencerInfoBean> list = controller.getNumLikeForEachInfluencer();

        influencer1.setText(list.get(0).getInfluencer() + ": " + list.get(0).getCount());
        influencer2.setText(list.get(1).getInfluencer() + ": " + list.get(1).getCount());
        influencer3.setText(list.get(2).getInfluencer() + ": " + list.get(2).getCount());

        list = controller.getNumDisLikeForEachInfluencer();

        dislike1.setText(list.get(0).getInfluencer() + ": " + list.get(0).getCount());
        dislike2.setText(list.get(1).getInfluencer() + ": " + list.get(1).getCount());
        dislike3.setText(list.get(2).getInfluencer() + ": " + list.get(2).getCount());

        List<VersatileUser> listUser = controller.mostVersatileUsers("normalUser");

        versatile1.setText(listUser.get(0).getUsername() + ": " + listUser.get(0).getHowManyCategories());
        versatile2.setText(listUser.get(1).getUsername() + ": " + listUser.get(1).getHowManyCategories());
        versatile3.setText(listUser.get(2).getUsername() + ": " + listUser.get(2).getHowManyCategories());

    }
    
}
