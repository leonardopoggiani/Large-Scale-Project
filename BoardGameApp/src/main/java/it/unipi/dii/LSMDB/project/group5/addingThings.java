package it.unipi.dii.LSMDB.project.group5;

import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.bean.GroupBean;
import it.unipi.dii.LSMDB.project.group5.bean.PostBean;
import it.unipi.dii.LSMDB.project.group5.controller.ArticlesPagesDBController;
import it.unipi.dii.LSMDB.project.group5.controller.GamesPagesDBController;
import it.unipi.dii.LSMDB.project.group5.controller.GroupsPagesDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UsersPagesDBController;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.MongoDBManager;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.Neo4jDBManager;
import it.unipi.dii.LSMDB.project.group5.view.LoginPageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class addingThings {
    public static void main(String[] args) throws Exception {
        if(MongoDBManager.createConnection() && Neo4jDBManager.InitializeDriver()){
            Random rand = new Random();
            ObservableList<String> titoli = FXCollections.observableArrayList("Articolo", "Articolone", "Che articolo", "Che idea ma quale idea", "Articolino", "Scontro tra titani","Confrontro tra i due", "Che ne pensate?", "Sapete che..", "Titolo", "Abbiamo provato il gioco", "");
            ObservableList<String> autori = FXCollections.observableArrayList("chakado", "microcline", "leonardo", "francesca", "kd5dgh", "jtong77", "notanseladams","t_s_sullivan", "okosp", "xoff");
            ObservableList<String> categorie = FXCollections.observableArrayList("Card Game:1002","Humor:1079","Party Game:1030","Educational:1094","Medical:2145","Animals:1089","Racing:1031");
            ObservableList<String> text = FXCollections.observableArrayList("Ah incredibile", "Ah davvero", "Post", "Sono ioo", "Ci siete?", "Che fate?", "Tutto bene?");
            ObservableList<String> nomi = FXCollections.observableArrayList("Bel gruppo", "Gli sfollati", "Quelli di sempre", "Pisammare", "Cimone 2k16", "Quelli del quartiere");
            ObservableList<String> giochi = FXCollections.observableArrayList("Muse: Awakenings", "Streams", "Bowl Bound", "Kribbeln", "Santa Cruz 1797", "Qwixx: Das Duell");

            ArticlesPagesDBController controller = new ArticlesPagesDBController();
            GamesPagesDBController controller1 = new GamesPagesDBController();
            UsersPagesDBController controller2 = new UsersPagesDBController();
            GroupsPagesDBController controller3 = new GroupsPagesDBController();

            for(int i = 0; i < 300; i++){

                ArticleBean a = new ArticleBean(
                    titoli.get(rand.nextInt(titoli.size())),
                    autori.get(rand.nextInt(autori.size())),
                    new Timestamp(System.currentTimeMillis()),
                    giochi.get(rand.nextInt(giochi.size())),
                    "");
                a.setText(text.get(rand.nextInt(text.size())));
                if(controller.addArticle(a)){
                    System.out.println("ok articolo");
                }
                sleep(2000);
            }

            for(int i = 0; i < 100; i++){
                if(controller2.addRemoveFollow(autori.get(rand.nextInt(6)), autori.get(rand.nextInt(6)),"add")){
                    System.out.println("ok");
                }
            }

            for(int i = 0; i < 100; i++){
                if(controller3.addGroup(new GroupBean((nomi.get(rand.nextInt(5))),new Timestamp(System.currentTimeMillis()), autori.get(rand.nextInt(7)),"no description",giochi.get(rand.nextInt(5))))){
                    System.out.println("ok");
                }
            }

            for(int i = 0; i < 200; i++){
                String member = autori.get(rand.nextInt(6));
                List<GroupBean> gruppi = controller3.showUsersGroups("leonardo","admin");
                if (controller3.addDeleteGroupMember(member, gruppi.get(rand.nextInt(gruppi.size())).getName(), "leonardo","add" )) {
                    System.out.println("ok");
                }
            }

            for(int i = 0; i < 300; i++){
                String author = autori.get(rand.nextInt(6));
                String testo = text.get(rand.nextInt(7));
                List<GroupBean> gruppi = controller3.showUsersGroups(author,"member");
                System.out.println("gruppi " + gruppi.size());

                if(gruppi.size() != 0) {
                    GroupBean gruppoScelto = gruppi.get(rand.nextInt(gruppi.size()));
                    PostBean p = new PostBean(author, testo, (new Timestamp(System.currentTimeMillis())).toString(), gruppoScelto.getName(), gruppoScelto.getAdmin());
                    if (controller3.addDeletePost(p, "add")) {
                        System.out.println("ok");
                    }
                }


            }
        }

        MongoDBManager.close();
        Neo4jDBManager.close();
    }
}
