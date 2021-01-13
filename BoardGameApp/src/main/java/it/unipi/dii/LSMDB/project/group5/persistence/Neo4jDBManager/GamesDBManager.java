package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class    GamesDBManager extends Neo4jDBManager{

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
                    //infoGame.setNumVotes(RatingsDBManager.countRatings(name));
                    //infoGame.setAvgRating(RatingsDBManager.avgRatings(name));

                }

            }
            //infoGame.setGroups(GamesReviewsRatesDBManager.searchListGroups(name));


            infoGames.add(infoGame);
        }

        GameBean game1 = new GameBean("Un gioco", 8.5,120);
        GameBean game2 = new GameBean("Un gioco bello", 5.5,120);
        GameBean game3 = new GameBean("Un gioco brutto", 2.5,110);
        GameBean game4 = new GameBean("Un gioco normale", 8.5,2);
        GameBean game5 = new GameBean("Un gioco favoloso", 9.5,12);
        GameBean game6 = new GameBean("Un gioco meraviglioso", 8.5,10);
        GameBean game7 = new GameBean("Un gioco altro", 4.5,50);

        infoGames.add(game1);
        infoGames.add(game2);
        infoGames.add(game3);
        infoGames.add(game4);
        infoGames.add(game5);
        infoGames.add(game6);
        infoGames.add(game7);

        return infoGames;

    }
}
