package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;
import org.openjfx.Entities.InfoReview;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.openjfx.DBManager.Neo4jDBManager.Neo4jDBManager.driver;

public class GamesReviewsRatesDBManager {

    public static List<InfoReview> searchListReviews(String name)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<List<InfoReview>>()
            {
                @Override
                public List<InfoReview> execute(Transaction tx)
                {
                    return transactionListReviews(tx, name);
                }
            });
        }
    }

    public static List<InfoReview> transactionListReviews(Transaction tx, String name) {

        List<InfoReview> infoReviews = new ArrayList<>();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        Result result = tx.run("MATCH (u:User)-[r:REVIEWED]->(g:Game) WHERE g.name=$name return r,u,g", parameters);

        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            InfoReview infoReview = new InfoReview();
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

    public static int countReviews(String name)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountReviews(tx, name);
                }
            });
        }
    }

    //Funzione che conta le reviews ad un determinato gioco

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

    public static int countRates(String name)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountRates(tx, name);
                }
            });
        }
    }

    //Funzione che conta i rates ad un determinato gioco

    public static int transactionCountRates(Transaction tx, String name) {

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

    public static Double avgRates(String name)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<Double>()
            {
                @Override
                public Double execute(Transaction tx)
                {
                    return transactionAvgRates(tx, name);
                }
            });
        }
    }

    // Funzione che fa la media delle valutazioni ad un determinato gioco

    public static Double transactionAvgRates(Transaction tx, String name) {

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
