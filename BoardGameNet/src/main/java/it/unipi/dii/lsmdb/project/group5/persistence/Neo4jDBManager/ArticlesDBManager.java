package it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager;

import com.google.common.collect.Lists;
import it.unipi.dii.lsmdb.project.group5.App;
import it.unipi.dii.lsmdb.project.group5.bean.ArticleBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArticlesDBManager extends Neo4jDBManager {


    /** La funzione restituisce la lista degli articoli suggeriti nella home di un utente
     * Se un utente segue degli influencer mostra gli articoli di essi, altrimenti quelli suggeriti
     * in base alle sue categorie preferite
     * @param username username utente
     * @param limit
     * @return Lista degli articoli suggeriti
     */
    public static List<ArticleBean> searchSuggestedArticles(final String username, final int limit)
    {
        try(Session session=driver.session())
        {

            return session.readTransaction(new TransactionWork<List>()
            {
                @Override
                public List<ArticleBean> execute(Transaction tx)
                {
                    List<ArticleBean> ret = Lists.newArrayList();
                    try {
                        ret = transactionSearchSuggestedArticles(tx, username, limit);
                    } catch(Exception e) {
                        System.err.println("error neo4j");
                        App.getScene().getWindow().hide();
                    }
                    return ret;
                }
            });
        }
    }

    /**
     * La funzione restituisce la lista degli articoli suggeriti nella home di un utente
     * Se un utente segue degli influencer mostra gli articoli di esse, altrimenti quelli suggeriti
     * in base alle sue categorie preferite
     * @param tx transaction
     * @param username username utente
     * @param limit
     * @return Lista degli articoli suggeriti
     */
    private static List<ArticleBean> transactionSearchSuggestedArticles(Transaction tx, String username, int limit)
    {
        List<ArticleBean> articles = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();
        int quantiInflu = 0;
        parameters.put("username", username);
        parameters.put("role", "influencer");
        parameters.put("limit", limit);

        String conInflu = "MATCH (u:User{username:$username})-[f:FOLLOW]->(i:User{role:$role})-[p:PUBLISHED]-(a:Article) " +
                " RETURN a, i, p ORDER BY p.timestamp LIMIT $limit";

        String nienteInflu = "MATCH (i:User)-[p:PUBLISHED]->(a:Article)-[r:REFERRED]->(g:Game), (u:User) " +
                " WHERE NOT(i.username = u.username) AND " +
                " u.username=$username AND ((g.category1 = u.category1 OR g.category1 = u.category2) " +
                " OR (g.category2 = u.category1 OR g.category2 = u.category2))" +
                " RETURN distinct(a),i,p ORDER BY p.timestamp LIMIT $limit ";

        Result result;
        quantiInflu = UsersDBManager.transactionCountUsers(tx,username,"influencer");
        if(quantiInflu < 3)
        {
            result = tx.run(nienteInflu, parameters);
        }
        else
        {
            result = tx.run(conInflu, parameters);
        }
        while(result.hasNext())
        {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            ArticleBean article = new ArticleBean();
            String author;
            String title;
            for (Pair<String,Value> nameValue: values) {
                if ("a".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    title = value.get("title").asString();
                    article.setTitle(title);
                    article.setId(value.get("idArt").asInt());

                }
                if ("i".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    author = value.get("username").asString();
                    article.setAuthor(author);

                }
                if ("p".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    String timestamp = value.get("timestamp").asString();
                    article.setTimestamp(Timestamp.valueOf(timestamp));

                }
            }
            articles.add(article);
        }

        return articles;

    }


    /**
     * La funzione aggiunge un nuovo articolo
     * @param newArt articolo da aggiungere
     * @return true se ha aggiunto con successo, false altrimenti
     */

    public static boolean addArticle(final ArticleBean newArt) {
        try  {
            Session session = driver.session();
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddArticle(tx, newArt);
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
     * La funzione aggiunge un nuovo articolo
     * @param tx transaction
     * @param newArt articolo da aggiungere
     * @return true se ha aggiunto con successo, false altrimenti
     */

    private static boolean transactionAddArticle(Transaction tx, ArticleBean newArt) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", newArt.getAuthor());
        parameters.put("id", newArt.getId());
        parameters.put("timestamp", newArt.getTimestamp().toString());
        parameters.put("title", newArt.getTitle());
        parameters.put("game1", newArt.getListGame().get(0));
        parameters.put("game2", (newArt.getListGame().size() == 2) ? newArt.getListGame().get(1) : "" );
        String query = "";

        if (newArt.getListGame().size() == 1 || newArt.getListGame().get(1).equals("")) {
            query =
                "MATCH(u:User {username:$author}), (g1:Game{name:$game1})"
                    + " CREATE (u)-[p:PUBLISHED{timestamp:$timestamp}]->(a:Article{idArt:$id, title:$title})"
                    + " CREATE (g1)<-[:REFERRED]-(a)"
                    + " return a ";
        } else {
            query =
                "MATCH(u:User {username:$author}), (g1:Game{name:$game1}), (g2:Game{name:$game2}) "
                    + " CREATE (u)-[p:PUBLISHED{timestamp:$timestamp}]->(a:Article{idArt:$id, title:$title}) "
                    + " CREATE (g1)<-[:REFERRED]-(a)-[:REFERRED]->(g2) "
                    + " return a ";
        }


        Result result = tx.run(query, parameters);
        if(result.hasNext()) {
            return true;
        }

        return false;
    }

    /**
     * La funzione elimina un articolo
     * @param idArt dell' articolo
     * @return true se ha eliminato correttamente l'articolo
     * @return false altrimenti
     */

    public static boolean deleteArticle(final int idArt) {
        try {
            Session session = driver.session();
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteArticle(tx, idArt);
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
     * La funzione elimina un articolo
     * @param tx transaction
     * @param idArt id articolo
     * @return true se ha eliminato correttamente l'articolo
     * @return false altrimenti
     */

    private static boolean transactionDeleteArticle(Transaction tx, int idArt) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", idArt);

        tx.run("MATCH (a:Article{idArt:$id})" +
                        " DETACH DELETE a "
                , parameters);

        return true;
    }

}
