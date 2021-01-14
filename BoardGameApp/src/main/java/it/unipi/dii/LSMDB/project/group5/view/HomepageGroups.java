package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.bean.GroupBean;
import it.unipi.dii.LSMDB.project.group5.bean.GroupMemberBean;
import it.unipi.dii.LSMDB.project.group5.controller.GroupsPagesDBController;
import it.unipi.dii.LSMDB.project.group5.view.tablebean.TableGroupBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    ObservableList<String> adminActions = FXCollections.observableArrayList("Add post", "Delete group", "View posts", "Add member", "View members", "Remove member");
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
    public TableColumn<TableGroupBean, Integer> numberposts;

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

    public void setTimestampLastPost(String group) {
        for(int i = 0; i < gruppiMembro.size(); i++) {
            TableGroupBean tb = gruppiMembro.get(i);
            if(tb.getGroupName().equals(group)) {
                tb.updateTimestamp();
            }
        }

        for(int i = 0; i < gruppiAdmin.size(); i++) {
            TableGroupBean tb = gruppiAdmin.get(i);
            if(tb.getGroupName().equals(group)) {
                tb.updateTimestamp();
            }
        }
    }

    public void updateNumberPosts(String group) {
        for(int i = 0; i < gruppiMembro.size(); i++) {
            TableGroupBean tb = gruppiMembro.get(i);
            if(tb.getGroupName().equals(group)) {
                tb.updateNumberPost();
            }
        }

        for(int i = 0; i < gruppiAdmin.size(); i++) {
            TableGroupBean tb = gruppiAdmin.get(i);
            if(tb.getGroupName().equals(group)) {
                tb.updateNumberPost();
            }
        }
    }

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
        GroupsPagesDBController controller = new GroupsPagesDBController();

        utils.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        admintable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        usertable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        name.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        admin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        game.setCellValueFactory(new PropertyValueFactory<>("game"));
        timestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        members.setCellValueFactory(new PropertyValueFactory<>("members"));
        numberposts.setCellValueFactory(new PropertyValueFactory<>("members"));

        nomeMembro.setCellValueFactory(new PropertyValueFactory<>("groupMemberName"));

         List<GroupBean> gruppiDiCuiSonoAdmin = controller.showUsersGroups(LoginPageView.getLoggedUser(),"admin");
         List<GroupBean> gruppiDiCuiSonoMembro = controller.showUsersGroups(LoginPageView.getLoggedUser(),"member");

         if(gruppiDiCuiSonoAdmin != null) {
             for (int i = 0; i < gruppiDiCuiSonoAdmin.size(); i++) {
                 GroupBean g = gruppiDiCuiSonoAdmin.get(i);
                 TableGroupBean tableGroup = new TableGroupBean(g.getName(), g.getTimestamp(), g.getAdmin(), g.getGame(), controller.countGroupMembers(g.getName(), g.getAdmin()), 0);
                 nomiDeiGruppi.add(g.getName());

                 if (!giochiDeiGruppi.contains(g.getName())) {
                     giochiDeiGruppi.add(g.getName());
                 }
                 gruppiAdmin.add(tableGroup);
             }
         }

         if(gruppiDiCuiSonoMembro != null) {
             for (int i = 0; i < gruppiDiCuiSonoMembro.size(); i++) {
                 GroupBean g = gruppiDiCuiSonoMembro.get(i);
                 TableGroupBean tableGroup = new TableGroupBean(g.getName(), g.getTimestamp(), g.getAdmin(), g.getGame(), controller.countGroupMembers(g.getName(), g.getAdmin()), 0);
                 nomiDeiGruppi.add(g.getName());

                 if (!giochiDeiGruppi.contains(g.getName())) {
                     giochiDeiGruppi.add(g.getName());
                 }

                 gruppiMembro.add(tableGroup);
             }
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

        GroupsPagesDBController membersNumber = new GroupsPagesDBController();

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
        boolean ret = membersNumber.addGroup(group);
        System.out.println("Ritorno " + ret);

        if(ret) {
            TableGroupBean tableGroup = new TableGroupBean(group.getName(), group.getTimestamp(), group.getAdmin(), group.getGame(),membersNumber.countGroupMembers(group.getName(),group.getAdmin()), 0);
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
                case "Remove member" ->  removeMember(gruppoSelezionato);
            }
        }
    }

    private void removeMember(String gruppoSelezionato) throws IOException {
        currentGroup = gruppoSelezionato;
        adminGroup = retrieveAdmin(gruppoSelezionato);
        App.setRoot("RemoveMember");
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

    private void viewPosts(String gruppoSelezionato) throws IOException {
        GroupsPagesDBController controller = new GroupsPagesDBController();
        currentGroup = gruppoSelezionato;
        adminGroup = retrieveAdmin(gruppoSelezionato);
        App.setRoot("PostViewPage");
    }

    private void deleteGroup(String gruppoSelezionato) {
         GroupsPagesDBController controller = new GroupsPagesDBController();

        currentGroup = gruppoSelezionato;
        boolean ret = controller.deleteGroup(gruppoSelezionato,retrieveAdmin(gruppoSelezionato));

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
        GroupsPagesDBController controller = new GroupsPagesDBController();
        currentGroup = gruppoSelezionato;

        List<String> listMembers = controller.showGroupsMembers(gruppoSelezionato,retrieveAdmin(gruppoSelezionato));
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
