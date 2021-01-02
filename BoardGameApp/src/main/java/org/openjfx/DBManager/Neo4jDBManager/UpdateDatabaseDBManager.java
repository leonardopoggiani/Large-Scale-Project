package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.openjfx.Entities.InfoComment;
import org.openjfx.Entities.InfoLike;

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
    private static Boolean transactionAddComment(Transaction tx, InfoComment newComm)
    {
        HashMap<String,Object> parameters= new HashMap<>();
        parameters.put("authorComm",newComm.getAuthor());
        parameters.put("text", newComm.getText());
        parameters.put("timestamp", newComm.getTimestamp().toString());
        parameters.put("authorArt", newComm.getAuthorArt());
        parameters.put("title", newComm.getTitleArt());

        Result result=tx.run("MATCH(u:User {username:$authorComm}),(ua:User {username:$authorArt})-[:PUBLISHED]->(a:Article{name:$title}) " +
                        "CREATE (u)-[c:COMMENTED{timestamp:$timestamp, text:$text}]->(a) " +
                        "return c"
                ,parameters);
        if(result.hasNext())
        {
            return true;
        }
        return false;
    }

    public static Boolean addLike(InfoLike like) {
        try (Session session = driver.session()) {
            boolean res;
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddLike(tx, like);
                }
            });


        }
    }
    private static Boolean transactionAddLike(Transaction tx, InfoLike like)
    {

        HashMap<String,Object> parameters= new HashMap<>();
        parameters.put("authorLike",like.getAuthor());
        parameters.put("type", like.getType());
        parameters.put("timestamp", like.getTimestamp().toString());
        parameters.put("authorArt", like.getAuthorArt());
        parameters.put("title", like.getTitleArt());

        Result result0 = tx.run("MATCH (ua:User {username:$authorArt})-[:PUBLISHED]->(a:Article{name:$title})<-[l:LIKED{type:$type}]-(u:User{username:$authorLike}) return l"
                ,parameters);

        if(result0.hasNext()) {
            System.out.println("Trovato sto per eliminare");
            Result result1 = tx.run("MATCH (a:Article{name:$title})<-[l:LIKED{type:$type}]-(u:User{username:$authorLike}) delete l"
                    ,parameters);
                System.out.println("Ho eliminato");
                return true;

        }
        else {

            Result result = tx.run("MATCH(u:User {username:$authorLike}),(ua:User {username:$authorArt})-[:PUBLISHED]->(a:Article{name:$title}) " +
                            "CREATE (u)-[l:LIKED{timestamp:$timestamp, type:$type}]->(a) " +
                            "return l"
                    , parameters);
            if(result.hasNext())
            {
                System.out.println("Ho aggiunto");
                return true;
            }
            return false;
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

}
