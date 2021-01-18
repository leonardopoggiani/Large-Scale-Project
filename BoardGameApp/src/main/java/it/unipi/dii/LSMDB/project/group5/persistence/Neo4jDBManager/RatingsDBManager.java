package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.RatingBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.HashMap;


public class RatingsDBManager extends Neo4jDBManager {

    /**
     * La funzione conta i rating ad un gioco
     * @param name
     * @return Numero dei rating ad un gioco
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

    public static double avgRatings(final String name)
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
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  -1;
        }
    }

    /**
     * La funzione calcola il rating medio di un gioco
     * @param tx
     * @param name
     * @return Ratings medio di un gioco
     */

    public static double transactionAvgRatings(Transaction tx, String name) {

        double avgRates = 0.0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        Result result = tx.run("MATCH (ul:User)-[r:RATED]->(g:Game{name:$name}) return coalesce(avg(r.vote), 0) AS avgRates", parameters);

        if (result.hasNext()) {
            Record record = result.next();

            if(!(record.get("avgRates").equals("NULL")))
                avgRates = record.get("avgRates").asDouble();
        }
        return avgRates;
    }

    /**
     * La funzione aggiunge un rating ad un gioco
     * @param newRating
     * @return true se ha aggiunto correttamente il rating
     * @return false altrimenti
     */
    public static boolean addRating(final RatingBean newRating) {
        try (Session session = driver.session()) {
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddRating(tx, newRating);
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
     * La funzione aggiunge un rating ad un gioco
     * @param tx transaction
     * @param newRating nuovo rating
     * @return true se ha aggiunto correttamente il rating
     * @return false altrimenti
     */

    private static boolean transactionAddRating(Transaction tx, RatingBean newRating) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", newRating.getAuthor());
        parameters.put("vote", newRating.getVote());
        parameters.put("timestamp", newRating.getTimestamp().toString());
        parameters.put("game", newRating.getGame());

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
