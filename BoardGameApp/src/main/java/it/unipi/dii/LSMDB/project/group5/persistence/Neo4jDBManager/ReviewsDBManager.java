package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

<<<<<<< HEAD:BoardGameApp/src/main/java/it/unipi/dii/LSMDB/project/group5/persistence/Neo4jDBManager/GamesReviewsRatesDBManager.java
import it.unipi.dii.LSMDB.project.group5.bean.ReviewBean;
import org.neo4j.driver.Record;
=======
>>>>>>> 9e20df63b2c595d303ab41229111dd77b567ad4b:BoardGameApp/src/main/java/it/unipi/dii/LSMDB/project/group5/persistence/Neo4jDBManager/ReviewsDBManager.java
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.util.Pair;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

<<<<<<< HEAD:BoardGameApp/src/main/java/it/unipi/dii/LSMDB/project/group5/persistence/Neo4jDBManager/GamesReviewsRatesDBManager.java
public class GamesReviewsRatesDBManager {
=======
import static org.openjfx.DBManager.Neo4jDBManager.Neo4jDBManager.driver;

public class ReviewsDBManager {
>>>>>>> 9e20df63b2c595d303ab41229111dd77b567ad4b:BoardGameApp/src/main/java/it/unipi/dii/LSMDB/project/group5/persistence/Neo4jDBManager/ReviewsDBManager.java

    /**
     * La funzione restituisce la lista delle reviews ad un gioco
     * @param name
     * @return Lista delle reviews ad un gioco
     */

    public static List<ReviewBean> searchListReviews(final String name)
    {
        try(Session session= Neo4jDBManager.driver.session())
        {
            return session.readTransaction(new TransactionWork<List<ReviewBean>>()
            {
                @Override
                public List<ReviewBean> execute(Transaction tx)
                {
                    return transactionListReviews(tx, name);
                }
            });
        }
    }

    /**
     * La funzione restituisce la lista delle reviews ad un gioco
     * @param tx
     * @param name
     * @return Lista delle reviews ad un gioco
     */

    public static List<ReviewBean> transactionListReviews(Transaction tx, String name) {

        List<ReviewBean> infoReviews = new ArrayList<>();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        Result result = tx.run("MATCH (u:User)-[r:REVIEWED]->(g:Game) WHERE g.name=$name return r,u,g", parameters);

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
        try(Session session= Neo4jDBManager.driver.session())
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
<<<<<<< HEAD:BoardGameApp/src/main/java/it/unipi/dii/LSMDB/project/group5/persistence/Neo4jDBManager/GamesReviewsRatesDBManager.java

    public static int countRatings(final String name)
    {
        try(Session session= Neo4jDBManager.driver.session())
        {
            return session.readTransaction(new TransactionWork<Integer>()
            {
=======
    public static Boolean addReview(final ReviewBean newRev) {
        try (Session session = driver.session()) {
            boolean res;
            return session.writeTransaction(new TransactionWork<Boolean>() {
>>>>>>> 9e20df63b2c595d303ab41229111dd77b567ad4b:BoardGameApp/src/main/java/it/unipi/dii/LSMDB/project/group5/persistence/Neo4jDBManager/ReviewsDBManager.java
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

<<<<<<< HEAD:BoardGameApp/src/main/java/it/unipi/dii/LSMDB/project/group5/persistence/Neo4jDBManager/GamesReviewsRatesDBManager.java
    public static Double avgRatings(final String name)
    {
        try(Session session= Neo4jDBManager.driver.session())
        {
            return session.readTransaction(new TransactionWork<Double>()
            {
=======
    public static Boolean deleteReview(final ReviewBean delRev) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
>>>>>>> 9e20df63b2c595d303ab41229111dd77b567ad4b:BoardGameApp/src/main/java/it/unipi/dii/LSMDB/project/group5/persistence/Neo4jDBManager/ReviewsDBManager.java
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

    private static Boolean transactionDeleteRev(Transaction tx, ReviewBean delRev) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", delRev.getAuthor());
        parameters.put("timestamp", delRev.getTimestamp().toString());
        parameters.put("game", delRev.getGame());

        Result result = tx.run("MATCH (ua:User {username:$author})-[r:REVIEWED {timestamp:$timestamp}]->(g:Game{name:$game}) " +
                        "DELETE r return r"
                , parameters);


        return true;
    }

}
