package org.openjfx.Controller;

import org.openjfx.DBManager.MongoDBManager.MongoDBManager;
import org.openjfx.DBManager.Neo4jDBManager.LoginSignUpDBManager;
import org.openjfx.DBManager.Neo4jDBManager.UpdateDatabaseDBManager;
import org.openjfx.Entities.InfoComment;
import org.openjfx.Entities.InfoLike;

import java.util.logging.Logger;

public class UpdateDatabaseDBController {

    Logger logger =  Logger.getLogger(this.getClass().getName());

    public  UpdateDatabaseDBController() {
        LoginSignUpDBManager.InitializeDriver();
        MongoDBManager.createConnection();
    }

    public Boolean Neo4jAddComment(InfoComment newComm) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addComment(newComm);

        return ret;

    }


    public Boolean Neo4jAddLike(InfoLike like) {

        Boolean ret = false;
        ret = UpdateDatabaseDBManager.addLike(like);

        return ret;

    }
}
