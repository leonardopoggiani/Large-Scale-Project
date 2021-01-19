package it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.lsmdb.project.group5.bean.CommentBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CommentsDBManager extends Neo4jDBManager {

    /**
     * La funzione cerca la lista di tutti i commenti ad un articolo
     * @param idArt id articolo
     * @param limit quanti commenti
     * @return Lista dei commenti all'articolo
     */

    public static List<CommentBean> searchListComments(final int idArt, final int limit)
    {

        try
        {
            Session session=driver.session();
            return session.readTransaction(new TransactionWork<List<CommentBean>>()
            {
                @Override
                public List<CommentBean> execute(Transaction tx)
                {
                    return transactionListComments(tx, idArt, limit);
                }
            });
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return  null;
        }
    }

    /**
     * La funzione cerca la lista di tutti i commenti ad un articolo
     * @param tx
     * @param idArt
     * @param limit
     * @return Lista dei commenti all'articolo
     */
    public static List<CommentBean> transactionListComments(Transaction tx, int idArt, int limit) {

        List<CommentBean> infoComments = new ArrayList<>();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", idArt);
        parameters.put("limit", limit);
        Result result = tx.run("MATCH (u:User)-[c:COMMENTED]->(a:Article) WHERE a.idArt=$id RETURN c,u  " +
                " ORDER BY c.timestamp DESC LIMIT $limit", parameters);

        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            CommentBean infoComment = new CommentBean();
            for (Pair<String, Value> nameValue : values) {
                if ("c".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    infoComment.setText(value.get("text").asString());
                    String timestamp = value.get("timestamp").asString();
                    infoComment.setTimestamp(Timestamp.valueOf(timestamp));

                }
                if ("u".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    infoComment.setAuthor(value.get("username").asString());

                }
                if ("a".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    infoComment.setId(value.get("idArt").asInt());

                }

            }

            infoComments.add(infoComment);
        }

        return infoComments;
    }

    /**
     * La funzione conta il numero di commenti ad un articolo
     * @param idArt id dell'articolo
     * @return Numero dei commenti ad un articolo, -1 in caso di errore
     */

    public static int countComments(final int idArt)
    {
        try
        {
            Session session=driver.session();
            return session.readTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountLikes(tx, idArt);
                }
            });
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return  -1;
        }
    }

    /**
     * La funzione conta il numero di commenti ad un articolo
     * @param tx transaction
     * @param idArt id dell'articolo
     * @return Numero dei commenti ad un articolo
     */

    public static int transactionCountLikes(Transaction tx, int idArt) {

        int numberComments = 0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", idArt);
        Result result = tx.run("MATCH (ul:User)-[c:COMMENTED]->(a)" +
                " WHERE a.idArt=$id " +
                " RETURN count(distinct c) AS quantiComments", parameters);

        if (result.hasNext()) {
            Record record = result.next();
            numberComments = record.get("quantiComments").asInt();

        }
        return numberComments;
    }

    /**
     * La funzione aggiunge un commento ad un articolo
     * @param newComm
     * @return true se ha aggiunto con successo, false altrimenti
     */

    public static boolean addComment(final CommentBean newComm) {
        try{
            Session session = driver.session();
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddComment(tx, newComm);
                }
            });


        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return  false;
        }
    }


    /**
     * La funzione aggiunge un commento ad un articolo
     * @param tx
     * @param newComm
     * @return true se ha aggiunto con successo, false altrimenti
     */
    private static boolean transactionAddComment(Transaction tx, CommentBean newComm) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("authorComm", newComm.getAuthor());
        parameters.put("text", newComm.getText());
        parameters.put("id", newComm.getId());
        parameters.put("timestamp", newComm.getTimestamp().toString());


        Result result = tx.run("MATCH(u:User {username:$authorComm}),(a:Article{idArt:$id}) " +
                        " CREATE (u)-[c:COMMENTED{timestamp:$timestamp, text:$text}]->(a) " +
                        " return c"
                , parameters);
        if (result.hasNext()) {
            return true;
        }
        return false;
    }

    /**
     * La funzione elimina un commento ad un articolo
     * @param delComm
     * @return true se ha eliminato correttamente il commento
     * @return false altrimenti
     */
    public static Boolean deleteComment(final CommentBean delComm) {
        try {
            Session session = driver.session();
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteComment(tx, delComm);
                }
            });


        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return  false;
        }
    }


    /**
     * La funzione elimina un commento ad un articolo
     * @param tx
     * @param delComm
     * @return true se ha eliminato correttamente il commento
     * @return false altrimenti
     */

    private static Boolean transactionDeleteComment(Transaction tx, CommentBean delComm) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("authorComm", delComm.getAuthor());
        parameters.put("timestamp", delComm.getTimestamp().toString());
        parameters.put("id", delComm.getId());

        tx.run("MATCH (ua:User {username:$authorComm})-[c:COMMENTED {timestamp:$timestamp}]->(a:Article{idArt:$id}) " +
                        " DELETE c return c"
                , parameters);

        return true;
    }

}
