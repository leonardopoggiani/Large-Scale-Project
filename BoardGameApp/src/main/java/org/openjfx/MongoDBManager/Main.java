package org.openjfx.MongoDBManager;

import org.openjfx.MongoDBManager.MongoDBManager;

public class Main {
    public static void main (String[] args){
        MongoDBManager.createConnection();
        System.out.println("Connesso");
        MongoDBManager.close();

    }
}
