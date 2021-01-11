package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.RateBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.HashMap;


public class RatingsDBManager extends Neo4jDBManager {

    /**
     * La funzione conta i rates ad un gioco
     * @param name
     * @return Numero dei rates ad un gioco
     */

    public static int countRatings(final String name)
    {
        try(Session session=driver.session())
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
            org.neo4j.driver.Record record = result.next();
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
        try(Session session=driver.session())
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

    /**
     * La funzione aggiunge un rating ad un gioco
     * @param newRate
     * @return true se ha aggiunto correttamente il rating
     * @return false altrimenti
     */
    public static Boolean addRating(final RateBean newRate) {
        try (Session session = driver.session()) {
            boolean res;
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddRating(tx, newRate);
                }
            });


        }
    }

    /**
     * La funzione aggiunge un rating ad un gioco
     * @param tx
     * @param newRate
     * @return true se ha aggiunto correttamente il rating
     * @return false altrimenti
     */

    private static Boolean transactionAddRating(Transaction tx, RateBean newRate) {
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

}
