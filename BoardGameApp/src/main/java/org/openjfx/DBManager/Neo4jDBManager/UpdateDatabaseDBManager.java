package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.openjfx.Entities.*;

import java.sql.Timestamp;
import java.util.HashMap;

public class UpdateDatabaseDBManager extends Neo4jDBManager {

    /**
     * La funzione aggiunge un commento ad un articolo
     * @param newComm
     * @return true se ha aggiunto con successo
     * @return false altrimenti
     */

    public static Boolean addComment(final InfoComment newComm) {
        try (Session session = driver.session()) {
            boolean res;
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddComment(tx, newComm);
                }
            });


        }
    }


    /**
     * La funzione aggiunge un commento ad un articolo
     * @param tx
     * @param newComm
     * @return true se ha aggiunto con successo
     * @return false altrimenti
     */
    private static Boolean transactionAddComment(Transaction tx, InfoComment newComm) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("authorComm", newComm.getAuthor());
        parameters.put("text", newComm.getText());
        parameters.put("timestamp", newComm.getTimestamp().toString());
        parameters.put("authorArt", newComm.getAuthorArt());
        parameters.put("title", newComm.getTitleArt());

        Result result = tx.run("MATCH(u:User {username:$authorComm}),(ua:User {username:$authorArt})-[:PUBLISHED]->(a:Article{name:$title}) " +
                        "CREATE (u)-[c:COMMENTED{timestamp:$timestamp, text:$text}]->(a) " +
                        "return c"
                , parameters);
        if (result.hasNext()) {
            return true;
        }
        return false;
    }

    /**
     * La funzione aggiunge un like or dislike ad un articolo
     * @param like
     * @return 2 se ha aggiunto un like(dislike)
     * @return 1 se ha eliminato un like(dislike)
     * @return 0 altrimenti
     */

    public static Integer addLike(final InfoLike like) {
        try (Session session = driver.session()) {
            boolean res;
            return session.writeTransaction(new TransactionWork<Integer>() {
                @Override
                public Integer execute(Transaction tx) {
                    return transactionAddLike(tx, like);
                }
            });


        }
    }

    /**
     * La funzione aggiunge un like or dislike ad un articolo
     * @param tx
     * @param like
     * @return 2 se ha aggiunto un like(dislike)
     * @return 1 se ha eliminato un like(dislike)
     * @return 0 altrimenti
     */
    private static Integer transactionAddLike(Transaction tx, InfoLike like) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("authorLike", like.getAuthor());
        parameters.put("type", like.getType());
        parameters.put("timestamp", like.getTimestamp().toString());
        parameters.put("authorArt", like.getAuthorArt());
        parameters.put("title", like.getTitleArt());

        Result result0 = tx.run("MATCH (ua:User {username:$authorArt})-[:PUBLISHED]->(a:Article{name:$title})<-[l:LIKED{type:$type}]-(u:User{username:$authorLike}) return l"
                , parameters);

        if (result0.hasNext()) {
            System.out.println("Trovato sto per eliminare");
            Result result1 = tx.run("MATCH (a:Article{name:$title})<-[l:LIKED{type:$type}]-(u:User{username:$authorLike}) delete l"
                    , parameters);
            System.out.println("Ho eliminato");
            return 1;

        } else {

            Result result = tx.run("MATCH(u:User {username:$authorLike}),(ua:User {username:$authorArt})-[:PUBLISHED]->(a:Article{name:$title}) " +
                            "CREATE (u)-[l:LIKED{timestamp:$timestamp, type:$type}]->(a) " +
                            "return l"
                    , parameters);
            if (result.hasNext()) {
                System.out.println("Ho aggiunto");
                return 2;
            }
            return 0;
        }


    }

    /**
     * La funzione elimina un commento ad un articolo
     * @param delComm
     * @return true se ha eliminato correttamente il commento
     * @return false altrimenti
     */
    public static Boolean deleteComment(final InfoComment delComm) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteComment(tx, delComm);
                }
            });


        }
    }


    /**
     * La funzione elimina un commento ad un articolo
     * @param tx
     * @param delComm
     * @return true se ha eliminato correttamente il commento
     * @return false altrimenti
     */

    private static Boolean transactionDeleteComment(Transaction tx, InfoComment delComm) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("authorComm", delComm.getAuthor());
        parameters.put("timestamp", delComm.getTimestamp().toString());
        parameters.put("title", delComm.getTitleArt());

        Result result = tx.run("MATCH (ua:User {username:$authorComm})-[c:COMMENTED {timestamp:$timestamp}]->(a:Article{name:$title}) " +
                        "DELETE c return c"
                , parameters);

        return true;
    }



    /**
     * La funzione aggiunge una review ad un gioco
     * @param newRev
     * @return true se ha aggiunto correttamente la review
     * @return false altrimenti
     */
    public static Boolean addReview(final InfoReview newRev) {
        try (Session session = driver.session()) {
            boolean res;
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddReview(tx, newRev);
                }
            });


        }
    }

    /**
     * La funzione aggiunge una review ad un gioco
     * @param tx
     * @param newRev
     * @return true se ha aggiunto correttamente la review
     * @return false altrimenti
     */

    private static Boolean transactionAddReview(Transaction tx, InfoReview newRev) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", newRev.getAuthor());
        parameters.put("text", newRev.getText());
        parameters.put("timestamp", newRev.getTimestamp().toString());
        parameters.put("game", newRev.getGame());


        Result result = tx.run("MATCH(u:User {username:$author}),(g:Game{name:$game}) " +
                        "CREATE (u)-[r:REVIEWED{timestamp:$timestamp, text:$text}]->(g) " +
                        "return r"
                , parameters);
        if (result.hasNext()) {
            return true;
        }
        return false;
    }

   /**
    * La funzione elimina una review ad un gioco
    * @param delRev
    * @return true se ha eliminato correttamente la review
    * @return false altrimenti
    */

    public static Boolean deleteReview(final InfoReview delRev) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteRev(tx, delRev);
                }
            });


        }
    }


    /**
     * La funzione elimina una review ad un gioco
     * @param tx
     * @param delRev
     * @return true se ha eliminato correttamente la review
     * @return false altrimenti
     */

    private static Boolean transactionDeleteRev(Transaction tx, InfoReview delRev) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", delRev.getAuthor());
        parameters.put("timestamp", delRev.getTimestamp().toString());
        parameters.put("game", delRev.getGame());

        Result result = tx.run("MATCH (ua:User {username:$author})-[r:REVIEWED {timestamp:$timestamp}]->(g:Game{name:$game}) " +
                        "DELETE r return r"
                , parameters);


        return true;
    }

    /**
     * La funzione aggiunge un rating ad un gioco
     * @param newRate
     * @return true se ha aggiunto correttamente il rating
     * @return false altrimenti
     */
    public static Boolean addRating(final InfoRate newRate) {
        try (Session session = driver.session()) {
            boolean res;
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddRating(tx, newRate);
                }
            });


        }
    }

    /**
     * La funzione aggiunge un rating ad un gioco
     * @param tx
     * @param newRate
     * @return true se ha aggiunto correttamente il rating
     * @return false altrimenti
     */

    private static Boolean transactionAddRating(Transaction tx, InfoRate newRate) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", newRate.getAuthor());
        parameters.put("vote", newRate.getVote());
        parameters.put("timestamp", newRate.getTimestamp().toString());
        parameters.put("game", newRate.getGame());

        Result result0 = tx.run("MATCH (u:User {username:$author})-[r:RATED]->(g:Game{name:$game})" +
                "return r", parameters);
        if (result0.hasNext()) {
            return false;
        }

        Result result = tx.run("MATCH(u:User {username:$author}),(g:Game{name:$game}) " +
                        "CREATE (u)-[r:RATED{timestamp:$timestamp, vote:$vote}]->(g) " +
                        "return r"
                , parameters);
        if (result.hasNext()) {
            return true;
        }
        return false;
    }

    /**
     * La funzione crea un nuovo gruppo
     * @param newGroup
     * @return true se ha creato correttamente il gruppo
     * @return false altrimenti
     */

    public static Boolean addGroup(final InfoGroup newGroup) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddGroup(tx, newGroup);
                }
            });


        }
    }


    /**
     * La funzione crea un nuovo gruppo
     * @param tx
     * @param group
     * @return true se ha creato correttamente il gruppo
     * @return false altrimenti
     */
    private static Boolean transactionAddGroup(Transaction tx, InfoGroup group) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("admin", group.getAdmin());
        parameters.put("game", group.getGame());
        parameters.put("timestamp", group.getTimestamp().toString());
        parameters.put("desc", group.getDescription());
        parameters.put("name", group.getName());

        Result result0 = tx.run("MATCH (u:User {username:$admin})-[b:BE_PART]->(g:Group{name:$name, admin:$admin}) return g"
                , parameters);

        if (result0.hasNext()) {
            System.out.println("Già hai creato un gruppo con questo nome, cambialo!");
            return false;
        } else {

            Result result = tx.run("MATCH (u:User{username:$admin}),(ga:Game{name:$game})" +
                            "CREATE (u)-[:BE_PART{timestamp:$timestamp}]->(gr:Group {name:$name,description:$desc, admin:$admin})-[:REFERRED]->(ga)"
                    , parameters);
            if (result.hasNext()) {
                System.out.println("Ho aggiunto il nuovo gruppo");
                return true;
            }
            return false;
        }


    }

    /**
     * La funzione elimina un gruppo
     * @param delGroup
     * @return true se ha eliminato correttamente il gruppo
     * @return false altrimenti
     */

    public static Boolean deleteGroup(final InfoGroup delGroup) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteGroup(tx, delGroup);
                }
            });


        }
    }


    /**
     * La funzione elimina un gruppo
     * @param tx
     * @param delGroup
     * @return true se ha eliminato correttamente il gruppo
     * @return false altrimenti
     */


    private static Boolean transactionDeleteGroup(Transaction tx, InfoGroup delGroup) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("admin", delGroup.getAdmin());
        parameters.put("name", delGroup.getName());

        Result result = tx.run("MATCH (u:User)-[b:BE_PART]->(gr:Group)-[r:REFERRED]->(ga:Game)" +
                        "WHERE gr.name=$name and  gr.admin=$admin" +
                        "DELETE b,gr,r"
                , parameters);

        return true;
    }

    /**
     * La funzione aggiunge un membro ad un gruppo
     * @param username
     * @param name
     * @param admin
     * @return true se ha aggiunto correttamente il membro
     * @return false se l'utente era già membro del gruppo
     */


    public static Boolean addGroupMember(final String username, final String name, final String admin) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddGroupMember(tx, username, name, admin);
                }
            });


        }
    }


    /**
     * La funzione aggiunge un membro ad un gruppo
     * @param tx
     * @param username
     * @param name
     * @param admin
     * @return true se ha aggiunto correttamente il membro
     * @return false se l'utente era già membro del gruppo
     */

    private static Boolean transactionAddGroupMember(Transaction tx, String username, String name, String admin) {

        HashMap<String, Object> parameters = new HashMap<>();
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        parameters.put("admin", admin);
        parameters.put("name", name);
        parameters.put("username", username);
        parameters.put("timestamp", ts.toString());

        Result result0 = tx.run("MATCH (u:User{username:$username})-[b:BE_PART]->(gr:Group{name:$name, admin:$admin})" +
                        "RETURN b"
                , parameters);
        if (result0.hasNext()) {
            //System.out.println("Utente già membro del gruppo!");
            return false;
        } else {
            Result result = tx.run("MATCH (u:User{username:$username}),(gr:Group{name:$name, admin:$admin})" +
                            "CREATE (u)-[b:BE_PART {timestamp:$timestamp}]->(gr)" +
                            "RETURN b"
                    , parameters);
            if (result.hasNext()) {
                System.out.println("Ho aggiunto il nuovo gruppo");
                return true;
            }
            return false;
        }

    }

}