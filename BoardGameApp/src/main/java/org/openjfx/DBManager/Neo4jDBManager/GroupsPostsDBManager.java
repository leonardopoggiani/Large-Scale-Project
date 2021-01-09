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


    /**
     * La funzione restituisce la lista dei gruppi di un utente è membro
     * @param username
     * @param type
     * @return Lista dei gruppi di cui un utente è membro senza essere admin se type = member
     * @return  Lista dei gruppi di cui un utente è membro e admin se type = admin
     */
    public static List<InfoGroup> showUsersGroups( final String username, final String type)
    {
        try(Session session=driver.session())
        {
            return session.readTransaction(new TransactionWork<List>()
            {
                @Override
                public List<InfoGroup> execute(Transaction tx)
                {
                    return transactionShowUsersGroups(tx, username, type);
                }
            });
        }
    }

    /**
     * La funzione restituisce la lista dei gruppi di un utente è membro
     * @param tx
     * @param username
     * @param type
     * @return Lista dei gruppi di cui un utente è membro senza essere admin se type = member
     * @return  Lista dei gruppi di cui un utente è membro e admin se type = admin
     */
    private static List<InfoGroup> transactionShowUsersGroups(Transaction tx, String username, String type) {
        List<InfoGroup> groups = new ArrayList<>();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("username", username);
        Result result = null;

        if (type == "member") {
            result = tx.run("MATCH (u:User{username:$username})-[b:BE_PART]->(gr:Group)-[r:REFERRED]->(ga:Game)" +
                    "WHERE NOT (gr.admin=$username)" +
                    "RETURN u,b,gr,ga ORDER BY b.timestamp", parameters);

        } else if (type == "admin") {
            result = tx.run("MATCH (u:User{username:$username})-[b:BE_PART]->(gr:Group{admin:$username})-[r:REFERRED]->(ga:Game)" +
                    "RETURN u,b,gr,ga ORDER BY b.timestamp", parameters);
        }

        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            InfoGroup group = new InfoGroup();
            for (Pair<String, Value> nameValue : values) {
                if ("ga".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    group.setGame(value.get("name").asString());

                }

                if ("gr".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    String nameGame = value.get("name").asString();
                    String adminGame = value.get("admin").asString();
                    group.setName(nameGame);
                    group.setDescription(value.get("description").asString());
                    group.setAdmin(adminGame);
                    if(type == "admin")
                        group.setLastPost(timestampLastPost(nameGame, adminGame));

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
    public static List<String> showGroupsMembers(final String name, final String admin)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<List>()
            {
                @Override
                public List<String> execute(Transaction tx)
                {
                    return transactionShowGroupsMembers(tx, name, admin);
                }
            });
        }
    }

    private static List<String> transactionShowGroupsMembers(Transaction tx, String name, String admin)
    {
        List<String> members = new ArrayList<>();
        String member = null;
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("admin", admin);

        Result result1 = tx.run("MATCH (gr:Group{name:$name, admin:$admin})<-[b:BE_PART]-(u:User)" +
                "RETURN u", parameters);

        while(result1.hasNext())
        {
            Record record = result1.next();
            List<Pair<String, Value>> values = record.fields();
            InfoGroup group= new InfoGroup();
            for (Pair<String,Value> nameValue: values) {
                if ("u".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    member = value.get("username").asString();

                }

            }
            //article.setComments(ArticlesCommentsLikesDBManager.searchListComments(title, author));

            members.add(member);
        }
        return members;

    }

    public static Timestamp timestampLastPost(final String name, final String admin)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<Timestamp>()
            {
                @Override
                public Timestamp execute(Transaction tx)
                {
                    return transactionTimestampLastPost(tx, name, admin);
                }
            });
        }
    }

    private static Timestamp transactionTimestampLastPost(Transaction tx, String name, String admin)
    {

        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("admin", admin);
        Timestamp ts = null;
        Result result1 = tx.run("MATCH (gr:Group{name:$name, admin:$admin})<-[p:POST]-(u:User)" +
                "RETURN p ORDER BY p.timestamp DESC LIMIT 1 ", parameters);

        while (result1.hasNext())
        {
            Record record = result1.next();
            List<Pair<String, Value>> values = record.fields();
            InfoGroup group= new InfoGroup();
            for (Pair<String,Value> nameValue: values) {
                    Value value = nameValue.value();
                    String timestamp = value.get("timestamp").asString();
                    ts = Timestamp.valueOf(timestamp);

            }
        }

        return ts;

    }



    public static int countGroupsMembers(final String name, final String admin)
    {
        try(Session session=driver.session())
        {
            return session.writeTransaction(new TransactionWork<Integer>()
            {
                @Override
                public Integer execute(Transaction tx)
                {
                    return transactionCountGroupsMembers(tx, name, admin);
                }
            });
        }
    }

    //Funzione che conta i rates ad un determinato gioco

    public static int transactionCountGroupsMembers(Transaction tx, String name, String admin) {

        int numMembers= 0;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("admin", admin);
        Result result = tx.run("MATCH (u:User)-[b:BE_PART]->(gr:Group{name:$name, admin:$admin}) return count(distinct b) AS countMembers", parameters);

        if (result.hasNext()) {
            Record record = result.next();
            numMembers = record.get("countMembers").asInt();

        }
        return numMembers;
    }

}
