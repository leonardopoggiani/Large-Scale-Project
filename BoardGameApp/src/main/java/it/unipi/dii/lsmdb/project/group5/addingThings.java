package it.unipi.dii.lsmdb.project.group5;

import it.unipi.dii.lsmdb.project.group5.bean.*;
import it.unipi.dii.lsmdb.project.group5.controller.ArticlesPagesDBController;
import it.unipi.dii.lsmdb.project.group5.controller.GamesPagesDBController;
import it.unipi.dii.lsmdb.project.group5.controller.GroupsPagesDBController;
import it.unipi.dii.lsmdb.project.group5.controller.UsersPagesDBController;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.MongoDBManager;
import it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.Neo4jDBManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

public class addingThings {

    public static void main(String[] args) throws Exception {

        if(MongoDBManager.createConnection() && Neo4jDBManager.InitializeDriver()){
            Random rand = new Random();
            ObservableList<String> titoli = FXCollections.observableArrayList("Articolo", "Articolone", "Che articolo", "Che idea ma quale idea", "Articolino", "Scontro tra titani","Confrontro tra i due", "Che ne pensate?", "Sapete che..", "Titolo", "Abbiamo provato il gioco", "");
            ObservableList<String> autori = FXCollections.observableArrayList("chakado", "microcline", "Leonardo1", "Clarissa1", "kd5dgh", "jtong77", "notanseladams","t_s_sullivan", "okosp", "xoff");
            ObservableList<String> categorie = FXCollections.observableArrayList("Card Game:1002","Humor:1079","Party Game:1030","Educational:1094","Medical:2145","Animals:1089","Racing:1031");
            ObservableList<String> text = FXCollections.observableArrayList("Ah incredibile", "Ah davvero", "Post", "Sono ioo", "Ci siete?", "Che fate?", "Tutto bene?");
            ObservableList<String> nomi = FXCollections.observableArrayList("Bel gruppo", "Gli sfollati", "Quelli di sempre", "Pisammare", "Cimone 2k16", "Quelli del quartiere");
            ObservableList<String> giochi = FXCollections.observableArrayList("Muse: Awakenings", "Streams", "Bowl Bound", "Kribbeln", "Santa Cruz 1797", "Qwixx: Das Duell");

            ArticlesPagesDBController controller = new ArticlesPagesDBController();
            GamesPagesDBController controller1 = new GamesPagesDBController();
            UsersPagesDBController controller2 = new UsersPagesDBController();
            GroupsPagesDBController controller3 = new GroupsPagesDBController();

            List<GameBean> listaGiochi = controller1.showAllGames();
            List<UserBean> listaUtenti = controller2.showAllUsers();

            for(int i = 0; i < 50; i++) {
                String user = listaUtenti.get(rand.nextInt(listaUtenti.size())).getUsername();
                if(user != null) {
                    controller2.promoteDemoteUser(user, "influencer");
                }
            }

            for(int i = 0; i < 20; i++) {
                String user = listaUtenti.get(rand.nextInt(listaUtenti.size())).getUsername();
                if (user != null) {
                    controller2.promoteDemoteUser(user, "moderator");
                }
            }

            for(int i = 0; i < 5; i++) {
                String user = listaUtenti.get(rand.nextInt(listaUtenti.size())).getUsername();
                if(user != null) {
                    controller2.promoteDemoteUser(user, "admin");
                }
            }

            List<UserBean> listaInfluencer = controller2.showAllInfluencer();

            for (int i = 0; i < 300; i++) {
                String titolo = titoli.get(rand.nextInt(titoli.size()));
                String influencer = listaInfluencer.get(rand.nextInt(listaInfluencer.size())).getUsername();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String gioco1 = listaGiochi.get(rand.nextInt(listaGiochi.size())).getName();
                String gioco2 = listaGiochi.get(rand.nextInt(listaGiochi.size())).getName();
                String testo = text.get(rand.nextInt(text.size()));

                if (titolo != null
                    && influencer != null
                    && timestamp != null
                    && gioco1 != null
                    && gioco2 != null)
                {

                    ArticleBean a = new ArticleBean(titolo, influencer, timestamp, gioco1, gioco2);
                    a.setText(testo);

                    if (controller.addArticle(a)) {
                        System.out.println("ok articolo");
                    }

                    for (int j = 0; j < 100; j++) {
                        testo = text.get(rand.nextInt(text.size()));
                        String user = listaUtenti.get(rand.nextInt(listaUtenti.size())).getUsername();
                        if(user != null) {
                            controller.addComment(
                                new CommentBean( testo, user, new Timestamp(System.currentTimeMillis()), a.getId()));
                        }
                    }

                    int likeLimit = rand.nextInt(100) + 1;

                    for (int j = 0; j < likeLimit; j++) {
                        String user = listaUtenti.get(rand.nextInt(listaUtenti.size())).getUsername();
                        if(user != null)
                            controller.addLike(
                                new LikeBean("like", user, new Timestamp(System.currentTimeMillis()), a.getId()));
                    }

                    int dislikeLimit = rand.nextInt(70) + 1;

                    for (int j = 0; j < dislikeLimit; j++) {
                        String user = listaUtenti.get(rand.nextInt(listaUtenti.size())).getUsername();
                        if (user != null) {
                          controller.addLike(
                              new LikeBean(
                                  "dislike", user, new Timestamp(System.currentTimeMillis()), a.getId()));
                        }
                    }
                }
            }


            for(int i = 0; i < 400; i++){
                String user = listaUtenti.get(rand.nextInt(listaUtenti.size())).getUsername();
                String user2 =  listaUtenti.get(rand.nextInt(listaUtenti.size())).getUsername();
                if(user != null && user2 != null) {
                    if(controller2.addRemoveFollow(user,user2,"add")){
                        System.out.println("ok");
                    }
                }
            }

            for(int i = 0; i < 100; i++){
                if(controller3.addGroup(new GroupBean((nomi.get(rand.nextInt(5))),new Timestamp(System.currentTimeMillis()), listaUtenti.get(rand.nextInt(listaUtenti.size())).getUsername(),"no description",listaGiochi.get(i).getName()))){
                    System.out.println("ok");
                }
            }

            for(int i = 0; i < 200; i++){
                String member = listaUtenti.get(rand.nextInt(listaUtenti.size())).getName();
                List<GroupBean> gruppi = controller3.showUsersGroups("leonardo","admin");
                if (gruppi.size() > 0) {
                  if (controller3.addDeleteGroupMember(
                      member, gruppi.get(rand.nextInt(gruppi.size())).getName(), "leonardo", "add")) {
                    System.out.println("ok");
                  }
                }
            }

            for(int i = 0; i < 400; i++){
                String author = listaUtenti.get(rand.nextInt(listaUtenti.size())).getUsername();
                String testo = text.get(rand.nextInt(7));
                List<GroupBean> gruppi = controller3.showUsersGroups(author,"member");

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
