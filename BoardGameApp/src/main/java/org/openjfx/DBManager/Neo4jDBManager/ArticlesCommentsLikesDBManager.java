package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;
import org.openjfx.Entities.InfoComment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArticlesCommentsLikesDBManager extends Neo4jDBManager{

    /**
     * La funzione cerca la lista di tutti i commenti ad un articolo
     * @param author
     * @param title
     * @return Lista dei commenti all'articolo
     */

    public static List<InfoComment> searchListComments(String title, String author)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<List<InfoComment>>()
            {
                @Override
                public List<InfoComment> execute(Transaction tx)
                {
                    return transactionListComments(tx, title, author);
                }
            });
        }
    }

    /**
     * @param tx
     * @param author
     * @param title
     * @return Lista dei commenti all'articolo
     */
    public static List<InfoComment> transactionListComments(Transaction tx, String title, String author) {

        List<InfoComment> infoComments = new ArrayList<>();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", author);
        parameters.put("title", title);
        Result result = tx.run("MATCH (u:User)-[c:COMMENTED]->(a:Article)<-[p:PUBLISHED]-(au:User) WHERE au.username=$author AND a.name=$title RETURN c,u", parameters);

        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            InfoComment infoComment = new InfoComment();
            for (Pair<String, Value> nameValue : values) {
                if ("c".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    infoComment.setText(value.get("text").asString());
                    String timestamp = value.get("timestamp").asString();
                    infoComment.setTimestamp(Timestamp.valueOf(timestamp));

                }
                if ("u".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    infoComment.setAuthor(value.get("username").asString());

                }

            }
            infoComment.setAuthorArt(author);
            infoComment.setTitleArt(title);

            infoComments.add(infoComment);
        }

        return infoComments;
    }


    /**
     * La funzione conta il numero di like or dislike ad un articolo
     * @param title
     * @param author
     * @param type
     * @return Numero dei like, se type=like
     * @return Numero di dislike se type= dislike

     */
    public static int countLikes(String title, String author, String type)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountLikes(tx, title, author, type);
                }
            });
        }
    }

    /**
     * La funzione conta il numero di like or dislike ad un articolo
     * @param tx
     * @param title
     * @param author
     * @param type
     * @return Numero dei like, se type=like
     * @return Numero di dislike se type= dislike

     */

    public static int transactionCountLikes(Transaction tx, String title, String author, String type) {

        int numberLike = 0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("type", type);
        parameters.put("author", author);
        parameters.put("title", title);
        Result result = tx.run("MATCH (ul:User)-[l:LIKED{type:$type}]->(a),(i:User)-[p:PUBLISHED]->(a) WHERE a.name=$title AND i.username=$author return count(distinct l) AS quantiLike", parameters);

        if (result.hasNext()) {
            Record record = result.next();
            numberLike = record.get("quantiLike").asInt();

            }
        return numberLike;
    }

    /**
     * La funzione conta il numero di commenti ad un articolo
     * @param title
     * @param author
     * @return Numero dei commenti ad un articolo
     */

    public static int countComments(String title, String author)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountLikes(tx, title, author);
                }
            });
        }
    }

    /**
     * La funzione conta il numero di commenti ad un articolo
     * @param tx
     * @param title
     * @param author
     * @return Numero dei commenti ad un articolo
     */

    public static int transactionCountLikes(Transaction tx, String title, String author) {

        int numberComments = 0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", author);
        parameters.put("title", title);
        Result result = tx.run("MATCH (ul:User)-[c:COMMENTED]->(a),(i:User)-[p:PUBLISHED]->(a) WHERE a.name=$title AND i.username=$author return count(distinct c) AS quantiComments", parameters);

        if (result.hasNext()) {
            Record record = result.next();
            numberComments = record.get("quantiComments").asInt();

        }
        return numberComments;
    }
}
