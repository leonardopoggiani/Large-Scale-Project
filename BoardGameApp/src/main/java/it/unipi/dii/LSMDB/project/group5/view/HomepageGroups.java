package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.bean.GroupBean;
import it.unipi.dii.LSMDB.project.group5.bean.GroupMemberBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.controller.GroupsPostsDBController;
import it.unipi.dii.LSMDB.project.group5.controller.UpdateDatabaseDBController;
import it.unipi.dii.LSMDB.project.group5.view.tablebean.TableGroupBean;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

public class HomepageGroups {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    private static String currentGroup;
    private static String adminGroup;

    ObservableList<TableGroupBean> filtering = FXCollections.observableArrayList();
    ObservableList<String> giochiDeiGruppi = FXCollections.observableArrayList();
    ObservableList<String> nomiDeiGruppi = FXCollections.observableArrayList();
    ObservableList<String> userActions = FXCollections.observableArrayList("Add post", "Leave group", "View posts", "View members");
    ObservableList<String> adminActions = FXCollections.observableArrayList("Add post", "Delete group", "View posts", "Add member", "View members");
    ObservableList<GroupMemberBean> membriGruppo = FXCollections.observableArrayList();

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
    public TableColumn<GroupMemberBean, String> nomeMembro;

    @FXML
    TableView admintable;

    @FXML
    TableView usertable;

    @FXML
    TableView utils;

    @FXML
    ComboBox filter;

    @FXML
    ComboBox nomigruppi;

    @FXML
    ComboBox action;

    @FXML
    TextField groupname;

    @FXML
    TextField referredgame;

    @FXML
    TextField description;

    @FXML
    void initialize() throws IOException {
        setGroups();
    }

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

        utils.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        admintable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        usertable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        name.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        admin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        game.setCellValueFactory(new PropertyValueFactory<>("game"));
        timestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        members.setCellValueFactory(new PropertyValueFactory<>("members"));
        nomeMembro.setCellValueFactory(new PropertyValueFactory<>("groupMemberName"));

         List<GroupBean> gruppiDiCuiSonoAdmin = controller.neo4jShowUsersGroups(LoginPageView.getLoggedUser(),"admin");
         List<GroupBean> gruppiDiCuiSonoMembro = controller.neo4jShowUsersGroups(LoginPageView.getLoggedUser(),"member");

        for(int i = 0; i < gruppiDiCuiSonoAdmin.size(); i++){
            GroupBean g = gruppiDiCuiSonoAdmin.get(i);
            TableGroupBean tableGroup = new TableGroupBean(g.getName(),g.getTimestamp(),g.getAdmin(),g.getGame(),controller.neo4jCountGroupMembers(g.getName(),g.getAdmin()));
            nomiDeiGruppi.add(g.getName());

            if(!giochiDeiGruppi.contains(g.getName())){
                giochiDeiGruppi.add(g.getName());
            }
            gruppiAdmin.add(tableGroup);
        }

        for(int i = 0; i < gruppiDiCuiSonoMembro.size(); i++){
            GroupBean g = gruppiDiCuiSonoMembro.get(i);
            TableGroupBean tableGroup = new TableGroupBean(g.getName(),g.getTimestamp(),g.getAdmin(),g.getGame(),controller.neo4jCountGroupMembers(g.getName(),g.getAdmin()));
            nomiDeiGruppi.add(g.getName());

            if(!giochiDeiGruppi.contains(g.getName())){
                giochiDeiGruppi.add(g.getName());
            }

            gruppiMembro.add(tableGroup);
        }

        admintable.setItems(gruppiAdmin);
        usertable.setItems(gruppiMembro);

