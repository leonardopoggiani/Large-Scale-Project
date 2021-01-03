package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;
import org.openjfx.Entities.InfoGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.openjfx.DBManager.Neo4jDBManager.Neo4jDBManager.driver;

public class ListSuggGamesDBManager {

    public static List<InfoGame> searchSuggestedGames(String username)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<List<InfoGame>>()
            {
                @Override
                public List<InfoGame> execute(Transaction tx)
                {
                    return transactionSearchSuggestedGames(tx, username);
                }
            });
        }
    }


    private static List<InfoGame> transactionSearchSuggestedGames(Transaction tx, String username)
    {
        List<InfoGame> infoGames = new ArrayList<>();
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
            InfoGame infoGame =new InfoGame();
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
