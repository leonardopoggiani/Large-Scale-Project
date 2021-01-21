package it.unipi.dii.lsmdb.project.group5.view.tablebean;

import javafx.beans.property.SimpleStringProperty;

public class GroupMemberBean {

    SimpleStringProperty groupMemberName;

    public GroupMemberBean(String name){ this.groupMemberName = new SimpleStringProperty(name); }
}