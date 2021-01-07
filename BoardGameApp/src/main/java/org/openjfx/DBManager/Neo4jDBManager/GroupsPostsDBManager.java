package org.openjfx.DBManager.Neo4jDBManager;


import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;
import org.openjfx.Entities.InfoGroup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.openjfx.DBManager.Neo4jDBManager.Neo4jDBManager.driver;

public class GroupsPostsDBManager {


    public static List<InfoGroup> showUsersGroups(String username)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<List>()
            {
                @Override
                public List<InfoGroup> execute(Transaction tx)
                {
                    return transactionShowUsersGroups(tx, username);
                }
            });
        }
    }


    private static List<InfoGroup> transactionShowUsersGroups(Transaction tx, String username)
    {
        List<InfoGroup> groups = new ArrayList<>();
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("username", username);

        Result result=tx.run("MATCH (u:User{username:$username})-[b:BE_PART]->(gr:Group)-[r:REFERRED]->(ga:Game)" +
                "RETURN u,b,gr,ga ORDER BY b.timestamp", parameters);

        while(result.hasNext())
        {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            InfoGroup group= new InfoGroup();
            for (Pair<String,Value> nameValue: values) {
                if ("ga".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    group.setGame(value.get("name").asString());

                }

                if ("gr".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    group.setName(value.get("name").asString());
                    group.setDescription(value.get("description").asString());
                    group.setAdmin(value.get("admin").asString());

                }

                if ("b".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    String timestamp = value.get("timestamp").asString();
                    group.setTimestamp(Timestamp.valueOf(timestamp));

                }


            }
            //article.setComments(ArticlesCommentsLikesDBManager.searchListComments(title, author));

            groups.add(group);
        }
        return groups;

    }
}
