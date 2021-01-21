package it.unipi.dii.lsmdb.project.group5.controller;

import it.unipi.dii.lsmdb.project.group5.bean.*;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.AnalyticsDBManager;

import java.util.List;
import java.util.logging.Logger;

public class AnalyticsDBController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    //MONGODB
    public List<VersatileUser> showMostVersatileInfluencer(int limit)
    {
        return it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.AnalyticsDBManager.mostVersatileUsers("influencer");
    } // statistiche generali

    public List<GameBean> showLeastRatedGames(String mode, String value)
    {
        return AnalyticsDBManager.showLeastRatedGames(mode, value);
    } // admin rimuove giochi

    public List<CountryBean> getUsersFromCountry()
    {
        return AnalyticsDBManager.getUsersFromCountry();
    } // statistiche generali

    public CategoryBean getCategoryInfo(String category)
    {
        return  AnalyticsDBManager.getCategoryInfo(category);
    } // statistiche generali

    public List<UserBean> showLessRecentLoggedUsers()
    {
        return  AnalyticsDBManager.showLessRecentLoggedUsers();
    } // admin rimuove utenti

    public List<AgeBean> getUsersForAge ()
    {
        return AnalyticsDBManager.getUsersForAge();
    } // statistiche generali

    public List<ActivityBean> getActivitiesStatisticsTotal ()
    {
        return AnalyticsDBManager.getActivitiesStatisticsTotal();
    } // statistiche generali

    public List<InfluencerInfoBean> numberOfArticlesPublishedInASpecifiedPeriod (String start)
    {
        return AnalyticsDBManager.numberOfArticlesPublishedInASpecifiedPeriod(start);
    } // moderatore declassa influencer

    public List<InfluencerInfoBean> distinctGamesInArticlesPublishedInASpecifiedPeriod (String start)
    {
        return AnalyticsDBManager.distinctGamesInArticlesPublishedInASpecifiedPeriod(start);
    } //  moderatore declassa influencer

    public List<InfluencerInfoBean> getNumLikeForEachInfluencer()
    {
        return AnalyticsDBManager.getNumLikeForEachInfluencer();
    } // moderatore rimuove influencer

    public List<InfluencerInfoBean> getNumDisLikeForEachInfluencer()
    {
        return AnalyticsDBManager.getNumDislikeForEachInfluencer();
    } // moderatore rimuove influencer


    //NEO4J (da vedere se va cambiata con quella in mongoDB)
    public List<VersatileUser> mostVersatileUsers(String type)
    {
        return it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.AnalyticsDBManager.mostVersatileUsers(type);
    } // moderatore promuove utenti

    public List<CategoryBean> getGamesDistribution()
    {
        return AnalyticsDBManager.gamesDistribution();
    } // moderatore promuove utenti

    public List<ActivityBean> getDailyAvgLoginForAgeRange(int start, int end)
    {
        return AnalyticsDBManager.dailyAvgLoginForAgeRange(start, end);
    } // moderatore promuove utenti

    public List<ActivityBean> getDailyAvgLoginForCountry()
    {
        return AnalyticsDBManager.dailyAvgLoginForCountry();
    } // moderatore promuove utenti
}

