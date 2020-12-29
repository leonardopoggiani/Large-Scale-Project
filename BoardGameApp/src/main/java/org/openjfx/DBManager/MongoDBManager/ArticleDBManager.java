package org.openjfx.DBManager.MongoDBManager;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.MongoCursor;
import javafx.scene.text.Text;
import org.bson.Document;
import org.openjfx.Entities.Article;
import java.util.*;

import static com.mongodb.client.model.Filters.*;

public class ArticleDBManager {
    private Article a;
    final static Class<?extends List> docClazz = new ArrayList<Document>().getClass();

    public static Article readArticle(String user, String title){
        MongoCollection<Document> collection = MongoDBManager.getCollection("User");

        Document fields = new Document();
        fields.put("username", 1);
        fields.put("title", 1);
        fields.put("timestamp", 1);
        fields.put("text", 1);
        fields.put("_id", 0);

        FindIterable<Document> docs = collection.find(and(eq("username", user), eq("articles.title", title)));

        Article a = new Article();
        try(MongoCursor<Document> cursor = docs.iterator()){
            while(cursor.hasNext()){
                Document next = cursor.next();
                a.setAuthor(next.get("username").toString());
                Document articles = (Document) next.get("articles");
                a.setTitle(articles.get("title").toString());
                a.setTimestamp(articles.get("timestamp").toString());
                a.setText(articles.get("body").toString());
            }
            cursor.close();
        }
        return a;



    }
}

