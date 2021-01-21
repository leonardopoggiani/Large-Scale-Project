package it.unipi.dii.lsmdb.project.group5.cache;

import it.unipi.dii.lsmdb.project.group5.bean.GameBean;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.ExecutionException;

public class GamesCacheTest {

    @org.junit.jupiter.api.Test
    void cachesInstanceShouldBeEqual() {
        GamesCache cache1 = GamesCache.getInstance();
        GamesCache cache2 = GamesCache.getInstance();

        Assertions.assertEquals(cache1,cache2);
    }

    @org.junit.jupiter.api.Test
    void cacheShouldReturnSomething() {
        GamesCache cache = new GamesCache();

        GameBean g = new GameBean();
        GameBean r = null;
        cache.cache.put("key",g);

        try {
            r = cache.cache.get("key");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Assertions.assertNotNull(r);
    }

    @org.junit.jupiter.api.Test
    void afterInvalidateCacheShouldBeEmpty() {
        GamesCache cache = new GamesCache();

        GameBean g = new GameBean();
        GameBean g2 = new GameBean();
        cache.cache.put("key",g);
        cache.cache.put("key",g2);

        cache.invalidaCache();

        long dim = cache.cache.size();

        Assertions.assertEquals(dim,0);
    }
}