package it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import it.unipi.dii.LSMDB.project.group5.bean.VersatileUser;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class ProvaVersatile {

    public static List<VersatileUser> showMostVersatileInfluencer (int lim){
        List<VersatileUser> ret = new ArrayList<VersatileUser>();

        MongoCollection<Document> collection = MongoDBManager.getCollection("Articles");


        Bson unwind = unwind("$games");
        //Bson group = group(groupFields, sum("howManyCategories", 1L));
        Bson group0 = new Document("$group", new Document ("_id", new Document("influencer" ,"$author")
                .append("category", "$games.category")));
        Bson group = group("$_id.influencer",sum("howManyCategories", 1L) );
        Bson projection = project(fields( excludeId(),computed("influencer", "$_id"),include( "howManyCategories")));
        Bson sort = sort(descending("howManyCategories"));
        Bson limit = limit(lim);

        try{
            MongoCursor<Document> cursor;

            cursor = collection.aggregate(Arrays.asList( unwind, group0,group, projection, sort, limit)).iterator();

            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
                VersatileUser temp = new VersatileUser();
                Document next = cursor.next();
                temp.setUsername(next.get("influencer").toString());
                temp.setHowManyCategories(Integer.parseInt(next.get("howManyCategories").toString()));
                ret.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
