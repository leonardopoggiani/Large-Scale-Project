package it.unipi.dii.lsmdb.project.group5.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.unipi.dii.lsmdb.project.group5.bean.GameBean;
import it.unipi.dii.lsmdb.project.group5.controller.GamesPagesDBController;
import it.unipi.dii.lsmdb.project.group5.logger.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author leonardopoggiani
 */
public class GamesCache {
    GamesPagesDBController controller = new GamesPagesDBController();

    //Singleton
    private static GamesCache instance;
    LoadingCache<String, GameBean> cache;

    public static GamesCache getInstance() {
        if (instance == null) {
            instance = new GamesCache();
        }
        return instance;
    }

    GamesCache() {
        cache = CacheBuilder
                .newBuilder()
                .maximumSize(50)
                .expireAfterAccess(20, TimeUnit.MINUTES)
                .build(new CacheLoader<String, GameBean>() {
                    @Override
                    public GameBean load(String name) throws Exception {
                        return cercaGiochi(name);
                    }

                });
    }

    public void invalidaCache() {
        Logger.log("invalidate cahce");
        cache.invalidateAll();
    }

    private GameBean cercaGiochi(String name) {
        return controller.showGame(name);
    }

    public GameBean getDataIfPresent(String name) throws ExecutionException {
        GameBean a = cache.get(name);

        if(a == null || a.getName() == null){
            Logger.log("cache miss");
            cache.put(name,cercaGiochi(name));
        }
        return a;
    }

}
