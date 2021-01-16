package it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager;

import it.unipi.dii.LSMDB.project.group5.bean.UserBean;
import org.apache.commons.codec.digest.DigestUtils;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.util.Pair;

import java.util.HashMap;
import java.util.List;

public class LoginSignUpDBManager extends Neo4jDBManager {


    /**
     * La funzione registra un nuovo utente
     * @param user
     * @return 0 se non esiste un utente con lo stesso username
     * @return 1 altrimenti
     */
     public static int registerUser(final UserBean user)
     {
         try(Session session = driver.session())
         {
             return session.writeTransaction(new TransactionWork<Integer>()
                 {
                     @Override
                     public Integer execute(Transaction tx)
                     {
                         return createUserNode(tx,user);
                     }
                 }
             );
         }
         catch(Exception ex)
         {
             System.err.println(ex.getMessage());
             return  -1;
         }


     }

    /**
     * La funzione crea un nodo utente nel database controllando che non esista uno stesso username
     * @param tx
     * @param user
     * @return 0 se l'iscrizione va a buon fine
     * @return -1 altrimenti
     */
    private static int createUserNode(Transaction tx, UserBean user)
    {
        HashMap<String,Object> parameters = new HashMap<>();
        parameters.put("username", user.getUsername());
        parameters.put("password", user.getPassword());
        parameters.put("category1", user.getCategory1());
        parameters.put("category2", user.getCategory2());
        parameters.put("role", user.getRole());
        if(!UserPresent(tx, user.getUsername())){
            tx.run("CREATE(u:User{username:$username,password:$password,category1: $category1, category2:$category2, role:$role})",parameters);
            return 0;
        }
        return -1;
    }


    /**
     * La funzione controlla che non esista uno stesso username
     * @param tx
     * @param username
     * @return 0 se non esiste un utente con lo stesso username
     * @return 1 altrimenti
     */
    protected static boolean UserPresent(Transaction tx,String username)
    {
        HashMap<String,Object> parameters =new HashMap<>();
        parameters.put("username",username);
        Result result=tx.run("MATCH(u:User) WHERE u.username=$username RETURN u",parameters);
        if(result.hasNext())
            return true;
        return false;
    }

    /**
     * La funzione esergue la query per il login di un utente controllando username e password
     * @param username
     * @param password
     * @return 0 se non esiste un utente con lo stesso username
     * @return 1
     */

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
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return  null;
        }

    }
    /**
     * La funzione controlla username e password nel database
     * @param tx
     * @param username
     * @param password
     * @return  restituisce il ruolo dell'utente se lo trova altrimenti null
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
                    role = value.get("role", role);

                }
            }
        }
        return role;
    }

    public static String passwordEncryption(String passToEncrypt)
    {
        String salt = "randomSalt";
        String encryptedPassword = DigestUtils.sha256Hex(passToEncrypt+salt);
        System.out.println("ENCRYPTION | encrypt-pw: " + encryptedPassword);
        return encryptedPassword;
    }


}

