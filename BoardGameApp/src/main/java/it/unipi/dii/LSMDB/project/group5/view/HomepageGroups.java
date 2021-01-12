package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.bean.GroupBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.controller.GroupsPostsDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UpdateDatabaseDBController;
import it.unipi.dii.LSMDB.project.group5.bean.TableGroupBean;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class HomepageGroups implements Initializable {

    int giaCaricato = -1;
    private TableView<TableGroupBean> tableMembro;
    private TableView<TableGroupBean> tableAdmin;
    private ObservableList<TableGroupBean> gruppiAdmin = FXCollections.observableArrayList();
    private ObservableList<TableGroupBean> gruppiMembro = FXCollections.observableArrayList();

    @FXML
    public TableColumn<TableGroupBean, String> name;

    @FXML
    public TableColumn<TableGroupBean, String> admin;

    @FXML
    public TableColumn<TableGroupBean, String> game;

    @FXML
    public TableColumn<TableGroupBean, String> timestamp;

    @FXML
    public TableColumn<TableGroupBean, Integer> members;

    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

    @FXML
    void goToGames() throws IOException {
        App.setRoot("HomepageGames");
    }

    @FXML
    void goToGroups() throws IOException {
        App.setRoot("HomepageGroups");
    }

    @FXML
    void goToFriends() throws IOException {
        App.setRoot("HomepageUsers");
    }

    @FXML
    void goToSettings() throws IOException {
        App.setRoot("ProfileSettingsPageView");
    }

    @FXML
    void setGroups() throws IOException {
        GroupsPostsDBController controller = new GroupsPostsDBController();
        if(giaCaricato == -1) {

            tableAdmin = (TableView<TableGroupBean>) App.getScene().lookup("#admintable");
            tableAdmin.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            tableMembro = (TableView<TableGroupBean>) App.getScene().lookup("#table2");
            tableMembro.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            name.setCellValueFactory(new PropertyValueFactory<>("groupName"));
            admin.setCellValueFactory(new PropertyValueFactory<>("admin"));
            game.setCellValueFactory(new PropertyValueFactory<>("game"));
            timestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
            members.setCellValueFactory(new PropertyValueFactory<>("members"));

             List<GroupBean> gruppiDiCuiSonoAdmin = controller.neo4jShowUsersGroups(LoginPageView.getLoggedUser(),"admin");
             List<GroupBean> gruppiDiCuiSonoMembro = controller.neo4jShowUsersGroups(LoginPageView.getLoggedUser(),"member");

             name = new TableColumn("Name");
             timestamp = new TableColumn<>("Timestamp");
             admin = new TableColumn<>("Admin");
             game = new TableColumn<>("Game");
             members = new TableColumn<>("Members");

            for(int i = 0; i < gruppiDiCuiSonoAdmin.size(); i++){
                GroupBean g = gruppiDiCuiSonoAdmin.get(i);
                TableGroupBean tableGroup = new TableGroupBean(g.getName(),g.getTimestamp(),g.getAdmin(),g.getGame(),controller.neo4jCountGroupMembers(g.getName(),g.getAdmin()));
                gruppiAdmin.add(tableGroup);
            }

            for(int i = 0; i < gruppiDiCuiSonoMembro.size(); i++){
                GroupBean g = gruppiDiCuiSonoMembro.get(i);
                TableGroupBean tableGroup = new TableGroupBean(g.getName(),g.getTimestamp(),g.getAdmin(),g.getGame(),controller.neo4jCountGroupMembers(g.getName(),g.getAdmin()));
                gruppiMembro.add(tableGroup);
            }

            tableAdmin.setItems(gruppiAdmin);
            tableMembro.setItems(gruppiMembro);

            giaCaricato = 1;
        }
    }

    @FXML
    void createGroup() throws IOException {
        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();
        GroupsPostsDBController membersNumber = new GroupsPostsDBController();

        TextField tf = (TextField) App.getScene().lookup("#groupname");
        TextField tf2 = (TextField) App.getScene().lookup("#referredgame");
        TextField tf3 = (TextField) App.getScene().lookup("#description");

        String groupname = tf.getText();
        String game = tf2.getText();
        String description = tf3.getText();

        if(groupname.equals("")){
            tf.setStyle("-fx-background-color: #ff0000; -fx-font-size: 12;");
            return;
        }

        if(game.equals("")) {
            tf2.setStyle("-fx-background-color: #ff0000; -fx-font-size: 12;");
            return;
        }

        if(description.equals("")){
            description = "No description provided";
        }

        tf.setText("");
        tf2.setText("");
        tf3.setText("");

        GroupBean group = new GroupBean(groupname, new Timestamp(System.currentTimeMillis()), LoginPageView.getLoggedUser(), description, game);
        boolean ret = controller.Neo4jAddGroup(group);

        //if(ret) {
            TableGroupBean tableGroup = new TableGroupBean(group.getName(), group.getTimestamp(), group.getAdmin(), group.getGame(),membersNumber.neo4jCountGroupMembers(group.getName(),group.getAdmin()));
            System.out.println(tableGroup);
            gruppiAdmin.add(tableGroup);
            tableAdmin.setItems(gruppiAdmin);
        //}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setGroups();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
