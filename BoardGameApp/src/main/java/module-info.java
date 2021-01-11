module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.gluonhq.charm.glisten;
    requires com.gluonhq.attach.display;
    requires org.neo4j.driver;
    requires java.naming;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;
    requires jdk.net;
    requires com.google.common;
    requires java.sql;
    requires com.github.benmanes.caffeine;
    requires org.checkerframework.checker.qual;
    requires ehcache;

    opens it.unipi.dii.LSMDB.project.group5.view to javafx.fxml;
    opens it.unipi.dii.LSMDB.project.group5 to javafx.fxml;
    opens it.unipi.dii.LSMDB.project.group5.bean to javafx.base;

    exports it.unipi.dii.LSMDB.project.group5;
}