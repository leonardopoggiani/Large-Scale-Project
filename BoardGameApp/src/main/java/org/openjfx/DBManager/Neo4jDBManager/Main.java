package org.openjfx.DBManager.Neo4jDBManager;

import org.openjfx.Controller.LoginSignUpDBController;
import org.openjfx.Entities.User;

public class Main {

    public static void main( String[] args ) throws Exception {
        Neo4jDBManager.InitializeDriver();
        LoginSignUpDBController cont = new LoginSignUpDBController();
        User user = new User("Gaia4", "ciaociao", "Strategic", "Cards Game", 28, "normalUser" );

        cont.neo4jRegisterUserController(user);
        System.out.println("---------------------------");
        cont.neo4jLoginUserController("Gaia4", "ciaociao");
        System.out.println("---------------------------");

    }
}
