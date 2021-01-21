package it.unipi.dii.lsmdb.project.group5.view.tablebean;

import javafx.beans.property.SimpleStringProperty;

public class GroupMemberBean {

    SimpleStringProperty groupMemberName;

    public GroupMemberBean(String name){ this.groupMemberName = new SimpleStringProperty(name); }

    public String getGroupMemberName() {
        return groupMemberName.get();
    }

    public void setGroupMemberName(String groupMemberName){
        this.groupMemberName = new SimpleStringProperty(groupMemberName);
    }
}