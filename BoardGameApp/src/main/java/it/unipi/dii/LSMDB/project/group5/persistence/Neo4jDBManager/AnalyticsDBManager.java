package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.StatisticsInfluencer;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AnalyticsDBManager extends Neo4jDBManager{


    /**
     * La funzione trova gli influencer con il maggior numero di likes o dislikes
     * @param type [like-dislike]
     * @return lista dei 3 influencer
     */
    public static List<StatisticsInfluencer> top3InfluLikes(String type) {
        try (Session session = driver.session()) {
            return session.readTransaction(new TransactionWork<List>() {
                @Override
                public List<StatisticsInfluencer> execute(Transaction tx) {

                    return transactionTop3InfluLikes(tx, type);
                }
            });


        }
    }
    /**
     * La funzione trova gli influencer con il maggior numero di likes o dislikes
     * @param type
     * @param tx
     * @return lista dei 3 influencer
     */
    private static List<StatisticsInfluencer> transactionTop3InfluLikes(Transaction tx, String type)
    {
        List<StatisticsInfluencer> top = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("type", type);
        StatisticsInfluencer temp = new StatisticsInfluencer();
        String query = "MATCH (u:User)-[:PUBLISHED]->(a:Article)<-[r:LIKED]-(u1:User)" +
                "WHERE r.type =$type RETURN u.username AS influencer, count(r) as countLike ORDER BY countLike desc LIMIT 3";
        Result result = tx.run(query,parameters);

        while(result.hasNext())
        {
            Record record = result.next();
            temp.setUsername(record.get("influencer").asString());
            temp.setHowManyLikes(record.get("countLike").asInt());
            temp.setType(type);
            top.add(temp);
            System.out.println(temp.toString());
        }

        return top;

    }


    /**
     * La funzione trova gli influencer che hanno scritto articoli su meno di 10
     * giochi nell'ultimo periodo, in ordine crescente
     * @param datePar data di partenza del periodo
     * @return Lista degli username degli influencer e il numero di giochi
     */
    public static List<StatisticsInfluencer> worstInfluencers(String datePar) {
        try (Session session = driver.session()) {
            return session.readTransaction(new TransactionWork<List>() {
                @Override
                public List<StatisticsInfluencer> execute(Transaction tx) {

                    return transactionWorstInfluencers(tx, datePar);
                }
            });


        }
    }
    /**
     * La funzione trova gli influencer che hanno scritto articoli su meno di 10
     * giochi nell'ultimo periodo, in ordine crescente
     * @param datePar data di partenza del periodo
     * @param tx
     * @return Lista degli username degli influencer e il numero di giochi
     */
    private static List<StatisticsInfluencer> transactionWorstInfluencers(Transaction tx, String datePar)
    {
        List<StatisticsInfluencer> versatileInfluencer = new ArrayList<>();
        StatisticsInfluencer temp = new StatisticsInfluencer();
        HashMap<String,Object> parameters = new HashMap<>();
        Date date = new Date();
        Timestamp today = new Timestamp(date.getTime());
        String todayString = today.toString();
        parameters.put("datePar", datePar);
        parameters.put("today", todayString);
        System.out.println(todayString);
        System.out.println(datePar);


        String query = "MATCH (u:User)-[p:PUBLISHED]->(a:Article)-[r:REFERRED]->(g:Game)" +
                "WHERE u.role=\"influencer\"" +
                "AND p.timestamp >= $datePar AND p.timestamp <= $today WITH u, count (distinct g) AS countGames WHERE countGames <10 RETURN u.username AS username, countGames ORDER BY countGames ASC";
        Result result = tx.run(query,parameters);

        while(result.hasNext())
        {
            Record record = result.next();
            temp.setUsername(record.get("username").asString());
            System.out.println(record.get("username").asString());
            temp.setHowManyGames(record.get("countGames").asInt());
            System.out.println(temp.toString());
            versatileInfluencer.add(temp);
        }


        System.out.println(versatileInfluencer);
        return versatileInfluencer;

    }

    /**
     * La funzione trova gli influencer che hanno scritto articoli sul maggior numero di categorie
     * nell'ultimo mese
     * @return Lista degli username degli influencer e il numero di categorie
     */
    public static List<StatisticsInfluencer> versatileInfluencers(String datePar) {
        try (Session session = driver.session()) {
            return session.readTransaction(new TransactionWork<List>() {
                @Override
                public List<StatisticsInfluencer> execute(Transaction tx) {

                    return transactionVersatileInfluencers(tx, datePar);
                }
            });


        }
    }
    /**
     * La funzione trova gli influencer che hanno scritto articoli sul maggior numero di giochi
     * nell'ultimo mese
     * @param tx
     * @return Lista degli username degli influencer e il numero di categorie
     */
    private static List<StatisticsInfluencer> transactionVersatileInfluencers(Transaction tx, String datePar)
    {
        List<StatisticsInfluencer> versatileInfluencer = new ArrayList<>();
        StatisticsInfluencer temp = new StatisticsInfluencer();
        /*HashMap<String,Object> parameters = new HashMap<>();
        Date date = new Date();
        Timestamp today = new Timestamp(date.getTime());
        String todayString = today.toString();
        parameters.put("datePar", datePar);
        parameters.put("today", todayString);
        System.out.println(todayString);
        System.out.println(datePar);*/


        String query = "MATCH (u:User)-[p:PUBLISHED]->(a:Article)-[r:REFERRED]->(g:Game)" +
                "WHERE u.role=\"influencer\"" +
                "AND p.timestamp >= $datePar AND p.timestamp <= $today WITH u, count (distinct g) AS countGames WHERE countGames <10 RETURN u.username AS username, countGames ORDER BY countGames ASC";
        /*Result result = tx.run(query,parameters);

        while(result.hasNext())
        {
            Record record = result.next();
            temp.setUsername(record.get("username").asString());
            System.out.println(record.get("username").asString());
            temp.setHowManyGames(record.get("countGames").asInt());
            System.out.println(temp.toString());
            versatileInfluencer.add(temp);
        }
*/

        System.out.println(versatileInfluencer);
        return versatileInfluencer;

    }



}
