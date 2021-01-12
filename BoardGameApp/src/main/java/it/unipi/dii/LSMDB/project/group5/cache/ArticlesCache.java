package it.unipi.dii.LSMDB.project.group5.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.unipi.dii.LSMDB.project.group5.bean.ArticleBean;
import it.unipi.dii.LSMDB.project.group5.controller.ArticlesCommentsLikesDBController;
import it.unipi.dii.LSMDB.project.group5.view.LoginPageView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ArticlesCache {
    ArticlesCommentsLikesDBController controller = new ArticlesCommentsLikesDBController();

    //Singleton
    private static ArticlesCache instance;
    LoadingCache<String, ArticleBean> cache;
    private String author;


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
                .build(new CacheLoader<String, ArticleBean>() {
                    @Override
                    public ArticleBean load(String title) throws Exception {
                        return cercaArticoli(title);
                    }

                });
    }

    private ArticleBean cercaArticoli(String title) {
        return controller.mongoDBshowArticle(title, author);
    }

    public ArticleBean getDataIfPresent(String titolo) throws ExecutionException {
        ArticleBean a = cache.get(titolo);
        if(a.getTitle() == null){
            cache.put(titolo,cercaArticoli(titolo));
        }
        return a;
    }

    public void setAuthor(String autore) {
        this.author = autore;
    }


}
