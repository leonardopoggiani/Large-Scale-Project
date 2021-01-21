package it.unipi.dii.lsmdb.project.group5.view.tablebean;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * The bean class for groups in table
 */

public class TableGroupBean {
  /** The Group name. */
  SimpleStringProperty groupName;

  /** The Timestamp. */
  SimpleStringProperty timestamp;

  /** The Timestamp last post. */
  SimpleStringProperty timestampLastPost;

  /** The Admin. */
  SimpleStringProperty admin;

  /** The Game. */
  SimpleStringProperty game;

  /** The Members. */
  SimpleIntegerProperty members;

  /**
   * Instantiates a new Table group bean.
   *
   * @param name the name of the group
   * @param time the time of the creation
   * @param lastPost the time of the last post
   * @param admin the admin of the group
   * @param game the game that the group is talking about
   * @param members the number of members of the groups
   */
  public TableGroupBean(
      String name, String time, String lastPost, String admin, String game, int members) {
        this.groupName = new SimpleStringProperty(name);
        this.timestamp = new SimpleStringProperty(String.valueOf(time));
        this.admin = new SimpleStringProperty(admin);
        this.game = new SimpleStringProperty(game);
        this.members = new SimpleIntegerProperty(members);
        this.timestampLastPost = new SimpleStringProperty(String.valueOf(lastPost));
    }

  /**
   * Gets group name.
   *
   * @return the group name
   */
  public String getGroupName() {
        return groupName.get();
    }

  /**
   * Gets game.
   *
   * @return the game
   */
  public String getGame() {
        return game.get();
    }

  /**
   * Gets admin.
   *
   * @return the admin
   */
  public String getAdmin() {
        return admin.get();
    }

  /**
   * Gets timestamp.
   *
   * @return the timestamp
   */
  public String getTimestamp() {
        return timestamp.get();
    }

  /**
   * Gets members.
   *
   * @return the members
   */
  public int getMembers() {
    return members.get(); }

    @Override
    public String toString(){
        return getGroupName() + " | " + getAdmin() + " | " + getTimestamp() + " | " + getGame();
    }

}
