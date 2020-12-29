package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;
import org.openjfx.Entities.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListArticlesCommentDBManager extends Neo4jDBManager{

    public static List<Comment> searchListComments(String title, String author)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<List<Comment>>()
            {
                @Override
                public List<Comment> execute(Transaction tx)
                {
                    return transactionListComments(tx, title, author);
                }
            });
        }
    }

    public static List<Comment> transactionListComments(Transaction tx, String title, String author) {

        List<Comment> comments = new ArrayList<>();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("author", author);
        parameters.put("title", title);
        Result result = tx.run("MATCH (u:User)-[c:COMMENTED]->(a:Article)<-[p:PUBLISHED]-(au:User) WHERE au.username=$author AND a.name=$title RETURN c,u", parameters);

        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            Comment comment = new Comment();
            for (Pair<String, Value> nameValue : values) {
                if ("c".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    comment.setText(value.get("text").asString());
                    String timestamp = value.get("timestamp").toString();
                    comment.setTimestamp(timestamp);

                }
                if ("u".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    comment.setAuthor(value.get("username").asString());

                }

            }


            comments.add(comment);
        }
        return comments;
    }



    public static int countLikes(String title, String author, String type)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountLikes(tx, title, author, type);
                }
            });
        }
    }

    //Funzione che conta i like o i dislike in base al parametro type
    //Prende titolo dell'articolo, autore e tipo

    public static int transactionCountLikes(Transaction tx, String title, String author, String type) {

        int numberLike = 0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("type", type);
        parameters.put("author", author);
        parameters.put("title", title);
        Result result = tx.run("(ul:User)-[l:LIKED{type:$type}]->(a),(i:User)-[p:PUBLISHED]->(a)" +
                "WHERE a.name=$title AND i.username=$author" +
                "return count(distinct l) AS quantiLike", parameters);

        if (result.hasNext()) {
            Record record = result.next();
            numberLike = record.get("quantiLike").asInt();

            }
        return numberLike;
    }
}
