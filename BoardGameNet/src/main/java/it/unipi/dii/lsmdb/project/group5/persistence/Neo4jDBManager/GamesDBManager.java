package it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.lsmdb.project.group5.bean.ArticleBean;
import it.unipi.dii.lsmdb.project.group5.bean.GameBean;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.GameDBManager;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class  GamesDBManager extends Neo4jDBManager{

    /**
     * La funzione restituisce la lista dei giochi suggeriti nella sezione giochi di un utente
     * in base alle sue categorie preferite
     * @param username
     * @return Lista degli articoli suggeriti
     */

    public static List<GameBean> searchSuggestedGames(final String username, final int limit)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<List<GameBean>>()
            {
                @Override
                public List<GameBean> execute(Transaction tx)
                {
                    return transactionSearchSuggestedGames(tx, username, limit);
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
    private static List<GameBean> transactionSearchSuggestedGames(Transaction tx, String username, int limit)
    {
        List<GameBean> infoGames = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("limit", limit);
        Result result=tx.run("MATCH (g:Game),(u:User) " +
                " WHERE u.username=$username AND ((g.category1 = u.category1 OR g.category1 = u.category2) " +
                " OR (g.category2 = u.category1 OR g.category2 = u.category2)) " +
                " RETURN g,u LIMIT $limit ", parameters);

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
                    infoGame.setNumVotes(GameDBManager.getNumVotes(name));
                    infoGame.setAvgRating(GameDBManager.getAvgRating(name));

                }
            }
            infoGames.add(infoGame);
        }

        return infoGames;

    }


    /**
     * La funzione aggiunge un nuovo gioco
     * @param newGame
     * @return true se ha aggiunto con successo, false altrimenti
     */

    public static boolean addGame(final GameBean newGame) {
        try  {
            Session session = driver.session();
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddGame(tx, newGame);
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
     * La funzione aggiunge un nuovo gioco
     * @param tx transaction
     * @param newGame gioco
     * @return true se ha aggiunto con successo, false altrimenti
     */

    private static boolean transactionAddGame(Transaction tx, GameBean newGame) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", newGame.getName());
        if(!newGame.getCategory1().equals("")) {
            parameters.put("category1", newGame.getCategory1() );

            String checkGame = "MATCH (g:Game{name:$name})" +
                    " RETURN g";

            Result result = tx.run(checkGame, parameters);

            if (result.hasNext()) {
                return false;
            }

            result = tx.run("CREATE (g:Game{name:$name, category1:$category1})"
                    , parameters);

            return true;
        }

        return false;
    }

    /**
     * La funzione elimina un gioco
     * @param name del  gioco
     * @return true se ha eliminato con successo, false altrimenti
     */

    public static boolean deleteGame(final String name) {
        try {
            Session session = driver.session();
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteGame(tx, name);
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
     * La funzione elimina un gioco
     * @param tx transaction
     * @param name del gioco
     * @return true se ha eliminato correttamente il gioco
     * @return false altrimenti
     */

    private static boolean transactionDeleteGame(Transaction tx, String name) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        List<ArticleBean> articleDelete = new ArrayList<>();

        //Elimino il gruppo, con tutti i suoi post, be_part, referred
        String eliminaGroups = "MATCH (gr:Group)-[:REFERRED]->(g:Game{name:$name})" +
                " DETACH DELETE gr";
        //Elimino il gioco e tutto quello che è collegato ad esso
        String eliminaTutto = "MATCH (g:Game{name:$name}) " +
                " DETACH DELETE g";

        tx.run(eliminaGroups, parameters);
        tx.run(eliminaTutto, parameters);

        return true;
    }

}
