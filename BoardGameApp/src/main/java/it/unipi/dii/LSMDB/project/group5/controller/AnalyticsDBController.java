package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.bean.*;
import it.unipi.dii.LSMDB.project.group5.persistence.MongoDBManager.AnalyticsDBManager;

import java.util.List;
import java.util.logging.Logger;

public class AnalyticsDBController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    //MONGODB
    public List<VersatileUser> showMostVersatileInfluencer(int limit)
    {
        return showMostVersatileInfluencer(limit);
    } // moderatore

    public List<GameBean> showLeastRatedGames(String mode, String value)
    {
        return AnalyticsDBManager.showLeastRatedGames(mode, value);
    } // admin rimuove giochileoanrdo

    public List<CountryBean> getUsersFromCountry()
    {
        return  AnalyticsDBManager.getUsersFromCountry();
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
        return  AnalyticsDBManager.getUsersForAge();
    } // statistiche generali

    public List<ActivityBean> getActivitiesStatisticsTotal ()
    {
        return  AnalyticsDBManager.getActivitiesStatisticsTotal();
    } // statistiche generali

    public List<InfluencerInfoBean> numberOfArticlesPublishedInASpecifiedPeriod (String start)
    {
        return numberOfArticlesPublishedInASpecifiedPeriod(start);
    } // moderatore promuovere influencer

    public List<InfluencerInfoBean> distinctGamesInArticlesPublishedInASpecifiedPeriod (String start)
    {
        return distinctGamesInArticlesPublishedInASpecifiedPeriod(start);
    } //  moderatore

    public List<InfluencerInfoBean> getNumLikeForEachInfluencer()
    {
        return getNumLikeForEachInfluencer();
    } // moderatore rimuove influencer

    public List<InfluencerInfoBean> getNumDisLikeForEachInfluencer()
    {
        return getNumDisLikeForEachInfluencer();
    } // moderatore rimuove influencer


    //NEO4J (da vedere se va cambiata con quella in mongoDB)
    public List<VersatileUser> mostVersatileUsers(String type)
    {
        return mostVersatileUsers(type);
    } // moderatore promuove utenti



}

