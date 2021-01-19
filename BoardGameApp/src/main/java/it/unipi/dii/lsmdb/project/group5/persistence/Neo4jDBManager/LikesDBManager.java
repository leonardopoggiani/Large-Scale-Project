package it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.lsmdb.project.group5.bean.LikeBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.HashMap;


public class LikesDBManager extends Neo4jDBManager {

    /**
     * La funzione conta il numero di like or dislike ad un articolo
     * @param idArt
     * @param type
     * @return Numero dei like, se type=like
     * @return Numero di dislike se type= dislike

     */
    public static int countLikes(String type, int idArt)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountLikes(tx, type, idArt);
                }
            });
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  -1;
        }
    }

    /**
     * La funzione conta il numero di like or dislike ad un articolo
     * @param tx
     * @param idArt
     * @return Numero dei like, se type=like
     * @return Numero di dislike se type= dislike

     */

    public static int transactionCountLikes(Transaction tx, String type, int idArt) {

        int numberLike = 0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", idArt);
        parameters.put("type", type);
        Result result = tx.run("MATCH (ul:User)-[l:LIKED{type:$type}]->(a:Article) " +
                " WHERE a.idArt=$id return count(distinct l) AS quantiLike", parameters);

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

    public static int addLike(final LikeBean like) {
        try (Session session = driver.session()) {
            return session.writeTransaction(new TransactionWork<Integer>() {
                @Override
                public Integer execute(Transaction tx) {
                    return transactionAddLike(tx, like);
                }
            });


        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  -1;
        }
    }

    /**
     * La funzione aggiunge un like or dislike ad un articolo
     * @param tx
     * @param like
     * @return 1 se ha aggiunto un like(dislike)
     * @return 0 se ha eliminato un like(dislike)
     * @return -1 altrimenti
     */
    private static int transactionAddLike(Transaction tx, LikeBean like) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("authorLike", like.getAuthor());
        parameters.put("type", like.getType());
        parameters.put("timestamp", like.getTimestamp().toString());
        parameters.put("id", like.getId());

        Result result = tx.run("MATCH (a:Article{idArt:$id})<-[l:LIKED{type:$type}]-(u:User{username:$authorLike}) return l"
                , parameters);

        if (result.hasNext()) {

            tx.run("MATCH (a:Article{idArt:$id})<-[l:LIKED{type:$type}]-(u:User{username:$authorLike}) delete l"
                    , parameters);

            return 1;

        } else {

            result = tx.run("MATCH (u:User {username:$authorLike}), (a:Article{idArt:$id}) " +
                            " CREATE (u)-[l:LIKED{timestamp:$timestamp, type:$type}]->(a) " +
                            " return l"
                    , parameters);
            if (result.hasNext()) {
                return 2;
            }
            return 0;
        }


    }



}
