package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.StatisticsInfluencer;
import it.unipi.dii.LSMDB.project.group5.bean.VersatileInfluencerBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalyticsDBManager extends Neo4jDBManager{


   /* match (u:User)-[:PUBLISHED]->(a:Article)<-[r:LIKED]-(u1:User)
    where r.status = "like"
            return u.name, count(r) as likeCount
    order by likeCount desc
    limit 3

    // per dislike
    match (u:User)-[:PUBLISHED]->(a:Article)<-[r:LIKED]-(u1:User)
    where r.status = "dislike"
            return u.name, count(r) as likeCount
    order by likeCount desc
    limit 3*/

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

    //TODO
    /**
     * La funzione trova gli influencer che hanno scritto articoli sul maggior numero di giochi
     * nell'ultimo mese
     * @return Lista degli username degli influencer e il numero di categorie
     */
    public static List<VersatileInfluencerBean> versatileInfluencers() {
        try (Session session = driver.session()) {
            return session.readTransaction(new TransactionWork<List>() {
                @Override
                public List<VersatileInfluencerBean> execute(Transaction tx) {

                    return transactionVersatileInfluencers(tx);
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
    private static List<VersatileInfluencerBean> transactionVersatileInfluencers(Transaction tx)
    {
        List<VersatileInfluencerBean> versatileInfluencer = new ArrayList<>();
        VersatileInfluencerBean temp = new VersatileInfluencerBean();
        String query = "MATCH (u:User)-[p:PUBLISHED]->(a:Article)-[r:REFERRED]->(g:Game)" +
                "WHERE u.role=\"influencer\"" +
                "AND p.timestamp >= datetime({year:2020, month:1, day:1})" +
                "AND p.timestamp <= datetime({year:2020, month:12, day: 31})" +
                "WITH u, count (distinct g) AS countGames" +
                "WHERE countGames <10" +
                "RETURN u.username AS username, countGames" +
                "ORDER BY countGames ASC";
        Result result = tx.run(query);

        while(result.hasNext())
        {
            Record record = result.next();
            temp.setUsername(record.get("username").asString());
            temp.setNumGames(record.get("countGames").asInt());
            versatileInfluencer.add(temp);
        }



        return versatileInfluencer;

    }



}
