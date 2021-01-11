package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersDBManager extends Neo4jDBManager{

    /** La funzione restituisce la lista dei followers, o following o friends in base al parametro type
     * @param type (following, followers, friends)
     * @param username
     * @return Lista degli username
     */
    public static List<String> listUsers(final String username, String type)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<List>()
            {
                @Override
                public List<String> execute(Transaction tx)
                {
                    return transactionListUsers(tx, username, type);
                }
            });
        }
    }

    /** La funzione restituisce la lista dei followers, o following o friends in base al parametro type
     * @param type (following, followers, friends)
     * @param tx
     * @param username
     * @return Lista degli username
     */
    private static List<String> transactionListUsers(Transaction tx, String username, String type)
    {
        List<String> users = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();

        parameters.put("username", username);
        //Tutti quelli che un utente segue
        String searchFollowingOnly ="MATCH (u:User{username:$username})-[f:FOLLOW]->(u2:User)" +
                                     "WHERE NOT (u2)-[:FOLLOW]->(u)" +
                                     "RETURN u2.username as followingOnly";

        String searchFollowingAll ="MATCH (u:User{username:$username})-[f:FOLLOW]->(u2:User)" +
                "RETURN u2.username as followingAll";

        String searchFollowersOnly ="MATCH (u:User{username:$username})<-[f:FOLLOW]-(u2:User)" +
                                     "WHERE NOT (u2)<-[:FOLLOW]-(u)" +
                                     "RETURN u2.username as followersOnly";

        String searchFollowersAll ="MATCH (u:User{username:$username})<-[f:FOLLOW]-(u2:User)" +
                "RETURN u2.username as followersAll";
        //Doppio follow
        String searchFriends =  "MATCH (u:User{username:$username})<-[f:FOLLOW]-(u2:User)" +
                                "WHERE (u2)<-[:FOLLOW]-(u)" +
                                "RETURN u2.username as friends";

        if(type.equals("friends"))
        {
            Result result=tx.run(searchFriends, parameters);
            while(result.hasNext())
            {
                Record record = result.next();
                users.add(record.get("friends").asString());
            }
        }
        else if(type.equals("followingOnly"))
        {
            Result result=tx.run(searchFollowingOnly, parameters);
            while(result.hasNext())
            {
                Record record = result.next();
                users.add(record.get("followingOnly").asString());
            }
        }
        else if(type.equals("followingAll"))
        {
            Result result=tx.run(searchFollowingOnly, parameters);
            while(result.hasNext())
            {
                Record record = result.next();
                users.add(record.get("followingAll").asString());
            }
        }
        else if(type.equals("followersOnly"))
        {
            Result result=tx.run(searchFollowersOnly, parameters);
            while(result.hasNext())
            {
                Record record = result.next();
                users.add(record.get("followersOnly").asString());
            }
        }
        else if(type.equals("followersAll"))
        {
            Result result=tx.run(searchFollowersAll, parameters);
            while(result.hasNext())
            {
                Record record = result.next();
                users.add(record.get("followersAll").asString());
            }
        }


        return users;

    }



    /** La funzione restituisce la lista following suggeriti
     * Se l'utente ha degli amici allora suggerisce gli utenti seguiti dal maggior numero
     * di suoi amici, altrimenti suggerisce gli utenti con almeno una delle sue stesse categorie
     * preferite.
     * @param username
     * @return Lista degli username dei following suggeriti
     */
    public static List<String> listSuggestedFollowing(final String username)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<List>()
            {
                @Override
                public List<String> execute(Transaction tx)
                {
                    return transactionListSuggestedFollowing(tx, username);
                }
            });
        }
    }

    /** La funzione restituisce la lista following suggeriti
     * Se l'utente ha più di 5 amici allora suggerisce gli utenti amici dei suoi amici ma che lui non segue ancora,
     * altrimenti suggerisce gli utenti con almeno una delle sue stesse categorie
     * preferite.
     * @param tx
     * @param username
     * @return Lista degli username dei following suggeriti
     */
    private static List<String> transactionListSuggestedFollowing(Transaction tx, String username)
    {
        List<String> suggestion = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();

        parameters.put("username", username);
        String searchFriends = "MATCH (u:User{username:$username})-[f:FOLLOW]->(u2:User{role:\"normalUser\"})\n" +
                "WHERE (u2)-[:FOLLOW]->(u)" +
                "RETURN count(u2) AS quantiAmici";
        String searchForFriends = "MATCH (me:User{username:$username})-[:FOLLOW]->(friend:User), (friend)-[:FOLLOW]-(me), (tizio:User{role:\"normalUser\"})" +
                "WHERE NOT((me)-[:FOLLOW]->(tizio)) AND (tizio)-[:FOLLOW]->(friend) AND (friend)-[:FOLLOW]->(tizio) AND NOT tizio.username=$username RETURN tizio.username AS suggestion";
        String searchForCategory = "MATCH (ub:User{username:$username}),(ua:User)" +
                "WHERE (ub.category1=ua.category1 or ub.category1=ua.category2)" +
                "or (ub.category2=ua.category1 or ub.category2=ua.category2)" +
                "AND NOT(ua.username=$username)" +
                "RETURN ua.username AS suggestion";

        Result result=tx.run(searchFriends, parameters);
        int quantiAmici = result.next().get("quantiAmici").asInt();
        System.out.println(quantiAmici);
        if(quantiAmici > 0)
        {
            result = tx.run(searchForFriends, parameters);
        }
        else
        {
            result = tx.run(searchForCategory, parameters);
        }

        while(result.hasNext())
        {
            Record record = result.next();
            suggestion.add(record.get("suggestion").asString());
        }
        return suggestion;

    }
}
