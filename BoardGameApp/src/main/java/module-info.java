module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;
    requires com.gluonhq.charm.glisten;
    requires com.gluonhq.attach.display;
    requires org.neo4j.driver;
    requires java.naming;
    //requires org.mongodb.driver.sync.client;
    //requires mongo.java.driver;


    opens org.openjfx.View to javafx.fxml;
    opens org.openjfx to javafx.fxml; 
    //opens org.mongodb.driver.sync.client;


    exports org.openjfx;
}