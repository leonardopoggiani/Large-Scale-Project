package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.ReviewBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReviewsDBManager extends Neo4jDBManager {

    /**
     * La funzione restituisce la lista delle reviews ad un gioco
     * @param name
     * @return Lista delle reviews ad un gioco
     */

    public static List<ReviewBean> searchListReviews(final String name, final int quante)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<List<ReviewBean>>()
            {
                @Override
                public List<ReviewBean> execute(Transaction tx)
                {
                    return transactionListReviews(tx,name, quante);
                }
            });
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  null;
        }
    }

    /**
     * La funzione restituisce la lista delle reviews ad un gioco
     * @param tx
     * @param name
     * @return Lista delle reviews ad un gioco
     */

    public static List<ReviewBean> transactionListReviews(Transaction tx, String name, int quante) {

        List<ReviewBean> infoReviews = new ArrayList<>();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("quante", quante);
        parameters.put("name", name);
        Result result = tx.run("MATCH (u:User)-[r:REVIEWED]->(g:Game) WHERE g.name=$name return r,u,g ORDER BY r.timestamp DESC LIMIT $quante", parameters);

        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            ReviewBean infoReview = new ReviewBean();
            for (Pair<String, Value> nameValue : values) {
                if ("r".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    infoReview.setText(value.get("text").asString());
                    String timestamp = value.get("timestamp").asString();
                    infoReview.setTimestamp(Timestamp.valueOf(timestamp));

                }
                if ("u".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    infoReview.setAuthor(value.get("username").asString());

                }

            }

            infoReview.setGame(name);
            System.out.println(infoReviews);
            infoReviews.add(infoReview);
        }

        return infoReviews;
    }

    /**
     * La funzione conta le reviews ad un gioco
     * @param name
     * @return Numero delle reviews ad un gioco
     */

    public static int countReviews(final String name)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountReviews(tx, name);
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
     * La funzione conta le reviews ad un gioco
     * @param tx
     * @param name
     * @return Numero delle reviews ad un gioco
     */

    public static int transactionCountReviews(Transaction tx, String name) {

        int numberReviews = 0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        Result result = tx.run("MATCH (u:User)-[r:REVIEWED]->(g:Game{name:$name}) return count(distinct r) AS quanteReviews", parameters);

        if (result.hasNext()) {
            org.neo4j.driver.Record record = result.next();
            numberReviews = record.get("quanteReviews").asInt();

        }
        return numberReviews;
    }

    /**
     * La funzione aggiunge una review ad un gioco
     * @param newRev
     * @return true se ha aggiunto correttamente la review
     * @return false altrimenti
     */
    public static Boolean addReview(final ReviewBean newRev) {
        try (Session session = driver.session()) {
            boolean res;
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddReview(tx, newRev);
                }
            });


        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  false;
        }
    }

    /**
     * La funzione aggiunge una review ad un gioco
     * @param tx
     * @param newRev
     * @return true se ha aggiunto correttamente la review
     * @return false altrimenti
     */

    private static Boolean transactionAddReview(Transaction tx, ReviewBean newRev) {
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

    public static boolean deleteReview(final ReviewBean delRev) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteRev(tx, delRev);
                }
            });


        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  false;
        }
    }


    /**
     * La funzione elimina una review ad un gioco
     * @param tx
     * @param delRev
     * @return true se ha eliminato correttamente la review
     * @return false altrimenti
     */

    private static Boolean transactionDeleteRev(Transaction tx, ReviewBean delRev) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", delRev.getAuthor());
        parameters.put("timestamp", delRev.getTimestamp().toString());
        parameters.put("game", delRev.getGame());

        tx.run("MATCH (ua:User {username:$author})-[r:REVIEWED {timestamp:$timestamp}]->(g:Game{name:$game}) " +
                        "DELETE r return r"
                , parameters);


        return true;
    }

}
