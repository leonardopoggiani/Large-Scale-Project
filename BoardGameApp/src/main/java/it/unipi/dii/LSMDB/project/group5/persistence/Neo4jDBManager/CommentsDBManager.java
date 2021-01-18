package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.CommentBean;
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
     * @param author
     * @param title
     * @param limit
     * @return Lista dei commenti all'articolo
     */

    public static List<CommentBean> searchListComments(final String title, final String author, int limit)
    {

        try
        {
            Session session=driver.session();
            return session.readTransaction(new TransactionWork<List<CommentBean>>()
            {
                @Override
                public List<CommentBean> execute(Transaction tx)
                {
                    return transactionListComments(tx, title, author, limit);
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
     * @param tx
     * @param author
     * @param title
     * @return Lista dei commenti all'articolo
     */
    public static List<CommentBean> transactionListComments(Transaction tx, String title, String author, int limit) {

        List<CommentBean> infoComments = new ArrayList<>();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", author);
        parameters.put("title", title);
        parameters.put("limit", limit);
        Result result = tx.run("MATCH (u:User)-[c:COMMENTED]->(a:Article)<-[p:PUBLISHED]-(au:User) WHERE au.username=$author AND a.title=$title RETURN c,u  " +
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

            }
            infoComment.setAuthorArt(author);
            infoComment.setTitleArt(title);

            infoComments.add(infoComment);
        }

        return infoComments;
    }

    /**
     * La funzione conta il numero di commenti ad un articolo
     * @param title
     * @param author
     * @return Numero dei commenti ad un articolo, -1 in caso di errore
     */

    public static int countComments(String title, String author)
    {
        try
        {
            Session session=driver.session();
            return session.readTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountLikes(tx, title, author);
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
     * @param tx
     * @param title
     * @param author
     * @return Numero dei commenti ad un articolo
     */

    public static int transactionCountLikes(Transaction tx, String title, String author) {

        int numberComments = 0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", author);
        parameters.put("title", title);
        Result result = tx.run("MATCH (ul:User)-[c:COMMENTED]->(a),(i:User)-[p:PUBLISHED]->(a) WHERE a.title=$title AND i.username=$author return count(distinct c) AS quantiComments", parameters);

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
        parameters.put("timestamp", newComm.getTimestamp().toString());
        parameters.put("authorArt", newComm.getAuthorArt());
        parameters.put("title", newComm.getTitleArt());

        Result result = tx.run("MATCH(u:User {username:$authorComm}),(ua:User {username:$authorArt})-[:PUBLISHED]->(a:Article{title:$title}) " +
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
        parameters.put("title", delComm.getTitleArt());

        tx.run("MATCH (ua:User {username:$authorComm})-[c:COMMENTED {timestamp:$timestamp}]->(a:Article{title:$title}) " +
                        " DELETE c return c"
                , parameters);

        return true;
    }

}
