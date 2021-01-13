package it.unipi.dii.LSMDB.project.group5.bean;

import javafx.beans.property.SimpleStringProperty;

public class GroupMember {
    SimpleStringProperty groupMemberName;
    public GroupMember(String name){ this.groupMemberName = new SimpleStringProperty(name); }
    public String getGroupMemberName(){ return groupMemberName.get(); }
    public void setGroupMemberName(String name) {this.groupMemberName = new SimpleStringProperty(name); }
}