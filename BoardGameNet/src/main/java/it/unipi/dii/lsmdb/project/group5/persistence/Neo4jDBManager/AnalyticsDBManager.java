package it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager;

import com.google.common.collect.Lists;
import it.unipi.dii.lsmdb.project.group5.bean.VersatileUser;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsDBManager extends Neo4jDBManager{

    /**
     * La funzione trova gli infuencer che hanno scritto articoli su più categorie diverse
     * o gli utenti standard che hanno scritto recensioni sul maggior numero di categorie diverse
     * @param type normalUser o influencer
     * @return Lista degl username e numero di categorie
     */
    public static List<VersatileUser> mostVersatileUsers(String type) {
        try (Session session = driver.session()) {
            return session.readTransaction(new TransactionWork<List>() {
                @Override
                public List<VersatileUser> execute(Transaction tx) {

                    if(type.equals("influencer")) {
                        return transactionMostVersatileInfluencer(tx);
                    } else {
                        return transactionMostVersatileNormalUser(tx);
                    }
                }
            });

        }
    }
    /**
     * La funzione trova gli influencer che hanno scritto articoli
     * sul maggior numero categorie diverse di giochi
     * nell'ultimo mese
     * @param tx transaction
     * @return Lista degli username degli influencer e il numero di categorie
     */
    private static List<VersatileUser> transactionMostVersatileInfluencer(Transaction tx)
    {

        List<VersatileUser> versatileInfluencer = Lists.newArrayList();

        String query = "MATCH (u:User {role:\"influencer\"})-[:PUBLISHED]->(a:Article)-[:REFERRED]->(g: Game)" +
                " RETURN u.username AS influencer, COUNT(DISTINCT g.category1) AS numeroCategorie" +
                " ORDER BY numeroCategorie DESC " +
                " LIMIT 3";
        Result result = tx.run(query);
        VersatileUser temp;

        while(result.hasNext())
        {
            temp = new VersatileUser();
            Record record = result.next();
            temp.setUsername(record.get("influencer").asString());
            temp.setHowManyCategories(record.get("numeroCategorie").asInt());
            versatileInfluencer.add(temp);
        }

        return versatileInfluencer;

    }

    /**
     * La funzione trova gli utenti standard che hanno scritto reviews
     * sul maggior numero di categorie
     * @param tx transaction
     * @return Lista degli username e il numero di categorie
     */
    private static List<VersatileUser> transactionMostVersatileNormalUser(Transaction tx)
    {
        List<VersatileUser> versatileNormalUsers = new ArrayList<>();
        VersatileUser temp = new VersatileUser();

        String versatileNormalUser = "MATCH (u:User {role:\"normalUser\"})-[:REVIEWED]->(g: Game)" +
                " RETURN u.username AS username, COUNT(DISTINCT g.category1) AS numeroCategorie" +
                " ORDER BY numeroCategorie DESC " +
                " LIMIT 3";

        Result result = tx.run(versatileNormalUser);

        while(result.hasNext())
        {
            Record record = result.next();
            temp.setUsername(record.get("username").asString());
            temp.setHowManyCategories(record.get("numeroCategorie").asInt());
            versatileNormalUsers.add(temp);
        }
        return versatileNormalUsers;
    }
}
