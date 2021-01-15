package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.sql.Timestamp;
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
        String searchFollowingOnly ="MATCH (u:User{username:$username})-[f:FOLLOW]->(u2:User) " +
                                     " WHERE NOT (u2)-[:FOLLOW]->(u) " +
                                     " RETURN u2.username as followingOnly ";

        String searchFollowingAll ="MATCH (u:User{username:$username})-[f:FOLLOW]->(u2:User) " +
                " RETURN u2.username as followingAll";

        String searchFollowersOnly ="MATCH (u:User{username:$username})<-[f:FOLLOW]-(u2:User) " +
                                     " WHERE NOT (u2)<-[:FOLLOW]-(u) " +
                                     " RETURN u2.username as followersOnly ";

        String searchFollowersAll ="MATCH (u:User{username:$username})<-[f:FOLLOW]-(u2:User) " +
                " RETURN u2.username as followersAll";
        //Doppio follow
        String searchFriends =  "MATCH (u:User{username:$username})<-[f:FOLLOW]-(u2:User) " +
                                " WHERE (u2)<-[:FOLLOW]-(u) " +
                                " RETURN u2.username as friends ";

        String searchAllDebug =  "MATCH (u:User) " + " RETURN u.username as users LIMIT 10";

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
            Result result=tx.run(searchFollowingAll, parameters);
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
        } else if(type.equals("all")) {
            Result result=tx.run(searchAllDebug, parameters);
            while(result.hasNext())
            {
                Record record = result.next();
                users.add(record.get("users").asString());
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
    public static List<String> listSuggestedFollowing(final String username, final String role)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<List>()
            {
                @Override
                public List<String> execute(Transaction tx)
                {
                    return transactionListSuggestedFollowing(tx, username, role);
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
    private static List<String> transactionListSuggestedFollowing(Transaction tx, String username, String role)
    {
        List<String> suggestion = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();
        Result result;
        parameters.put("username", username);
        parameters.put("role", role);

        /*String searchFriends = "MATCH (u:User{username:$username})-[f:FOLLOW]->(u2:User)" +
                "WHERE (u2)-[:FOLLOW]->(u)" +
                "RETURN count(u2) AS quantiAmici";*/

        String searchForFriendsInflu = "MATCH (me:User{username:$username})-[:FOLLOW]->(friend:User), (friend)-[:FOLLOW]->(me), (tizio:User{role:$role})" +
                " WHERE NOT((me)-[:FOLLOW]->(tizio)) AND (friend)-[:FOLLOW]->(tizio) AND NOT tizio.username=$username RETURN tizio.username AS suggestion";

        String searchForArticlesFollowersInflu = "MATCH (u:User)-[f:FOLLOW]->(u2:User{role:$role})-[p:PUBLISHED]->(a:Article)" +
                " RETURN u2.username AS suggestion ,COUNT(f) AS quantiFollowers, COUNT(p) AS quantiArticoli ORDER BY quantiFollowers DESC, quantiArticoli DESC LIMIT 4";

        String searchForFriendsNormal = "MATCH (me:User{username:$username})-[:FOLLOW]->(friend:User), (friend)-[:FOLLOW]->(me), (tizio:User{role:$role})" +
                " WHERE NOT((me)-[:FOLLOW]->(tizio)) AND (tizio)-[:FOLLOW]->(friend) AND (friend)-[:FOLLOW]->(tizio) AND NOT tizio.username=$username RETURN tizio.username AS suggestion";

        String searchForCategoryNormal = "MATCH (ub:User{username:$username}),(ua:User{role:$role})" +
                " WHERE (ub.category1=ua.category1 or ub.category1=ua.category2)" +
                " or (ub.category2=ua.category1 or ub.category2=ua.category2)" +
                " AND NOT(ua.username=$username)" +
                " RETURN ua.username AS suggestion";

        int quantiAmici = transactionCountUsers(tx,username, "normalUser");
        System.out.println(quantiAmici);
        if(quantiAmici > 3)
        {

            if(role.equals("normalUser"))
            {
                result = tx.run(searchForFriendsNormal, parameters);
                System.out.println("uso searchForFriendsNormal ");
            }
            else
            {
                result = tx.run(searchForFriendsInflu, parameters);
                System.out.println("uso searchForFriendsInflu ");
            }

        }
        else
        {
            if(role.equals("normalUser"))
            {
                result = tx.run(searchForArticlesFollowersInflu, parameters);
                System.out.println("uso searchForArticlesFollowersInflu ");
            }
            else
            {
                result = tx.run(searchForCategoryNormal, parameters);
                System.out.println("uso searchForCategoryNormal ");
            }


        }

        while(result.hasNext())
        {
            Record record = result.next();
            suggestion.add(record.get("suggestion").asString());
        }
        return suggestion;

    }

    /** La funzione conta gli amici o gli influencer seguiti in base al parametro type
     * La funzione viene utilizzata per il suggerimento degli articoli e per il suggerimento
     * degli utenti.
     * @param tx
     * @param username
     * @param type
     * @return numero di amici o numero di influencer seguiti
     */

    public static int transactionCountUsers(Transaction tx, String username, String type) {

        HashMap<String, Object> parameters = new HashMap<>();
        Result result;
        int quanti = 0;
        parameters.put("username", username);
        String countFriends ="MATCH (u:User{username:$username})-[f:FOLLOW]->(u2:User)" +
                            " WHERE (u2)-[:FOLLOW]->(u)" +
                            " RETURN count(u2) AS quanti";

        String countInfluencers = "MATCH (u:User{username:$username})-[f:FOLLOW]->(u2:User{role:\"influencer\"})" +
                                " RETURN count(u2) AS quanti";

        if(type.equals("influencer"))
        {
            result = tx.run(countInfluencers, parameters);
        }
        else
        {
            result = tx.run(countFriends, parameters);
        }
        while (result.hasNext()) {
            Record record = result.next();
            quanti = record.get("quanti").asInt();
        }

        return quanti;
    }

    /**
     * La funzione aggiunge un commento ad un articolo
     * @param username1
     * @param username2
     * @return true se ha aggiunto o rimosso con successo
     * @return false altrimenti
     */

    public static Boolean addRemoveFollow(final String username1, final String username2, final String type) {
        try (Session session = driver.session()) {
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    if(type.equals("add"))
                        return transactionAddFollow(tx, username1, username2);
                    else
                        return transactionRemoveFollow(tx, username1, username2);
                }
            });


        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return false;
        }

    }


    /**
     * La funzione aggiunge un follow tra due utenti
     * @param tx
     * @param username1
     * @param username2
     * @return true se ha aggiunto con successo
     * @return false altrimenti
     */
    private static Boolean transactionAddFollow(Transaction tx, String username1, String username2) {
        HashMap<String, Object> parameters = new HashMap<>();
        Timestamp current = new Timestamp(System.currentTimeMillis());
        parameters.put("username1", username1);
        parameters.put("username2", username2);
        parameters.put("timestamp", current.toString());
        boolean existsFollow = transactionExistsFollow(tx,username1,username2);
        if(!existsFollow)
        {
            System.out.println("Non esiste aggiungo!");
            tx.run("MATCH(u1:User {username:$username1}),(u2:User {username:$username2})" +
                            " CREATE (u1)-[f:FOLLOW{timestamp:$timestamp}]->(u2) " +
                            " return f"
                    , parameters);

        }

        return true;
    }

    /**
     * La funzione aggiunge un follow tra due utenti
     * @param tx
     * @param username1
     * @param username2
     * @return true se ha eliminato con successo
     * @return false altrimenti
     */

    private static Boolean transactionRemoveFollow(Transaction tx, String username1, String username2) {
        HashMap<String, Object> parameters = new HashMap<>();
        Timestamp current = new Timestamp(System.currentTimeMillis());
        parameters.put("username1", username1);
        parameters.put("username2", username2);
        parameters.put("timestamp", current.toString());
        boolean existsFollow = transactionExistsFollow(tx,username1,username2);
        if(existsFollow)
        {
            System.out.println("Esiste elimino!");
            tx.run("MATCH(u1:User {username:$username1})-[f:FOLLOW]->(u2:User {username:$username2})" +
                            " DELETE f RETURN f"
                    , parameters);
            //System.out.println("Ho eliminato!");
            return true;
        }
        //System.out.println("Non posso eliminare!");
        return false;
    }

    /**
     * La funzione controlla se esiste o no un follow tra due utenti
     * @param tx
     * @param username1
     * @param username2
     * @return true se esiste
     * @return false altrimenti
     */
    private static Boolean transactionExistsFollow(Transaction tx, String username1, String username2) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("username1", username1);
        parameters.put("username2", username2);

        Result result = tx.run("MATCH (u:User{username:$username1})-[f:FOLLOW]->(u2:User{username:$username2})" +
                " RETURN f",parameters);

        if (result.hasNext()) {
            return true;
        }
        return false;
    }

    //TODO
    //Credo sia sbagliata ci devo guardare meglio
    /**
     * La funzione elimina uno user
     * @param username dell'utente
     * @return true se ha eliminato con successo, false altrimenti
     */

    public static boolean deleteUser(final String username) {
        try(Session session = driver.session())
        {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteUser(tx, username);
                }
            });


        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return false;
        }
    }


    /**
     * La funzione elimina uno user
     * @param tx transaction
     * @param username del gioco
     * @return true se ha eliminato correttamente il gioco
     * @return false altrimenti
     */

    private static boolean transactionDeleteUser(Transaction tx, String username) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("username", username);

        String eliminaReviews = "MATCH (u:User{username:$username})-[r:REVIEWED]->(g) " +
                " DELETE r";
        String eliminaRatings = "MATCH (u:User{username:$username})-[r:RATED]->(g)" +
                " DELETE r";
        String eliminaPosts = "(u:User{username:$username})-[p:POST]->(gr:Group)" +
                " DELETE p";
        String eliminaBepartGroup = "MATCH (u:User{username:$username})-[b:BE_PART]->(gr:Group{admin:$username})" +
                " DELETE b,gr";
        String eliminaPublished = "MATCH (u:User{username:$username})-[p:PUBLISHED]->(a)" +
                " DELETE p";

        String eliminaGame = "(u:User{username:$username}) " +
                " DELETE u";
        Result result = tx.run(eliminaReviews, parameters);
        result = tx.run(eliminaRatings, parameters);
        result = tx.run(eliminaPosts, parameters);
        result = tx.run(eliminaBepartGroup, parameters);
        result = tx.run(eliminaPublished, parameters);


        return true;
    }

    /**
     * La funzione aggiunge promuove o declassa un utente
     * @param username1
     * @param username2
     * @return true se ha modificato con successo, false altrimenti
     */

    public static boolean promoteDemoteUser(final String username1, final String username2, final String role) {
        try (Session session = driver.session()) {
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) { return transactionPromoteDemoteUser(tx, username1, role);
                }
            });


        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return false;
        }
    }


    /**
     * La funzione aggiunge promuove o declassa un utente
     * @param username
     * @param tx
     * @return true se ha modificato con successo, false altrimenti
     */
    private static boolean transactionPromoteDemoteUser(Transaction tx, String username, String role) {
        HashMap<String, Object> parameters = new HashMap<>();
        Timestamp current = new Timestamp(System.currentTimeMillis());
        parameters.put("username", username);
        parameters.put("role", role);


        Result result = tx.run("MATCH(u1:User {username:$username})" +
                        " SET u1.role:$role" +
                        " return u1.role AS newRole"
                        , parameters);

        if(result.hasNext())
        {
            Record record = result.next();
            if(record.get("newRole").asString().equals(role))
                return  true;
        }


        return false;
    }

}
