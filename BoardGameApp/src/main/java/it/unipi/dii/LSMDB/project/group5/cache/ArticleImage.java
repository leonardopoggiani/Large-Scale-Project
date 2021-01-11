package it.unipi.dii.LSMDB.project.group5.cache;

import javafx.scene.image.Image;

public class ArticleImage extends Image {
    public String url;

    ArticleImage(String url){
        super(url);
        this.url = url;
    }

    // standard constructors/getters
    public static ArticleImage get(String url){
        return new ArticleImage(url);
    }
}