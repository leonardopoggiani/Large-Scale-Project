package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.openjfx.Entities.LikeBean;

import java.util.HashMap;

import static org.openjfx.DBManager.Neo4jDBManager.Neo4jDBManager.driver;

public class LikesDBManager {

    /**
     * La funzione conta il numero di like or dislike ad un articolo
     * @param title
     * @param author
     * @param type
     * @return Numero dei like, se type=like
     * @return Numero di dislike se type= dislike

     */
    public static int countLikes(String title, String author, String type)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountLikes(tx, title, author, type);
                }
            });
        }
    }

    /**
     * La funzione conta il numero di like or dislike ad un articolo
     * @param tx
     * @param title
     * @param author
     * @param type
     * @return Numero dei like, se type=like
     * @return Numero di dislike se type= dislike

     */

    public static int transactionCountLikes(Transaction tx, String title, String author, String type) {

        int numberLike = 0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("type", type);
        parameters.put("author", author);
        parameters.put("title", title);
        Result result = tx.run("MATCH (ul:User)-[l:LIKED{type:$type}]->(a),(i:User)-[p:PUBLISHED]->(a) WHERE a.name=$title AND i.username=$author return count(distinct l) AS quantiLike", parameters);

        if (result.hasNext()) {
            Record record = result.next();
            numberLike = record.get("quantiLike").asInt();

        }
        return numberLike;
    }


    /**
     * La funzione aggiunge un like or dislike ad un articolo
     * @param like
     * @return 2 se ha aggiunto un like(dislike)
     * @return 1 se ha eliminato un like(dislike)
     * @return 0 altrimenti
     */

    public static Integer addLike(final LikeBean like) {
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
    private static Integer transactionAddLike(Transaction tx, LikeBean like) {

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



}
