package it.unipi.dii.lsmdb.project.group5.cache;

import it.unipi.dii.lsmdb.project.group5.bean.ArticleBean;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.ExecutionException;

class ArticlesCacheTest {

    @org.junit.jupiter.api.Test
    void cachesInstanceShouldBeEqual() {
        ArticlesCache cache1 = ArticlesCache.getInstance();
        ArticlesCache cache2 = ArticlesCache.getInstance();

        Assertions.assertEquals(cache1,cache2);
    }

    @org.junit.jupiter.api.Test
    void cacheShouldReturnSomething() {
        ArticlesCache cache = new ArticlesCache();

        ArticleBean g = new ArticleBean();
        ArticleBean r = null;
        cache.cache.put(1,g);

        try {
            r = cache.cache.get(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Assertions.assertNotNull(r);
    }

    @org.junit.jupiter.api.Test
    void afterInvalidateCacheShouldBeEmpty() {
        ArticlesCache cache = new ArticlesCache();

        ArticleBean g = new ArticleBean();
        ArticleBean g2 = new ArticleBean();
        cache.cache.put(1,g);
        cache.cache.put(2,g2);

        cache.invalidaCache();

        long dim = cache.cache.size();

        Assertions.assertEquals(dim,0);
    }

    @org.junit.jupiter.api.Test
    void likesShouldDecrease() {
        ArticlesCache cache = new ArticlesCache();

        ArticleBean g = new ArticleBean();
        cache.cache.put(1,g);

        try {
            cache.addNumLike(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            cache.dimNumLike(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            Assertions.assertEquals(cache.cache.get(1).getNumberLikes(),0);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void likesShouldIncrease() {
        ArticlesCache cache = new ArticlesCache();

        ArticleBean g = new ArticleBean();
        cache.cache.put(1,g);

        try {
            cache.addNumLike(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        int like = 0;
        try {
            ArticleBean a = cache.cache.get(1);
            like = a.getNumberLikes();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(like,1);
    }

    @org.junit.jupiter.api.Test
    void unlikesShouldIncrease() {
        ArticlesCache cache = new ArticlesCache();

        ArticleBean g = new ArticleBean();
        cache.cache.put(1,g);

        try {
            cache.addNumUnlike(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        int unlike = 0;
        try {
            ArticleBean a = cache.cache.get(1);
            unlike = a.getNumberDislike();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(unlike,1);
    }

    @org.junit.jupiter.api.Test
    void unlikesShouldDecrease() {
        ArticlesCache cache = new ArticlesCache();

        ArticleBean g = new ArticleBean();
        cache.cache.put(1,g);

        try {
            cache.addNumUnlike(1);
            cache.dimNumUnlike(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        int unlike = 1;
        try {
            ArticleBean a = cache.cache.get(1);
            unlike = a.getNumberDislike();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(unlike,0);
    }

    @org.junit.jupiter.api.Test
    void numberOfCommentsShouldIncrease() {
        ArticlesCache cache = new ArticlesCache();

        ArticleBean g = new ArticleBean();
        cache.cache.put(1,g);

        try {
            cache.addNumComment(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        int comment = 0;
        try {
            ArticleBean a = cache.cache.get(1);
            comment = a.getNumberComments();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(comment,1);
    }

    @org.junit.jupiter.api.Test
    void numberOfCommentsShouldDecrease() {
        ArticlesCache cache = new ArticlesCache();

        ArticleBean g = new ArticleBean();
        cache.cache.put(1,g);

        try {
            cache.addNumComment(1);
            cache.dimNumComment(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        int comment = 1;
        try {
            ArticleBean a = cache.cache.get(1);
            comment = a.getNumberComments();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Assertions.assertEquals(comment,0);
    }
}