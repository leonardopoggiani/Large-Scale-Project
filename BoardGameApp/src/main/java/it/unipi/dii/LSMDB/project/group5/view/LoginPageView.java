package it.unipi.dii.LSMDB.project.group5.view;

import it.unipi.dii.LSMDB.project.group5.cache.ArticlesCache;
import it.unipi.dii.LSMDB.project.group5.cache.GamesCache;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import it.unipi.dii.LSMDB.project.group5.App;
import it.unipi.dii.LSMDB.project.group5.controller.LoginSignUpDBController;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginPageView {

    Logger logger =  Logger.getLogger(this.getClass().getName());
    private static String loggedUser;
    private static String loggedRole;
    LoginSignUpDBController controller = new LoginSignUpDBController();
    private static int logged = -1;

    @FXML
    boolean validateLogin() throws IOException {
        Scene scene = App.getScene(); // recupero la scena della login
        TextField us = (TextField) scene.lookup("#username");
        String username = us.getText();
        TextField ps = (TextField) scene.lookup("#password");
        String password = ps.getText();
        boolean ret = false;

        if(username != null && password != null ){
            loggedRole = controller.loginUser(username,password);

            if(loggedRole != null && !loggedRole.equals("fallito") && !loggedRole.equals("NA")){
                logged = 1;
                loggedUser = username;
                ret = true;
            }
        } else {
            return false;
        }

        return ret;
    }

    @FXML
    void switchSignup() throws IOException {
        App.setRoot("SignupPageView");
    }

    @FXML
    void loginResult() throws IOException {
        if(validateLogin()) {
            if(loggedRole.equals("admin")){
                App.setRoot("adminHomepage");
            } else {
                // App.setRoot("HomepageArticles");
                App.setRoot("adminHomepage");
            }
            logger.info("Login correttamente effettuato");
        } else {
            logger.info("Login non corretto");
            TextField u = (TextField) App.getScene().lookup("#username");
            TextField p = (TextField) App.getScene().lookup("#password");
            u.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
            p.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
        }
    }

    public static String getLoggedUser(){
        return loggedUser;
    }

    public static int getLogged() { return logged ; }
    public static void logout() { logged = -1;
        GamesCache cache = GamesCache.getInstance();
        cache.invalidaCache();
        ArticlesCache cacheArticle = ArticlesCache.getInstance();
        cacheArticle.invalidaCache();
    }
}
