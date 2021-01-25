package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.bean.GroupBean;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.Neo4jDBManager;
import it.unipi.dii.lsmdb.project.group5.view.tablebean.GroupMemberBean;
import it.unipi.dii.lsmdb.project.group5.controller.GroupsPagesDBController;
import it.unipi.dii.lsmdb.project.group5.view.tablebean.TableGroupBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * The type Homepage groups.
 */
public class HomepageGroups {

    private static String currentGroup;
    private static String adminGroup;

    /**
     * The Filtering.
     */
    ObservableList<TableGroupBean> filtering = FXCollections.observableArrayList();
    /**
     * The Filtering 2.
     */
    ObservableList<TableGroupBean> filtering2 = FXCollections.observableArrayList();
    /**
     * The Giochi dei gruppi.
     */
    ObservableList<String> giochiDeiGruppi = FXCollections.observableArrayList("None");
    /**
     * The Nomi dei gruppi.
     */
    ObservableList<String> nomiDeiGruppi = FXCollections.observableArrayList();
    /**
     * The User actions.
     */
    ObservableList<String> userActions = FXCollections.observableArrayList("Leave group", "View posts", "View members");
    /**
     * The Admin actions.
     */
    ObservableList<String> adminActions = FXCollections.observableArrayList("Delete group", "View posts", "Add member", "View members", "Remove member");
    /**
     * The Membri gruppo.
     */
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
    public TableColumn<TableGroupBean, String> nameuser;

    @FXML
    public TableColumn<TableGroupBean, String> adminuser;

    @FXML
    public TableColumn<TableGroupBean, String> gameuser;

    @FXML
    public TableColumn<TableGroupBean, String> messageuser;

    @FXML
    public TableColumn<TableGroupBean, Integer> membersuser;

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
    Button statisticsButton;

    @FXML
    Button users;

    @FXML
    Button groups;

    @FXML
    ImageView ics;

    /**
     * Initialize.
     *
     * @throws IOException the io exception
     */
    @FXML
    void initialize() throws IOException {

        try {
            Neo4jDBManager.getDriver().verifyConnectivity();
        } catch(Exception e) {
            users.setDisable(true);
            groups.setDisable(true);
            statisticsButton.setDisable(true);
            returnToHomepage();
            return;
        }

        setGroups();
        ics.setVisible(false);

        if(LoginPageView.getLoggedRole().equals("moderator")) {
            statisticsButton.setDisable(false);
        } else {
            statisticsButton.setDisable(true);
        }
    }

    /**
     * Return to homepage.
     *
     * @throws IOException the io exception
     */
    @FXML
    void returnToHomepage() throws IOException {
        App.setRoot("HomepageArticles");
    }

    /**
     * Go to games.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToGames() throws IOException {
        App.setRoot("HomepageGames");
    }

    /**
     * Go to groups.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToGroups() throws IOException {
        App.setRoot("HomepageGroups");
    }

    /**
     * Go to friends.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToFriends() throws IOException {
        App.setRoot("HomepageUsers");
    }

    /**
     * Go to statistics.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToStatistics() throws IOException {
        App.setRoot("HomepageModeratorAnalytics");
    }

    /**
     * Go to settings.
     *
     * @throws IOException the io exception
     */
    @FXML
    void goToSettings() throws IOException {
        App.setRoot("ProfileSettingsPageView");
    }

    /**
     * Logout.
     *
     * @throws IOException the io exception
     */
    @FXML
    void logout() throws IOException {
        App.setRoot("LoginPageView");
        LoginPageView.logout();
    }

