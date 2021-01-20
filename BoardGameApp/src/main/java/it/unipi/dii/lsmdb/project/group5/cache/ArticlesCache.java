package it.unipi.dii.lsmdb.project.group5.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.unipi.dii.lsmdb.project.group5.bean.ArticleBean;
import it.unipi.dii.lsmdb.project.group5.controller.ArticlesPagesDBController;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author leonardopoggiani
 */
public class ArticlesCache {

    ArticlesPagesDBController controller = new ArticlesPagesDBController();

    private static ArticlesCache instance;
    LoadingCache<Integer, ArticleBean> cache;

    public static ArticlesCache getInstance() {
        if (instance == null) {
            instance = new ArticlesCache();
        }
        return instance;
    }

    ArticlesCache() {
        cache = CacheBuilder
                .newBuilder()
                .maximumSize(50)
                .expireAfterAccess(20, TimeUnit.MINUTES)
                .build(new CacheLoader<Integer, ArticleBean>() {
                    @Override
                    public ArticleBean load(Integer id) throws Exception {
                        return cercaArticoli(id);
                    }
                });
    }

    private ArticleBean cercaArticoli(int id) {
        return controller.showArticleDetails(id);
    }

    public ArticleBean getDataIfPresent(int id) throws ExecutionException {
        ArticleBean a = cache.get(id);

        if(a == null || a.getTitle() == null){
            cache.put(id,cercaArticoli(id));
        }
        return a;
    }

    public void invalidaCache() {
        cache.invalidateAll();
    }
}
