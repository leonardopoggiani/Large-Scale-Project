package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;

import java.util.HashMap;
import java.util.List;

public class LoginSignUpDBManager extends Neo4jDBManager {



     public static int registerUser(final String username, final String password, final String category1, final String category2, final int age, final String role)
     {

         try(Session session=driver.session())
         {
             return session.writeTransaction(new TransactionWork<Integer>()
                 {
                     @Override
                     public Integer execute(Transaction tx)
                     {
                         return createUserNode(tx, username, password, category1, category2, age, role);
                     }
                 }
             );
         }


     }

   /**

     * @return 0 se non esiste un utente con la stesso username l'operazione andra' a
     * buon fine.
     * @return  1 se esiste un utente con lo stesso username passato come parametro
     * l'operazione fallisce.
     */
    private static int createUserNode(Transaction tx,String username, String password, String category1,String category2, int age, String role)
    {
        HashMap<String,Object> parameters =new HashMap<>();
        parameters.put("username",username);
        parameters.put("password",password);
        parameters.put("category1",category1);
        parameters.put("category2",category2);
        parameters.put("age",age);
        parameters.put("role",role);
        if(!UserPresent(tx,username)){
            tx.run("CREATE(u:User{username:$username,password:$password,category1: $category1, category2:$category2,age:$age, role:$role})",parameters);
            return 0;
        }
        return 1;
    }

    protected static boolean UserPresent(Transaction tx,String username)
    {
        HashMap<String,Object> parameters =new HashMap<>();
        parameters.put("username",username);
        Result result=tx.run("MATCH(u:User) WHERE u.username=$username RETURN u",parameters);
        if(result.hasNext())
            return true;
        return false;
    }


    public static String loginUser(final String username,final String password)
    {

        try(Session session=driver.session())
        {

            return session.readTransaction(new TransactionWork<String>()
            {
                @Override
                public String execute(Transaction tx)
                {

                    return matchUser(tx,username,password);
                }
            });

        }

    }
    /**
     *
     * @param tx
     * @param username
     * @param password
     * @return 0 se viene trovato un utente che ha  password e  username passati
     * a parametro alla funzione
     * @return 1 se non esiste nessun utente che abbia username e password dati da
     * parametro
     */
    private static String matchUser(Transaction tx,String username,String password) {
        HashMap<String, Object> parameters = new HashMap<>();
        String role = "NA";
        parameters.put("username", username);
        parameters.put("password", password);
        Result result = tx.run("MATCH(u:User) WHERE u.username=$username AND u.password = $password RETURN u", parameters);
        role = getRole(result);

        return role;

    }

    private static String getRole(Result result) {
        String role = "NA";
        while (result.hasNext()) {
            Record record = result.next();
            List<Pair<String, Value>> values = record.fields();
            for (Pair<String, Value> nameValue : values) {
                if ("u".equals(nameValue.key())) {
                    Value value = nameValue.value();
                    if (value.isNull()) {
                        System.err.println("Boh errore in value!");
                    }
                    else
                        role = value.get("role", role);

                }
            }
        }
        return role;
    }

}

