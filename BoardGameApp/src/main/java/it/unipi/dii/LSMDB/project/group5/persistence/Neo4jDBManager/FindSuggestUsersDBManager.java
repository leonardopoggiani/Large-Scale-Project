package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindSuggestUsersDBManager extends Neo4jDBManager{

    /** La funzione restituisce la lista degli amici di un utente
     * @param username
     * @return Lista degli username
     */
    public static List<String> listUsersFriends(final String username)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<List>()
            {
                @Override
                public List<String> execute(Transaction tx)
                {
                    return transactionListUsersFriends(tx, username);
                }
            });
        }
    }

    /** La funzione restituisce la lista degli amici di un utente
     * @param tx
     * @param username
     * @return Lista degli username
     */
    private static List<String> transactionListUsersFriends(Transaction tx, String username)
    {
        List<String> friends = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();

        parameters.put("username", username);
        parameters.put("name", "name");
        parameters.put("admin", "admin");
        parameters.put("type", "friend");
        parameters.put("role", "normalUser");
        String searchFriends = "MATCH (u:User{username:$username})<-[f:FOLLOW{type:$type}]->(u2:User{role:$role})" +
                "RETURN u2.username as friend";

        Result result=tx.run(searchFriends, parameters);

        while(result.hasNext())
        {
            Record record = result.next();
            friends.add(record.get("friend").asString());
        }
        return friends;

    }
}
