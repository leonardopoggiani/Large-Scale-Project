package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.openjfx.Entities.Article;
import com.google.common.collect.Lists;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ListSuggArticlesDBManager extends Neo4jDBManager {

    public static List<Article> searchSuggestedArticles(String username)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<List>()
            {
                @Override
                public List<Article> execute(Transaction tx)
                {
                    return transactionSearchSuggestedArticles(tx, username);
                }
            });
        }
    }

    private static List<Article> transactionSearchSuggestedArticles(Transaction tx, String username)
    {
        Article a1 = new Article("Ottimo articolo","Leonardo Poggiani",new Timestamp(System.currentTimeMillis()));
        Article a2 = new Article("Bell'articolo","Lorenzo Poggiani",new Timestamp(System.currentTimeMillis()));
        Article a3 = new Article("Un articolo","Marco Poggiani",new Timestamp(System.currentTimeMillis()));
        Article a4 = new Article("Altro articolo","Gaia Anastasi",new Timestamp(System.currentTimeMillis()));
        Article a5 = new Article("Davvero un altro articolo?","Clarissa Polidori",new Timestamp(System.currentTimeMillis()));
        Article a6 = new Article("L'articolo","Francesca Tonioni",new Timestamp(System.currentTimeMillis()));

        ArrayList<Article> list = Lists.newArrayList(a1,a2,a3,a4,a5,a6);
        return list;
    }
}
