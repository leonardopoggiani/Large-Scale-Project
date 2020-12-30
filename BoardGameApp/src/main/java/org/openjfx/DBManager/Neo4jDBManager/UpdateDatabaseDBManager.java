package org.openjfx.DBManager.Neo4jDBManager;

public class UpdateDatabaseDBManager extends Neo4jDBManager {

   /* public static boolean addComment(Comment newComm) {
        try (Session session = driver.session()) {
            boolean res;
            return session.writeTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction tx) {
                    return transactionAddComment(tx, newComm);
                }
            });


        }
    }
    private static Boolean transactionAddComment(Transaction tx, Comment newComm)
    {
        HashMap<String,Object> parameters= new HashMap<>();
        parameters.put("id",calciatoreDoc.getString("_id"));
        parameters.put("nome", calciatoreDoc.getString("nome"));
        parameters.put("dataNascita",calciatoreDoc.getLong("dataNascita"));
        parameters.put("luogoNascita",calciatoreDoc.getString("luogoNascita"));
        try{
            parameters.put("altezza",calciatoreDoc.getInteger("altezza"));
        }
        catch(Exception e)
        {
            parameters.put("altezza",calciatoreDoc.getLong("altezza"));
        }
        parameters.put("nazionalita",calciatoreDoc.getString("nazionalita"));
        parameters.put("piede",calciatoreDoc.getString("piede"));
        parameters.put("procuratore",calciatoreDoc.getString("procuratore"));
        parameters.put("squadra",calciatoreDoc.getString("squadra"));
        parameters.put("linkFoto",calciatoreDoc.getString("linkFoto"));
        try{
            parameters.put("inRosa",calciatoreDoc.getLong("inRosa"));
        }
        catch(Exception e){
            parameters.put("inRosa",calciatoreDoc.getInteger("inRosa"));
        }
        try{
            parameters.put("scadenza",calciatoreDoc.getLong("scadenza"));
        }
        catch(Exception e){
            parameters.put("scadenza",calciatoreDoc.getInteger("scadenza"));}
        try{
            parameters.put("valoreAttuale",calciatoreDoc.getInteger("valoreAttuale"));
        }
        catch(Exception e)
        {
            parameters.put("valoreAttuale",calciatoreDoc.getInteger("valoreAttuale"));
        }
        StatementResult result=tx.run("CREATE(Calciatore{id:$id,nome:$nome,dataNascita:$dataNascita,"
                        + "luogoNascita:$luogoNascita,altezza:$altezza,nazionalita:$nazionalita,piede:$piede,"
                        + "procuratore:$procuratore,squadra:$squadra,linkFoto:$linkFoto,inRosa:$inRosa,scadenza:$scadenza})"
                ,parameters);

    }*/

}
