package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.ReviewBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GamesReviewsRatesDBManager {

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
     * La funzione conta i rates ad un gioco
     * @param name
     * @return Numero dei rates ad un gioco
     */

    public static int countRatings(final String name)
    {
        try(Session session= Neo4jDBManager.driver.session())
        {
            return session.readTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountRatings(tx, name);
                }
            });
        }
    }

    /**
     * La funzione conta i ratings ad un gioco
     * @param tx
     * @param name
     * @return Numero dei ratings ad un gioco
     */

    public static int transactionCountRatings(Transaction tx, String name) {

        int numberRates = 0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        Result result = tx.run("MATCH (ul:User)-[r:RATED]->(g:Game{name:$name}) return count(distinct r) AS quantiRates", parameters);

        if (result.hasNext()) {
            Record record = result.next();
            numberRates = record.get("quantiRates").asInt();

        }
        return numberRates;
    }

    /**
     * La funzione calcola il rating medio di un gioco
     * @param name
     * @return Ratings medio di un gioco
     */

    public static Double avgRatings(final String name)
    {
        try(Session session= Neo4jDBManager.driver.session())
        {
            return session.readTransaction(new TransactionWork<Double>()
            {
                @Override
                public Double execute(Transaction tx)
                {
                    return transactionAvgRatings(tx, name);
                }
            });
        }
    }

    /**
     * La funzione calcola il rating medio di un gioco
     * @param tx
     * @param name
     * @return Ratings medio di un gioco
     */

    public static Double transactionAvgRatings(Transaction tx, String name) {

        Double avgRates = 0.0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        Result result = tx.run("MATCH (ul:User)-[r:RATED]->(g:Game{name:$name}) return avg(r.vote) AS avgRates", parameters);

        if (result.hasNext()) {
            Record record = result.next();

            if(!record.get("avgRates").equals("NULL"))
                avgRates = record.get("avgRates").asDouble();
        }
        return avgRates;
    }
}
