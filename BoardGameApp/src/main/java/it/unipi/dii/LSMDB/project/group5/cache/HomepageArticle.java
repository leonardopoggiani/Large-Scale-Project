package it.unipi.dii.LSMDB.project.group5.cache;

public class HomepageArticle {
    public String titolo;

    HomepageArticle(String titolo){
        this.titolo = titolo;
    }

    // standard constructors/getters
    public static HomepageArticle get(String titolo){
        return new HomepageArticle(titolo);
    }
}