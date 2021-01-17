package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.bean.InfluencerInfoBean;
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
    void initialize() {
        List<InfluencerInfoBean> list = controller.getNumLikeForEachInfluencer();

        influencer1.setText(list.get(0).getInfluencer() + ": " + list.get(0).getCount());
        influencer2.setText(list.get(1).getInfluencer() + ": " + list.get(1).getCount());
        influencer3.setText(list.get(2).getInfluencer() + ": " + list.get(2).getCount());

    }
    
}
