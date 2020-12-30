package org.openjfx.DBManager.Neo4jDBManager;

public class UpdateDatabaseDBManager extends Neo4jDBManager {
    //DA FINIRE
/*
    public static boolean addComment(Comment newComm, String title, String authorArt) {
        try (Session session = driver.session()) {
            boolean res;
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddComment(tx, newComm, title, authorArt);
                }
            });


        }
    }
    private static Boolean transactionAddComment(Transaction tx, Comment newComm, String title, String authorArt)
    {
        HashMap<String,Object> parameters= new HashMap<>();
        parameters.put("authorComm",newComm.getAuthor());
        parameters.put("text", newComm.getText());
        parameters.put("timestamp", newComm.getTimestamp());
        parameters.put("authorArt", authorArt);
        parameters.put("title", title);

        Result result=tx.run("MATCH(u:User {username:$author}),(ua:User {ua.username:$authorArt})-[:PUBLISHED]->(a:Article{name:$title}) CREATE (u)-[c:COMMENTED{timestamp:$timestamp, text:$text}]->(a) "
                ,parameters);
        return true;
    }
*/
}
