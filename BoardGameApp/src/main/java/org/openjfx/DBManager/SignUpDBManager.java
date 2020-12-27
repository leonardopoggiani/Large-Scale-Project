package org.openjfx.DBManager;

import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import static org.neo4j.driver.Values.parameters;

public class SignUpDBManager extends Neo4jDBManager {


     /** Funzione che permette la registrazione di un utente nuovo tramite l'utilizzo della funzione
     * createUtenteNode
     * @param username
     * @param category1
     * @param category2
     * @param age
     * @param role
     */

    public static void registerUser(final String username, final String category1, final String category2, final int age, final String role) {
        try (Session session = driver.session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MERGE (u:User {username: $username, category_1: $category1, " +
                                "category_2: $category2, age: $age, role: $role})",
                        parameters("username", username, "category_1", category1, "category_2", category2,
                                 "age", age, "role", role));

                return null;
            });
        }
    }
}

