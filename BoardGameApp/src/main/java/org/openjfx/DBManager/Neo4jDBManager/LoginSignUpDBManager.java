package org.openjfx.DBManager.Neo4jDBManager;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

import java.util.HashMap;

public class LoginSignUpDBManager extends Neo4jDBManager {



     public static int registerUser(String username, String password, String category1, String category2, int age, String role)
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
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("username",username);
        Result result=tx.run("MATCH(u:User) WHERE u.username=$username RETURN u",parameters);
        if(result.hasNext())
            return true;
        return false;
    }

}