    /**
     * Sets groups.
     *
     * @throws IOException the io exception
     */
    @FXML
    void setGroups() {
        nomiDeiGruppi.clear();
        giochiDeiGruppi.clear();

        GroupsPagesDBController controller = new GroupsPagesDBController();

        utils.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        admintable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        usertable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        name.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        admin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        game.setCellValueFactory(new PropertyValueFactory<>("game"));
        timestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        members.setCellValueFactory(new PropertyValueFactory<>("members"));
        nomeMembro.setCellValueFactory(new PropertyValueFactory<>("groupMemberName"));
        nameuser.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        adminuser.setCellValueFactory(new PropertyValueFactory<>("admin"));
        gameuser.setCellValueFactory(new PropertyValueFactory<>("game"));
        messageuser.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        membersuser.setCellValueFactory(new PropertyValueFactory<>("members"));
        nomeMembro.setCellValueFactory(new PropertyValueFactory<>("groupMemberName"));

        List<GroupBean> gruppiDiCuiSonoAdmin = controller.showUsersGroups(LoginPageView.getLoggedUser(),"admin");
        List<GroupBean> gruppiDiCuiSonoMembro = controller.showUsersGroups(LoginPageView.getLoggedUser(),"member");

         if(gruppiDiCuiSonoAdmin != null) {
             for (int i = 0; i < gruppiDiCuiSonoAdmin.size(); i++) {
                 GroupBean g = gruppiDiCuiSonoAdmin.get(i);

                 TableGroupBean tableGroup = new TableGroupBean(g.getName(), g.getTimestamp().toString(),g.getLastPost().toString(), g.getAdmin(), g.getGame(), controller.countGroupMembers(g.getName(), g.getAdmin()));
                 nomiDeiGruppi.add(g.getName());

                 if (!giochiDeiGruppi.contains(g.getGame())) {
                     giochiDeiGruppi.add(g.getGame());
                 }
                 gruppiAdmin.add(tableGroup);
             }
         }


         if(gruppiDiCuiSonoMembro != null) {
             for (int i = 0; i < gruppiDiCuiSonoMembro.size(); i++) {
                 GroupBean g = gruppiDiCuiSonoMembro.get(i);
                 TableGroupBean tableGroup = new TableGroupBean(g.getName(), String.valueOf(g.getTimestamp()),String.valueOf(g.getLastPost()), g.getAdmin(), g.getGame(), controller.countGroupMembers(g.getName(), g.getAdmin()));
                 nomiDeiGruppi.add(g.getName());

                 if (!giochiDeiGruppi.contains(g.getGame())) {
                     giochiDeiGruppi.add(g.getGame());
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

    /**
     * Create group.
     *
     * @throws IOException the io exception
     */
    @FXML
    void createGroup() {

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

        if(ret) {
            TableGroupBean tableGroup = new TableGroupBean(group.getName(), String.valueOf(group.getTimestamp()),String.valueOf(group.getLastPost()), group.getAdmin(), group.getGame(),membersNumber.countGroupMembers(group.getName(),group.getAdmin()));
            gruppiAdmin.add(tableGroup);
            admintable.setItems(gruppiAdmin);
            nomiDeiGruppi.add(group.getName());
            action.setItems(adminActions);
            nomigruppi.setItems(nomiDeiGruppi);
            ics.setVisible(false);

            if(!giochiDeiGruppi.contains(group.getGame())){
                giochiDeiGruppi.add(group.getGame());
            }
            filter.setItems(giochiDeiGruppi);
        } else {
            ics.setVisible(true);
            Logger.log("addGroup problems " + group.getName());
        }
    }

    /**
     * Select action.
     *
     * @throws IOException the io exception
     */
    @FXML
    void selectAction() throws IOException {
        if(nomigruppi.getSelectionModel().getSelectedItem() != null) {
            String gruppoSelezionato = nomiDeiGruppi.get(nomigruppi.getSelectionModel().getSelectedIndex());
            String adminGruppoSelezionato = retrieveAdmin(gruppoSelezionato);

            if(action.getSelectionModel().getSelectedIndex() != -1){
                if(adminGruppoSelezionato.equals(LoginPageView.getLoggedUser())) {
                    switch (adminActions.get(action.getSelectionModel().getSelectedIndex())) {
                        case "Add member" -> addMember(gruppoSelezionato);
                        case "Delete group" -> deleteGroup(gruppoSelezionato);
                        case "View posts" -> viewPosts(gruppoSelezionato);
                        case "View members" -> viewMembers(gruppoSelezionato);
                        case "Remove member" -> removeMember(gruppoSelezionato);
                    }
                } else {
                    switch (userActions.get(action.getSelectionModel().getSelectedIndex())) {
                        case "Leave group" -> deleteGroup(gruppoSelezionato);
                        case "View posts" -> viewPosts(gruppoSelezionato);
                        case "View members" -> viewMembers(gruppoSelezionato);
                    }
                }
            }
        }
    }

    /**
     * Remove a member from a group.
     * */
    @FXML
    private void removeMember(String gruppoSelezionato) throws IOException {
        currentGroup = gruppoSelezionato;
        adminGroup = retrieveAdmin(gruppoSelezionato);
        App.setRoot("RemoveMember");
    }

    /**
     * Sets actions.
     */
    @FXML
    void setActions() {
        action.getSelectionModel().clearSelection();
        String gruppoSelezionato = nomiDeiGruppi.get(nomigruppi.getSelectionModel().getSelectedIndex());
        if(retrieveAdmin(gruppoSelezionato).equals(LoginPageView.getLoggedUser())) {
            action.setItems(adminActions);
        } else {
            action.setItems(userActions);
        }
    }

    /**
     * View the posts of a group.
     */
    private void viewPosts(String gruppoSelezionato) throws IOException {
        currentGroup = gruppoSelezionato;
        adminGroup = retrieveAdmin(gruppoSelezionato);
        App.setRoot("PostViewPage");
    }

    /**
     * Delete a group.
     */
    private void deleteGroup(String gruppoSelezionato) {
         GroupsPagesDBController controller = new GroupsPagesDBController();

        currentGroup = gruppoSelezionato;
        boolean ret = controller.deleteGroup(gruppoSelezionato,retrieveAdmin(gruppoSelezionato));

        if(ret) {
            for(int i = 0; i < gruppiAdmin.size(); i++) {
                if(gruppiAdmin.get(i).getGroupName().equals(gruppoSelezionato)){
                    gruppiAdmin.remove(i);
                }
            }
        } else {
            Logger.log("deleteGroup problems " + gruppoSelezionato);
        }

        admintable.setItems(gruppiAdmin);
    }

    /**
     * Add a member to a group.
     */
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

    /**
     * Filter research.
     *
     * @throws IOException the io exception
     */
@FXML
    void filterResearch() throws IOException {
        String gameFilter = giochiDeiGruppi.get(filter.getSelectionModel().getSelectedIndex());
        if(!gameFilter.equals("None")){
            filtering.clear();
            filtering2.clear();

            if(gameFilter != null && !gameFilter.equals("")) {

                for (int i = 0; i < gruppiAdmin.size(); i++) {
                    if (gruppiAdmin.get(i).getGame().equals(gameFilter)) {
                        filtering.add(gruppiAdmin.get(i));
                    }
                }

                admintable.setItems(filtering);

                for (int i = 0; i < gruppiMembro.size(); i++) {
                    if (gruppiMembro.get(i).getGame().equals(gameFilter)) {
                        filtering2.add(gruppiMembro.get(i));
                    }
                }

                usertable.setItems(filtering2);

            }
        } else {
            gruppiAdmin.clear();
            gruppiMembro.clear();
            setGroups();
        }
    }

    /**
     * Gets group.
     *
     * @return the group
     */
public static String getGroup() {
        return currentGroup;
    }

    /**
     * Gets admin group.
     *
     * @return the admin group
     */
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
