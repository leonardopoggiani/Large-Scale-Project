package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;
import org.openjfx.Entities.ArticleBean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListSuggArticlesDBManager extends Neo4jDBManager {

    /** La funzione restituisce la lista degli articoli suggeriti nella home di un utente
     * Se un utente segue degli influencer mostra gli articoli di esse, altrimenti quelli suggeriti
     * in base alle sue categorie preferite, ma solo 4
     * se esso non ha amici, in base alle alle categorie che ha specificato come preferite
     * @param username
     * @return Lista degli articoli suggeriti
     */
    public static List<ArticleBean> searchSuggestedArticles(final String username)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<List>()
            {
                @Override
                public List<ArticleBean> execute(Transaction tx)
                {
                    return transactionSearchSuggestedArticles(tx, username);
                }
            });
        }
    }

    /**
     * La funzione restituisce la lista degli articoli suggeriti nella home di un utente
     * Se un utente segue degli influencer mostra gli articoli di esse, altrimenti quelli suggeriti
     * in base alle sue categorie preferite, ma solo 4
     * se esso non ha amici, in base alle alle categorie che ha specificato come preferite
     * @param tx
     * @param username
     * @return Lista degli articoli suggeriti
     */
    private static List<ArticleBean> transactionSearchSuggestedArticles(Transaction tx, String username)
    {
        List<ArticleBean> articles = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();
        Boolean findInflu = false;
        parameters.put("username", username);
        parameters.put("type", "follow");
        parameters.put("role", "influencer");
        String searchInfluencers = "MATCH (u:User{username:$username})-[f:FOLLOW{type:$type}]->(u2:User{role:$role})" +
                "RETURN f";
        String conAmici = "MATCH (u:User{username:$username})-[f:FOLLOW{type:$type}]->(i:User{role:$role})-[p:PUBLISHED]-(a:Article)" +
                "RETURN a, i, p ORDER BY p.timestamp";

        String nienteAmici = "MATCH (i:User)-[p:PUBLISHED]->(a:Article)-[r:REFERRED]->(g:Game),(u:User)" +
                "WHERE u.username=$username AND ((g.category1 = u.category1 OR g.category1 = u.category2)" +
                "OR (g.category2 = u.category1 OR g.category2 = u.category2))" +
                "RETURN a,i,p ORDER BY p.timestamp LIMIT 4";

        Result result=tx.run(searchInfluencers, parameters);
        if(!result.hasNext())
        {
            result = tx.run(nienteAmici, parameters);
            System.out.println("Non ho trovato Influencers");
        }
        else
        {
            result = tx.run(conAmici, parameters);
            System.out.println("Ho trovato Influencers");
        }
        while(result.hasNext())
        {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            ArticleBean article = new ArticleBean();
            String author = "";
            String title = "";
            for (Pair<String,Value> nameValue: values) {
                if ("a".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    title = value.get("name").asString();
                    article.setTitle(title);

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
}
