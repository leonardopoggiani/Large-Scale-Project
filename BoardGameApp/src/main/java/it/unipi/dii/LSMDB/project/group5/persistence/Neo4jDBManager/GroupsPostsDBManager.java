package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;


import it.unipi.dii.LSMDB.project.group5.bean.GroupBean;
import it.unipi.dii.LSMDB.project.group5.bean.PostBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupsPostsDBManager extends Neo4jDBManager {


    /**
     * La funzione restituisce la lista dei gruppi di un utente è membro
     * @param username
     * @param type
     * @return Lista dei gruppi di cui un utente è membro senza essere admin se type = member
     * @return  Lista dei gruppi di cui un utente è membro e admin se type = admin
     */
    public static List<GroupBean> showUsersGroups(final String username, final String type)
    {
        try(Session session=driver.session())
        {

            return session.readTransaction(new TransactionWork<List>()
            {
                @Override
                public List<GroupBean> execute(Transaction tx)
                {
                    return transactionShowUsersGroups(tx, username, type);
                }
            });
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return  null;
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
    private static List<GroupBean> transactionShowUsersGroups(Transaction tx, String username, String type) {
        List<GroupBean> groups = new ArrayList<>();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("username", username);
        Result result = null;

        if (type == "member") {
            result = tx.run("MATCH (u:User{username:$username})-[b:BE_PART]->(gr:Group)-[r:REFERRED]->(ga:Game)" +
                    " WHERE NOT (gr.admin=$username)" +
                    " RETURN u,b,gr,ga ORDER BY b.timestamp", parameters);

        } else if (type == "admin") {
            result = tx.run("MATCH (u:User{username:$username})-[b:BE_PART]->(gr:Group{admin:$username})-[r:REFERRED]->(ga:Game)" +
                    " RETURN u,b,gr,ga ORDER BY b.timestamp", parameters);
        }

        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            GroupBean group = new GroupBean();
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

    /**
     * La funzione restituisce tutti i membri di un gruppo
     * @param name
     * @param admin
     * @return ritorna la lista degli utenti membri di un gruppo
     */
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
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return  null;
        }
    }


    /**
     * La funzione restituisce tutti i membri di un gruppo
     * @param tx
     * @param name
     * @param admin
     * @return ritorna la lista degli utenti membri di un gruppo
     */
    private static List<String> transactionShowGroupsMembers(Transaction tx, String name, String admin)
    {
        List<String> members = new ArrayList<>();
        String member = null;
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("admin", admin);

        Result result1 = tx.run("MATCH (gr:Group{name:$name, admin:$admin})<-[b:BE_PART]-(u:User)" +
                " RETURN u", parameters);

        while(result1.hasNext())
        {
            Record record = result1.next();
            List<Pair<String, Value>> values = record.fields();
            GroupBean group= new GroupBean();
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

    /**
     * La funzione restituisce il timestamp dell'ultimo post pubblicato sul gruppo
     * @param name
     * @param admin
     * @return ritorna il timestamp dell'ultimo post
     */
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
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return  null;
        }
    }


    /**
     * La funzione restituisce il timestamp dell'ultimo post pubblicato sul gruppo
     * @param tx
     * @param name
     * @param admin
     * @return ritorna il timestamp dell'ultimo post
     */
    private static Timestamp transactionTimestampLastPost(Transaction tx, String name, String admin)
    {

        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("admin", admin);
        Timestamp ts = null;
        Result result1 = tx.run("MATCH (gr:Group{name:$name, admin:$admin})<-[p:POST]-(u:User)" +
                " RETURN p ORDER BY p.timestamp DESC LIMIT 1 ", parameters);

        while (result1.hasNext())
        {
            Record record = result1.next();
            List<Pair<String, Value>> values = record.fields();
            GroupBean group= new GroupBean();
            for (Pair<String,Value> nameValue: values) {
                    Value value = nameValue.value();
                    String timestamp = value.get("timestamp").asString();
                    ts = Timestamp.valueOf(timestamp);

            }
        }

        return ts;

    }



    /**
     * La funzione restituisce il numero di membri di un gruppo
     * @param name
     * @param admin
     * @return ritorna il numero di membri
     */
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
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  -1;
        }
    }

    /**
     * La funzione restituisce il numero di membri di un gruppo
     * @param tx
     * @param name
     * @param admin
     * @return ritorna il numero di membri
     */

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

    /**
     * La funzione crea un nuovo gruppo
     * @param newGroup
     * @return true se ha creato correttamente il gruppo
     * @return false altrimenti
     */

    public static boolean addGroup(final GroupBean newGroup) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddGroup(tx, newGroup);
                }
            });
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  false;
        }
    }


    /**
     * La funzione crea un nuovo gruppo
     * @param tx
     * @param group
     * @return true se ha creato correttamente il gruppo
     * @return false altrimenti
     */
    private static Boolean transactionAddGroup(Transaction tx, GroupBean group) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("admin", group.getAdmin());
        parameters.put("game", group.getGame());
        parameters.put("timestamp", group.getTimestamp().toString());
        parameters.put("desc", group.getDescription());
        parameters.put("name", group.getName());

        Result result0 = tx.run("MATCH (u:User {username:$admin})-[b:BE_PART]->(g:Group{name:$name, admin:$admin}) return g"
                , parameters);

        if (result0.hasNext()) {
            System.out.println("Già hai creato un gruppo con questo nome, cambialo!");
            return false;
        } else {

            Result result = tx.run("MATCH (u:User{username:$admin}),(ga:Game{name:$game})" +
                            " CREATE (u)-[:BE_PART{timestamp:$timestamp}]->(gr:Group {name:$name,description:$desc, admin:$admin})-[:REFERRED]->(ga)" +
                            " RETURN gr"
                    , parameters);

            if (result.hasNext()) {
                System.out.println("Ho aggiunto il nuovo gruppo");
                return true;
            }
            return false;
        }
    }

    /**
     * La funzione elimina un gruppo
     * @param delGroup
     * @param delAdmin
     * @return true se ha eliminato correttamente il gruppo, false altrimenti
     */

    public static boolean deleteGroup(final String delGroup, final String delAdmin) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionDeleteGroup(tx, delGroup, delAdmin);
                }
            });


        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  false;
        }
    }


    /**
     * La funzione elimina un gruppo
     * @param tx
     * @param delGroup
     * @return true se ha eliminato correttamente il gruppo
     * @return false altrimenti
     */


    private static Boolean transactionDeleteGroup(Transaction tx, String delGroup, String delAdmin) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("admin", delAdmin);
        parameters.put("name", delGroup);
        String eliminaPosts = "MATCH (u:User)-[p:POST]->(gr:Group)" +
                " WHERE gr.name=$name" +
                " DELETE p";
        tx.run(eliminaPosts, parameters);

        //Esiste sempre almeno un be_part quello dell'admin
        tx.run("MATCH (u:User)-[b:BE_PART]->(gr:Group)-[r:REFERRED]->(ga:Game)" +
                        " WHERE gr.name=$name and  gr.admin=$admin" +
                        " DELETE b,gr,r"
                , parameters);

        return true;
    }

    /**
     * La funzione aggiunge o toglie un membro ad un gruppo in base al parametro type
     * @param username
     * @param name
     * @param admin
     * @param type
     * @return true se ha tolto o aggiunto un membro correttamente
     */


    public static boolean addDeleteGroupMember(final String username, final String name, final String admin, final String type) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    if(type.equals("add"))
                        return transactionAddGroupMember(tx, username, name, admin);
                    else
                        return transactionDeleteGroupMember(tx, username, name, admin);
                }
            });


        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  false;
        }
    }


    /**
     * La funzione aggiunge un membro ad un gruppo
     * @param tx
     * @param username
     * @param name
     * @param admin
     * @return true se ha aggiunto correttamente il membro, false altrimenti
     */

    private static boolean transactionAddGroupMember(Transaction tx, String username, String name, String admin) {

        HashMap<String, Object> parameters = new HashMap<>();
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        parameters.put("admin", admin);
        parameters.put("name", name);
        parameters.put("username", username);
        parameters.put("timestamp", ts.toString());

        Result result = tx.run("MATCH (u:User{username:$username})-[b:BE_PART]->(gr:Group{name:$name, admin:$admin})" +
                        " RETURN b"
                , parameters);
        if (result.hasNext()) {
            //System.out.println("Utente già membro del gruppo!");
            return false;
        } else {
            result = tx.run("MATCH (u:User{username:$username}),(gr:Group{name:$name, admin:$admin})" +
                            " CREATE (u)-[b:BE_PART {timestamp:$timestamp}]->(gr)" +
                            " RETURN b"
                    , parameters);
            if (result.hasNext()) {
                System.out.println("Ho aggiunto il nuovo gruppo");
                return true;
            }
            return false;
        }

    }

    /**
     * La funzione elimina un un membro di un gruppo
     * @param tx
     * @param username
     * @param group
     * @param admin
     * @return true se ha eliminato correttamente l'utente membro, false altrimenti
     */


    private static boolean transactionDeleteGroupMember(Transaction tx, String username, String group, String admin) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("admin", admin);
        parameters.put("group", group);
        String eliminaMembro = "MATCH (u:User{username:$username})-[b:BE_PART]->(gr:Group{name:$group, admin:$admin})\n" +
                " DELETE b";
        tx.run(eliminaMembro, parameters);

        return true;
    }

    /**
     * La funzione crea o elimina un post da un gruppo
     * @param newPost
     * @return true se ha creato o eliminato correttamente, false altrimenti
     */

    public static boolean addDeletePost(final PostBean newPost, final String type) {
        try (Session session = driver.session()) {

            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    if(type.equals("add"))
                        return transactionAddPost(tx, newPost);
                    else
                        return transactionDeletePost(tx, newPost);
                }
            });


        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  false;
        }
    }


    /**
     * La funzione crea un nuovo post in un gruppo
     * @param tx
     * @param post
     * @return true se ha creato correttamente il gruppo
     * @return false altrimenti
     */
    private static Boolean transactionAddPost(Transaction tx, PostBean post) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("group", post.getGroup());
        parameters.put("timestamp", post.getTimestamp().toString());
        parameters.put("admin", post.getAdmin());
        parameters.put("author", post.getAuthor());
        parameters.put("text", post.getText());


        Result result = tx.run("MATCH (u:User {username:$author})-[b:BE_PART]->(g:Group{name:$group, admin:$admin}) return b"
                , parameters);

        //Anche senza va bene lo stesso
        if (result.hasNext()) {
            result = tx.run("MATCH (u:User{username:$author}),(gr:Group{name:$group, admin:$admin})" +
                            " CREATE (u)-[p:POST{timestamp:$timestamp, text:$text}]->(gr)" +
                            " RETURN p"
                    , parameters);

            if (result.hasNext()) {
                System.out.println("Ho aggiunto il nuovo post");
                return true;
            }
            return false;
        }
        return false;

    }



    /**
     * La funzione elimina un un post
     * @param tx
     * @param delPost
     * @return true se ha eliminato correttamente il gruppo, false altrimenti
     */


    private static Boolean transactionDeletePost(Transaction tx, PostBean delPost) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("admin", delPost.getAdmin());
        parameters.put("group", delPost.getGroup());
        parameters.put("author", delPost.getAuthor());
        parameters.put("timestamp", delPost.getTimestamp());
        String eliminaPost = "MATCH (u:User{username:$author})-[p:POST{timestamp:$timestamp}]\n" +
                "->(gr:Group{name:$group, admin:$admin})\n" +
                " DELETE p";
        tx.run(eliminaPost, parameters);

        return true;
    }


    /**
     * La funzione restituisce la lista dei post ad un gruppo limitato in base al parametro
     * @param group
     * @param admin
     * @param limit
     * @return Lista dei post di un gruppo
     */
    public static List<PostBean> showGroupsPosts(final String group, final String admin, final int limit)
    {
        try(Session session=driver.session())
        {

            return session.readTransaction(new TransactionWork<List>()
            {
                @Override
                public List<PostBean> execute(Transaction tx)
                {
                    return transactionShowGroupsPosts(tx, group, admin, limit);
                }
            });
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            return  null;
        }

    }

    /**
     * La funzione restituisce la lista dei post ad un gruppo limitato in base al parametro
     * @param tx
     * @param group
     * @param admin
     * @param limit
     * @return Lista dei post di un gruppo
     */
    private static List<PostBean> transactionShowGroupsPosts(Transaction tx, String group, String admin, int limit) {
        List<PostBean> posts = new ArrayList<>();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("group", group);
        parameters.put("admin", admin);
        parameters.put("limit", limit);
        Result result = tx.run("MATCH (u:User)-[p:POST]->(gr:Group{name:$group, admin:$admin}) " +
                " RETURN u,p,gr ORDER BY p.timestamp DESC", parameters);

        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            PostBean post = new PostBean();
            for (Pair<String, Value> nameValue : values) {
                if ("p".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    String text = value.get("text").asString();
                    String timestamp = value.get("timestamp").asString();
                    post.setText(text);
                    post.setTimestamp(Timestamp.valueOf(timestamp));
                }

                if ("u".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    String author = value.get("username").asString();
                    post.setAuthor(author);

                }



            }
            //article.setComments(ArticlesCommentsLikesDBManager.searchListComments(title, author));
            post.setGroup(group);
            post.setAdmin(admin);
            posts.add(post);
        }

        return posts;

    }

}
