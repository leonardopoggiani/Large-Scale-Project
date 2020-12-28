package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jDBManager  {
    protected static Driver driver;
    protected static String uri="bolt://localhost:7687";
    protected static String user="neo4j";
    protected static String password="root";
    // protected static String password="root";

    public static void InitializeDriver()
    {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user,password));
    }

    public static void close() throws Exception
    {
        driver.close();
    }

}



    /*protected static boolean UtentePresente(Transaction tx,String email)
    {
        HashMap<String,Object> parameters =new HashMap<>();
        parameters.put("email",email);
        StatementResult result=tx.run("MATCH(a:Utente) WHERE a.email=$email RETURN a",parameters);
        if(result.hasNext())
            return true;
        return false;
    }*/


