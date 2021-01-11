package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jDBManager  {

    protected static String uri="bolt://localhost:11008";
    protected static String user="neo4j";
        protected static String password="root";

    //public static void InitializeDriver()
    //{
    protected static Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user,password));
    //}

    public static void close() throws Exception
    {
        driver.close();
    }

}


