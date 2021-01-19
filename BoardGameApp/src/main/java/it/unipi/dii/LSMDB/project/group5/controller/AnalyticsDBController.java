package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.bean.*;

import java.util.List;
import java.util.logging.Logger;

public class AnalyticsDBController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    //MONGODB
    public List<VersatileUser> showMostVersatileInfluencer(int limit)
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.AnalyticsDBManager.mostVersatileUsers("influencer");
    } // statistiche generali

    public List<GameBean> showLeastRatedGames(String mode, String value)
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.showLeastRatedGames(mode, value);
    } // admin rimuove giochi

    public List<CountryBean> getUsersFromCountry()
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.getUsersFromCountry();
    } // statistiche generali

    public CategoryBean getCategoryInfo(String category)
    {
        return  it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.getCategoryInfo(category);
    } // statistiche generali

    public List<UserBean> showLessRecentLoggedUsers()
    {
        return  it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.showLessRecentLoggedUsers();
    } // admin rimuove utenti

    public List<AgeBean> getUsersForAge ()
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.getUsersForAge();
    } // statistiche generali

    public List<ActivityBean> getActivitiesStatisticsTotal ()
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.getActivitiesStatisticsTotal();
    } // statistiche generali

    public List<InfluencerInfoBean> numberOfArticlesPublishedInASpecifiedPeriod (String start)
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.numberOfArticlesPublishedInASpecifiedPeriod(start);
    } // moderatore declassa influencer

    public List<InfluencerInfoBean> distinctGamesInArticlesPublishedInASpecifiedPeriod (String start)
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.distinctGamesInArticlesPublishedInASpecifiedPeriod(start);
    } //  moderatore declassa influencer

    public List<InfluencerInfoBean> getNumLikeForEachInfluencer()
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.getNumLikeForEachInfluencer();
    } // moderatore rimuove influencer

    public List<InfluencerInfoBean> getNumDisLikeForEachInfluencer()
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.getNumDislikeForEachInfluencer();
    } // moderatore rimuove influencer


    //NEO4J (da vedere se va cambiata con quella in mongoDB)
    public List<VersatileUser> mostVersatileUsers(String type)
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.AnalyticsDBManager.mostVersatileUsers(type);
    } // moderatore promuove utenti

    public List<CategoryBean> getGamesDistribution()
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.gamesDistribution();
    } // moderatore promuove utenti

    public List<ActivityBean> getDailyAvgLoginForAgeRange(int start, int end)
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.dailyAvgLoginForAgeRange(start, end);
    } // moderatore promuove utenti

    public List<ActivityBean> getDailyAvgLoginForCountry()
    {
        return it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager.dailyAvgLoginForCountry();
    } // moderatore promuove utenti
}

