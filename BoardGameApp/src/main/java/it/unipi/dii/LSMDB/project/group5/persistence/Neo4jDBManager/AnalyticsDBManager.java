package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.VersatileUser;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsDBManager extends Neo4jDBManager{

    /*
    /**
     * La funzione trova gli influencer con il maggior numero di likes o dislikes
     * @param type [like-dislike]
     * @return lista dei 3 influencer
     */
    /*
    public static List<VersatileUser> top3InfluLikes(String type) {
        try (Session session = driver.session()) {
            return session.readTransaction(new TransactionWork<List>() {
                @Override
                public List<VersatileUser> execute(Transaction tx) {

                    return transactionTop3InfluLikes(tx, type);
                }
            });


        }
    }*/
    /*/**
     * La funzione trova gli influencer con il maggior numero di likes o dislikes
     * @param type
     * @param tx
     * @return lista dei 3 influencer
     */
    /*
    private static List<VersatileUser> transactionTop3InfluLikes(Transaction tx, String type)
    {
        List<VersatileUser> top = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("type", type);
        VersatileUser temp = new VersatileUser();
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
    /*
    public static List<VersatileUser> worstInfluencers(String datePar) {
        try (Session session = driver.session()) {
            return session.readTransaction(new TransactionWork<List>() {
                @Override
                public List<VersatileUser> execute(Transaction tx) {

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
    /*
    private static List<VersatileUser> transactionWorstInfluencers(Transaction tx, String datePar)
    {
        List<VersatileUser> versatileInfluencer = new ArrayList<>();
        VersatileUser temp = new VersatileUser();
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
    */
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

                    if(type.equals("influencer"))
                        return transactionMostVersatileInfluencer(tx);
                    else
                        return transactionMostVersatileNormalUser(tx);
                }
            });


        }
    }
    /**
     * La funzione trova gli influencer che hanno scritto articoli
     * sul maggior numero categorie diverse di giochi
     * nell'ultimo mese
     * @param tx
     * @return Lista degli username degli influencer e il numero di categorie
     */
    private static List<VersatileUser> transactionMostVersatileInfluencer(Transaction tx)
    {
        List<VersatileUser> versatileInfluencer = new ArrayList<>();

        /*HashMap<String,Object> parameters = new HashMap<>();
        Date date = new Date();
        Timestamp today = new Timestamp(date.getTime());
        String todayString = today.toString();
        parameters.put("datePar", datePar);
        parameters.put("today", todayString);
        System.out.println(todayString);
        System.out.println(datePar);*/


        String query = "MATCH (u:User {role:\"influencer\"})-[:PUBLISHED]->(a:Article)-[:REFERRED]->(g: Game)" +
                " RETURN u.username AS influencer, COUNT(DISTINCT g.category1) AS numeroCategorie" +
                " ORDER BY numeroCategorie DESC " +
                " LIMIT 3";
        Result result = tx.run(query);

        while(result.hasNext())
        {
            VersatileUser temp = new VersatileUser();
            Record record = result.next();
            temp.setUsername(record.get("influencer").asString());
            temp.setHowManyCategories(record.get("numeroCategorie").asInt());

            System.out.println(temp.getUsername());
            System.out.println(temp.getHowManyCategories());

            System.out.println(temp.toString());
            versatileInfluencer.add(temp);
        }

        System.out.println(versatileInfluencer.size());
        System.out.println(versatileInfluencer.get(0).toString());
        System.out.println(versatileInfluencer.get(1).toString());
        return versatileInfluencer;

    }

    /**
     * La funzione trova gli utenti standard che hanno scritto reviews
     * sul maggior numero di categorie
     * @param tx
     * @return Lista degli username e il numero di categorie
     */
    private static List<VersatileUser> transactionMostVersatileNormalUser(Transaction tx)
    {
        List<VersatileUser> versatileNormalUsers = new ArrayList<>();
        VersatileUser temp = new VersatileUser();
        /*HashMap<String,Object> parameters = new HashMap<>();
        Date date = new Date();
        Timestamp today = new Timestamp(date.getTime());
        String todayString = today.toString();
        parameters.put("datePar", datePar);
        parameters.put("today", todayString);
        System.out.println(todayString);
        System.out.println(datePar);*/


        String versatileNormalUser = "MATCH (u:User {role:\"normalUser\"})-[:REVIEWED]->(g: Game)" +
                " RETURN u.username AS username, COUNT(DISTINCT g.category1) AS numeroCategorie" +
                " ORDER BY numeroCategorie DESC " +
                " LIMIT 3";

        Result result = tx.run(versatileNormalUser);

        while(result.hasNext())
        {
            Record record = result.next();
            temp.setUsername(record.get("username").asString());
            System.out.println(record.get("username").asString());
            temp.setHowManyCategories(record.get("numeroCategorie").asInt());
            System.out.println(temp.toString());
            versatileNormalUsers.add(temp);
        }


        System.out.println(versatileNormalUsers);
        return versatileNormalUsers;

    }
}
