package it.unipi.dii.lsmdb.project.group5.view;

import it.unipi.dii.lsmdb.project.group5.cache.ArticlesCache;
import it.unipi.dii.lsmdb.project.group5.cache.GamesCache;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.controller.LoginSignUpDBController;
import java.io.IOException;

/** The type Login page view. */
public class LoginPageView {

    private static String loggedUser;
    private static String loggedRole;

  /** The Controller. */
  LoginSignUpDBController controller = new LoginSignUpDBController();

  /**
   * Validate login boolean.
   *
   * @return the boolean
   * @throws IOException the io exception
   */
  @FXML
  boolean validateLogin() throws IOException {
        Scene scene = App.getScene();
        TextField us = (TextField) scene.lookup("#username");
        String username = us.getText();
        TextField ps = (TextField) scene.lookup("#password");
        String password = ps.getText();
        boolean ret = false;

        if(username != null && password != null ){
            loggedRole = controller.loginUser(username,password);

            if(loggedRole != null && !loggedRole.equals("fallito") && !loggedRole.equals("NA")){
                loggedUser = username;
                ret = true;
            }
        } else {
            Logger.log("login not valid");
            return false;
        }

        return ret;
    }

  /**
   * Switch signup.
   *
   * @throws IOException the io exception
   */
  @FXML
  void switchSignup() throws IOException {
        App.setRoot("SignupPageView");
    }

  /**
   * Login result.
   *
   * @throws IOException the io exception
   */
  @FXML
  void loginResult() throws IOException {
        if(validateLogin()) {
            Logger.log("correct login " + loggedUser);
            if(loggedRole.equals("admin")){
                if(HomepageArticles.getLimitedVersion()){
                    App.getScene().getWindow().hide();
                    return;
                }
                App.setRoot("adminHomepage");
            } else {
                App.setRoot("HomepageArticles");
            }
        } else {
            Logger.log("login not correct");
            TextField u = (TextField) App.getScene().lookup("#username");
            TextField p = (TextField) App.getScene().lookup("#password");
            u.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
            p.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12;");
        }
    }

  /**
   * Get logged user string.
   *
   * @return the string
   */
  public static String getLoggedUser() {
        return loggedUser;
    }

  /**
   * Gets logged role.
   *
   * @return the logged role
   */
  public static String getLoggedRole() {
    return loggedRole ; }

  /** Logout. */
  public static void logout() {
        GamesCache cache = GamesCache.getInstance();
        cache.invalidaCache();
        ArticlesCache cacheArticle = ArticlesCache.getInstance();
        cacheArticle.invalidaCache();
    }
}
