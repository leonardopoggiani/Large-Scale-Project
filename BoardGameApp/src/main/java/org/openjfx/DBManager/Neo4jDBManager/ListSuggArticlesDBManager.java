package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;
import org.openjfx.Entities.Article;

import java.util.ArrayList;
import java.util.HashMap;
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
        List<Article> articles = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("username", username);
        Result result=tx.run("MATCH (i:User)-[p:PUBLISHED]->(a:Article)-[r:REFERRED]->(g:Game),(u:User)" +
                "WHERE u.username=$username AND ((g.category1 = u.category1 OR g.category1 = u.category2)" +
                "OR (g.category2 = u.category1 OR g.category2 = u.category2))" +
                "RETURN a,i,p ORDER BY p.timestamp", parameters);

        while(result.hasNext())
        {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            Article article=new Article();
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
                    String timestamp = value.get("timestamp").toString();
                    article.setTimestamp(timestamp);

                }


            }
            //article.setComments(ArticlesCommentsLikesDBManager.searchListComments(title, author));


            articles.add(article);
        }
            return articles;
       /* Article a1 = new Article("Ottimo articolo","Leonardo Poggiani",new Timestamp(System.currentTimeMillis()));
        Article a2 = new Article("Bell'articolo","Lorenzo Poggiani",new Timestamp(System.currentTimeMillis()));
        Article a3 = new Article("Un articolo","Marco Poggiani",new Timestamp(System.currentTimeMillis()));
        Article a4 = new Article("Altro articolo","Gaia Anastasi",new Timestamp(System.currentTimeMillis()));
        Article a5 = new Article("Davvero un altro articolo?","Clarissa Polidori",new Timestamp(System.currentTimeMillis()));
        Article a6 = new Article("L'articolo","Francesca Tonioni",new Timestamp(System.currentTimeMillis()));

        ArrayList<Article> list = Lists.newArrayList(a1,a2,a3,a4,a5,a6);
        return list;*/
    }
}
