package it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.lsmdb.project.group5.view.HomepageArticles;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jDBManager  {

    // local-host
    // protected static String uri="bolt://localhost:7687";

    // local-host cluster
    // protected static String uri="bolt://localhost:7688";

    // vm standalone
    protected static String uri="neo4j://172.16.3.144:7687";

    protected static String user="neo4j";
    protected static String password="root";
    protected static Driver driver;

    public static boolean InitializeDriver()
    {
        try{
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user,password));
            return true;
        }
        catch (Exception ex){
            System.err.println(ex.getMessage());
            HomepageArticles.setLitimitedVersion();
            return false;
        }
    }

    public static void close() throws Exception
    {
        driver.close();
    }

    public static Driver getDriver() {
        return driver;
    }

}


