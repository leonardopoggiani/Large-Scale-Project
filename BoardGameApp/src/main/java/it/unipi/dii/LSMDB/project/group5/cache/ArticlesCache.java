package it.unipi.dii.LSMDB.project.group5.cache;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;;
import javafx.scene.image.Image;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ArticlesCache {
    //Singleton
    private static ArticlesCache instance;
    private AsyncLoadingCache<String, Image> cache;

    /**
     * Singleton
     * @return the private PokeMongoImageCache.
     */
    public static ArticlesCache getInstance() {
        if (instance == null) {
            instance = new ArticlesCache();
        }
        return instance;
    }

    ArticlesCache(){
        cache = Caffeine.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES) //After this time without read/write the resource is deallocated
                .maximumSize(2000) // Max number of images stored
                //.recordStats()
                .buildAsync(k -> ArticleImage.get(k));
    }

    public CompletableFuture<Image> getDataIfPresent(String url){
        return cache.get(url);
    }
}
