package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.openjfx.Entities.*;

import java.sql.Timestamp;
import java.util.HashMap;

public class UpdateDatabaseDBManager extends Neo4jDBManager {
    //DA FINIRE

    public static Boolean addComment(InfoComment newComm) {
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


    public static Integer addLike(InfoLike like) {
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

    public static Boolean deleteComment(InfoComment newComm) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteComment(tx, newComm);
                }
            });


        }
    }

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

    public static Boolean addReview(InfoReview newRev) {
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

    public static Boolean deleteReview(InfoReview delRev) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteRev(tx, delRev);
                }
            });


        }
    }

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


    public static Boolean addRate(InfoRate newRate) {
        try (Session session = driver.session()) {
            boolean res;
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddRate(tx, newRate);
                }
            });


        }
    }

    private static Boolean transactionAddRate(Transaction tx, InfoRate newRate) {
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


    public static Boolean addGroup(InfoGroup newGroup) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddGroup(tx, newGroup);
                }
            });


        }
    }

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


    public static Boolean deleteGroup(InfoGroup delGroup) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteGroup(tx, delGroup);
                }
            });


        }
    }

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

    //add member to a group

    public static Boolean addGroupMember(String username, String name, String admin) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddGroupMember(tx, username, name, admin);
                }
            });


        }
    }

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