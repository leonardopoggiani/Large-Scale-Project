package it.unipi.dii.LSMDB.project.group5.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.bean.GameBean;
import it.unipi.dii.LSMDB.project.group5.controller.ArticlesCommentsLikesDBController;
import it.unipi.dii.LSMDB.project.group5.controller.GamesReviewsRatesDBController;
import it.unipi.dii.LSMDB.project.group5.view.LoginPageView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GamesCache {
    GamesReviewsRatesDBController controller = new GamesReviewsRatesDBController();

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
        cache.invalidateAll();
    }

    private GameBean cercaGiochi(String name) {
        return controller.showGame(name);
    }

    public GameBean getDataIfPresent(String name) throws ExecutionException {
        GameBean a = cache.get(name);
        if(a == null || a.getName() == null){
            cache.put(name,cercaGiochi(name));
        }
        return a;
    }


}
