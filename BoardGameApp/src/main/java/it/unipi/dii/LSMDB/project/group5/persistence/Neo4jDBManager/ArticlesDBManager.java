package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;
<<<<<<< HEAD:BoardGameApp/src/main/java/it/unipi/dii/LSMDB/project/group5/persistence/Neo4jDBManager/ListSuggArticlesDBManager.java
=======
import org.openjfx.Entities.ArticleBean;
import org.openjfx.Entities.CommentBean;
>>>>>>> 9e20df63b2c595d303ab41229111dd77b567ad4b:BoardGameApp/src/main/java/it/unipi/dii/LSMDB/project/group5/persistence/Neo4jDBManager/ArticlesDBManager.java

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArticlesDBManager extends Neo4jDBManager {

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
            System.out.println(article.toString());
            articles.add(article);
        }
        return articles;

    }


    /**
     * La funzione aggiunge un nuovo articolo
     * @param newArt
     * @return true se ha aggiunto con successo
     * @return false altrimenti
     */

    public static Boolean addArticle(final ArticleBean newArt) {
        try (Session session = driver.session()) {
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddArticle(tx, newArt);
                }
            });


        }
    }


    /**
     * La funzione aggiunge un commento ad un articolo
     * @param tx
     * @param newArt
     * @return true se ha aggiunto con successo
     * @return false altrimenti
     */

    private static Boolean transactionAddArticle(Transaction tx, ArticleBean newArt) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", newArt.getAuthor());
        parameters.put("timestamp", newArt.getTimestamp().toString());
        parameters.put("title", newArt.getTitle());
        parameters.put("game", newArt.getGame());
        String checkArticle = "MATCH (a:Article{name:$title})<-[p:PUBLISHED]-(u:User{username:$author})" +
                "RETURN a";
        Result result = tx.run(checkArticle, parameters);
        if (result.hasNext()) {
            return false;
        }

        result = tx.run("MATCH(u:User {username:$author}), (g:Game{name:$game})" +
                        "CREATE (u)-[p:PUBLISHED{timestamp:$timestamp}]->(a:Article{name:$title})-[r:REFERRED]->(g) " +
                        "return a"
                , parameters);
        if (result.hasNext()) {
            return true;
        }
        return false;
    }

    /**
     * La funzione elimina un commento ad un articolo
     * @param delComm
     * @return true se ha eliminato correttamente il commento
     * @return false altrimenti
     */
    /*
    public static Boolean deleteComment(final CommentBean delComm) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteComment(tx, delComm);
                }
            });


        }
    }
    */

    /**
     * La funzione elimina un commento ad un articolo
     * @param tx
     * @param delComm
     * @return true se ha eliminato correttamente il commento
     * @return false altrimenti
     */

    private static Boolean transactionDeleteComment(Transaction tx, CommentBean delComm) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("authorComm", delComm.getAuthor());
        parameters.put("timestamp", delComm.getTimestamp().toString());
        parameters.put("title", delComm.getTitleArt());

        Result result = tx.run("MATCH (ua:User {username:$authorComm})-[c:COMMENTED {timestamp:$timestamp}]->(a:Article{name:$title}) " +
                        "DELETE c return c"
                , parameters);

        return true;
    }

}
