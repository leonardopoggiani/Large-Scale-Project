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

    opens org.openjfx.View to javafx.fxml;
    opens org.openjfx to javafx.fxml;

    exports org.openjfx;
}