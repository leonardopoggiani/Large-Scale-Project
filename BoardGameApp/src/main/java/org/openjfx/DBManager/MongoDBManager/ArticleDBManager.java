package org.openjfx.DBManager.MongoDBManager;


import com.mongodb.client.*;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import javafx.scene.text.Text;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.openjfx.Entities.Article;
import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class ArticleDBManager {

    public static Article readArticle(String user, String title){
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");

        Bson unwind = unwind("$articles");
        Bson projection = project(fields( excludeId(), include("username", "articles")));
        Bson match =  match(and(eq("username",user), eq("articles.title", title)));

        Article a = new Article();

       try(MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind,match,projection)).iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                //System.out.println(next.toJson());
                a.setAuthor(next.get("username").toString());
                Document article = (Document)next.get("articles");
                a.setTitle(article.get("title").toString());
                a.setTimestamp(article.get("timestamp").toString());
                a.setText(article.get("body").toString());
            }
            cursor.close();
        } return a;



    }
}

