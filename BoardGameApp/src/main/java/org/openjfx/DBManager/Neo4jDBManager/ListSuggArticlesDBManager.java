package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.openjfx.Entities.Article;

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

        return null;
    }
}