        nomigruppi.setItems(nomiDeiGruppi);
        action.setItems(adminActions);
        filter.setItems(giochiDeiGruppi);
        utils.setItems(membriGruppo);

    }

    @FXML
    void createGroup() throws IOException {
        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();
        GroupsPostsDBController membersNumber = new GroupsPostsDBController();

        String name = groupname.getText();
        String game = referredgame.getText();
        String des = description.getText();

        if(name.equals("")){
            groupname.setStyle("-fx-background-color: #ff0000; -fx-font-size: 12;");
            return;
        }

        if(game.equals("")) {
            referredgame.setStyle("-fx-background-color: #ff0000; -fx-font-size: 12;");
            return;
        }

        if(des.equals("")){
            des = "No description provided";
        }

        groupname.setText("");
        referredgame.setText("");
        description.setText("");

        GroupBean group = new GroupBean(name, new Timestamp(System.currentTimeMillis()), LoginPageView.getLoggedUser(), des, game);
        boolean ret = controller.Neo4jAddGroup(group);
        System.out.println("Ritorno " + ret);

        if(ret) {
            TableGroupBean tableGroup = new TableGroupBean(group.getName(), group.getTimestamp(), group.getAdmin(), group.getGame(),membersNumber.neo4jCountGroupMembers(group.getName(),group.getAdmin()));
            System.out.println(tableGroup);
            gruppiAdmin.add(tableGroup);
            admintable.setItems(gruppiAdmin);
            nomiDeiGruppi.add(group.getName());
            action.setItems(adminActions);
            nomigruppi.setItems(nomiDeiGruppi);

            if(!giochiDeiGruppi.contains(group.getGame())){
                giochiDeiGruppi.add(group.getGame());
            }
            filter.setItems(giochiDeiGruppi);
        } else {
            logger.info("problemi nella addGroup");
        }
    }

    @FXML
    void selectAction() throws IOException {
        if(nomigruppi.getSelectionModel().getSelectedItem() != null) {
            String gruppoSelezionato = nomiDeiGruppi.get(nomigruppi.getSelectionModel().getSelectedIndex());
            switch (adminActions.get(action.getSelectionModel().getSelectedIndex())) {
                case "Add post" -> addPost(gruppoSelezionato);
                case "Add member" -> addMember(gruppoSelezionato);
                case "Delete group" -> deleteGroup(gruppoSelezionato);
                case "View posts" -> viewPosts(gruppoSelezionato);
                case "View members" -> viewMembers(gruppoSelezionato);
            }
        }
    }

    @FXML
    void setActions() throws IOException {
        String gruppoSelezionato = nomiDeiGruppi.get(nomigruppi.getSelectionModel().getSelectedIndex());
        if(retrieveAdmin(gruppoSelezionato).equals(LoginPageView.getLoggedUser())) {
            action.setItems(adminActions);
        } else {
            action.setItems(userActions);
        }
    }

    private void viewPosts(String gruppoSelezionato) {
        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();
        GroupsPostsDBController membersNumber = new GroupsPostsDBController();
        currentGroup = gruppoSelezionato;
    }

    private void deleteGroup(String gruppoSelezionato) {
        UpdateDatabaseDBController controller = new UpdateDatabaseDBController();

        currentGroup = gruppoSelezionato;
        boolean ret = controller.Neo4jDeleteGroup(gruppoSelezionato,retrieveAdmin(gruppoSelezionato));

        for(int i = 0; i < gruppiAdmin.size(); i++) {
            if(gruppiAdmin.get(i).getGroupName().equals(gruppoSelezionato)){
                gruppiAdmin.remove(i);
            }
        }

        admintable.setItems(gruppiAdmin);
    }

    private void addPost(String gruppoSelezionato) {
        currentGroup = gruppoSelezionato;
    }

    private void addMember(String gruppoSelezionato) throws IOException {
        currentGroup = gruppoSelezionato;
        adminGroup = retrieveAdmin(gruppoSelezionato);
        App.setRoot("AddMember");
    }

    private void viewMembers(String gruppoSelezionato) throws IOException {
        GroupsPostsDBController controller = new GroupsPostsDBController();
        currentGroup = gruppoSelezionato;

        List<String> listMembers = controller.neo4jShowGroupsMembers(gruppoSelezionato,retrieveAdmin(gruppoSelezionato));
        ObservableList<GroupMemberBean> members = FXCollections.observableArrayList();

        for(int i = 0; i < listMembers.size(); i++){
            members.add(new GroupMemberBean(listMembers.get(i)));
        }

        utils.setItems(members);
    }

    private void filterResearch(){
        String gameFilter = nomiDeiGruppi.get(filter.getSelectionModel().getSelectedIndex());
        for(int i = 0; i < gruppiAdmin.size(); i++) {
            if(gruppiAdmin.get(i).getGame().equals(gameFilter)) {
                filtering.add(gruppiAdmin.get(i));
            }
        }
    }

    public static String getGroup() {
        return currentGroup;
    }

    public static String getAdminGroup() {
        return adminGroup;
    }

    private String retrieveAdmin(String group) {
        for(int i = 0; i < gruppiMembro.size(); i++) {
            TableGroupBean tb = gruppiMembro.get(i);
            if(tb.getGroupName().equals(group)) {
                return tb.getAdmin();
            }
        }

        for(int i = 0; i < gruppiAdmin.size(); i++) {
            TableGroupBean tb = gruppiAdmin.get(i);
            if(tb.getGroupName().equals(group)) {
                return tb.getAdmin();
            }
        }
        return "";
    }

}
