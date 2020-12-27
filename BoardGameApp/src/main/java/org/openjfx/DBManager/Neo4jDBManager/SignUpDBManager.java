package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import org.openjfx.Entities.User;

import static org.neo4j.driver.Values.parameters;

public class SignUpDBManager extends Neo4jDBManager {


     /** Funzione che permette la registrazione di un utente nuovo tramite l'utilizzo della funzione
     * createUtenteNode

     */

    public static void registerUser(User u) {
        try (Session session = driver.session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MERGE (u:User {username: $username, category1: $category1, " +
                                "category2: $category2, age: $age, role: $role})",
                        parameters("username", u.getUsername(), "category1", u.getCategory1(), "category2", u.getCategory2(),
                                 "age", u.getAge(), "role", u.getRole()));

                return null;
            });
        }
    }
}

