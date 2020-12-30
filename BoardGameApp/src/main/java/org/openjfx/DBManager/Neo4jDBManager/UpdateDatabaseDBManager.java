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
        parameters.put("timestamp", newComm.getTimestamp());
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
        parameters.put("timestamp", like.getTimestamp());
        parameters.put("authorArt", like.getAuthorArt());
        parameters.put("title", like.getTitleArt());

        Result result=tx.run("MATCH(u:User {username:$authorLike}),(ua:User {username:$authorArt})-[:PUBLISHED]->(a:Article{name:$title}) " +
                        "CREATE (u)-[l:LIKED{timestamp:$timestamp, type:$type}]->(a) " +
                        "return l"
                ,parameters);
        if(result.hasNext())
        {
            return true;
        }
        return false;
    }

}
