package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.VersatileInfluencerBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsDBManager extends Neo4jDBManager{


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
