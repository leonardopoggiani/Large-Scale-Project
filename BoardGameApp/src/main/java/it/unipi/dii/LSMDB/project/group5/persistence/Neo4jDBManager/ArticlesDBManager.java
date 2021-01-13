package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class ArticlesDBManager extends Neo4jDBManager {


     Logger logger = Logger.getLogger(this.getClass().getName());
    /** La funzione restituisce la lista degli articoli suggeriti nella home di un utente
     * Se un utente segue degli influencer mostra gli articoli di esse, altrimenti quelli suggeriti
     * in base alle sue categorie preferite, ma solo 4
     * se esso non ha amici, in base alle alle categorie che ha specificato come preferite
     * @param username username utente
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
     * @param tx transaction
     * @param username username utente
     * @return Lista degli articoli suggeriti
     */
    private static List<ArticleBean> transactionSearchSuggestedArticles(Transaction tx, String username)
    {
        List<ArticleBean> articles = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();
        int quantiInflu= 0;
        parameters.put("username", username);
        parameters.put("role", "influencer");
        /*String searchInfluencers = "MATCH (u:User{username:$username})-[f:FOLLOW]->(u2:User{role:$role})" +
                "RETURN f";*/
        String conAmici = "MATCH (u:User{username:$username})-[f:FOLLOW]->(i:User{role:$role})-[p:PUBLISHED]-(a:Article)" +
                "RETURN a, i, p ORDER BY p.timestamp";

        String nienteAmici = "MATCH (i:User)-[p:PUBLISHED]->(a:Article)-[r:REFERRED]->(g:Game),(u:User)" +
                "WHERE u.username=$username AND ((g.category1 = u.category1 OR g.category1 = u.category2)" +
                "OR (g.category2 = u.category1 OR g.category2 = u.category2))" +
                "RETURN distinct(a),i,p ORDER BY p.timestamp LIMIT 6";

        Result result;
        quantiInflu = UsersDBManager.transactionCountUsers(tx,username,"influencer");
        if(quantiInflu < 0)
        {
            result = tx.run(nienteAmici, parameters);
            System.out.println("Pochi Influencer");
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
            String author;
            String title;
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

            System.out.println("NELLA DBMANAGER");
            articles.add(article);
        }

        ArticleBean a = new ArticleBean("Un articolo","leonardo",new Timestamp(System.currentTimeMillis()),"Spirit island");
        ArticleBean b = new ArticleBean("Ammazza che articolone","leonardo",new Timestamp(System.currentTimeMillis()),"Spirit island");
        ArticleBean c = new ArticleBean("Non c'entra niente","francesca",new Timestamp(System.currentTimeMillis()),"Spirit island");
        ArticleBean d = new ArticleBean("Un articolo3","leonardo",new Timestamp(System.currentTimeMillis()),"Spirit island");
        ArticleBean e = new ArticleBean("Un articolo4","leonardo",new Timestamp(System.currentTimeMillis()),"Spirit island");
        ArticleBean f = new ArticleBean("Un articolo5","leonardo",new Timestamp(System.currentTimeMillis()),"Spirit island");

        articles.add(a);
        articles.add(b);
        articles.add(c);
        articles.add(d);
        articles.add(e);
        articles.add(f);

        return articles;

    }


    /**
     * La funzione aggiunge un nuovo articolo
     * @param newArt
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
     * @param newArt articolo
     * @return true se ha aggiunto con successo, false altrimenti
     */

    private static boolean transactionAddArticle(Transaction tx, ArticleBean newArt) {
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
     * La funzione elimina un articolo
     * @param author autore articolo
     * @param title titol articolo
     * @return true se ha eliminato correttamente l'articolo
     * @return false altrimenti
     */

    public static boolean deleteArticle(final String author, final String title) {
        try {
            Session session = driver.session();
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteArticle(tx, author, title);
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
     * @param author autore articolo
     * @param title titolo articolo
     * @return true se ha eliminato correttamente l'articolo
     * @return false altrimenti
     */

    private static boolean transactionDeleteArticle(Transaction tx, String author, String title) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", author);
        parameters.put("title", title);

        tx.run("MATCH (ua:User {username:$author})-[p:PUBLISHED]->(a:Article{name:$title})-[r:REFERRED]-(g:Game) " +
                        "DELETE p,a,r "
                , parameters);

        return true;
    }

}
