package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;
import org.openjfx.Entities.GameBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.openjfx.DBManager.Neo4jDBManager.Neo4jDBManager.driver;

public class ListSuggGamesDBManager {

    /**
     * La funzione restituisce la lista dei giochi suggeriti nella sezione giochi di un utente
     * in base alle sue categorie preferite
     * @param username
     * @return Lista degli articoli suggeriti
     */

    public static List<GameBean> searchSuggestedGames(final String username)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<List<GameBean>>()
            {
                @Override
                public List<GameBean> execute(Transaction tx)
                {
                    return transactionSearchSuggestedGames(tx, username);
                }
            });
        }
    }

    /**
     * La funzione restituisce la lista dei giochi suggeriti nella sezione giochi di un utente
     * in base alle sue categorie preferite
     * @param tx
     * @param username
     * @return Lista degli articoli suggeriti
     */
    private static List<GameBean> transactionSearchSuggestedGames(Transaction tx, String username)
    {
        List<GameBean> infoGames = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("username", username);
        Result result=tx.run("MATCH (g:Game),(u:User)" +
                "WHERE u.username=$username AND ((g.category1 = u.category1 OR g.category1 = u.category2)" +
                "OR (g.category2 = u.category1 OR g.category2 = u.category2))" +
                "RETURN g,u", parameters);

        while(result.hasNext())
        {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            GameBean infoGame =new GameBean();
            String name ="";
            for (Pair<String,Value> nameValue: values) {
                if ("g".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    name = value.get("name").asString();
                    infoGame.setName(name);
                    infoGame.setCategory1(value.get("category1").asString());
                    infoGame.setCategory2(value.get("category2").asString());

                }

            }
            //infoGame.setGroups(GamesReviewsRatesDBManager.searchListGroups(name));


            infoGames.add(infoGame);
        }
        return infoGames;

    }
}
