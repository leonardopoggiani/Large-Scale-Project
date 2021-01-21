package it.unipi.dii.lsmdb.project.group5.controller;

import it.unipi.dii.lsmdb.project.group5.bean.*;
import it.unipi.dii.lsmdb.project.group5.persistence.MongoDBManager.AnalyticsDBManager;
import java.util.List;

public class AnalyticsDBController {

    public List<VersatileUser> showMostVersatileInfluencer(int limit)
    {
        return it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.AnalyticsDBManager.mostVersatileUsers("influencer");
    }

    public List<GameBean> showLeastRatedGames(String mode, String value)
    {
        return AnalyticsDBManager.showLeastRatedGames(mode, value);
    }

    public List<CountryBean> getUsersFromCountry()
    {
        return AnalyticsDBManager.getUsersFromCountry();
    }

    public CategoryBean getCategoryInfo(String category)
    {
        return  AnalyticsDBManager.getCategoryInfo(category);
    }

    public List<UserBean> showLessRecentLoggedUsers()
    {
        return  AnalyticsDBManager.showLessRecentLoggedUsers();
    }

    public List<AgeBean> getUsersForAge ()
    {
        return AnalyticsDBManager.getUsersForAge();
    }

    public List<ActivityBean> getActivitiesStatisticsTotal ()
    {
        return AnalyticsDBManager.getActivitiesStatisticsTotal();
    }

    public List<InfluencerInfoBean> numberOfArticlesPublishedInASpecifiedPeriod (String start)
    {
        return AnalyticsDBManager.numberOfArticlesPublishedInASpecifiedPeriod(start);
    }

    public List<InfluencerInfoBean> distinctGamesInArticlesPublishedInASpecifiedPeriod (String start)
    {
        return AnalyticsDBManager.distinctGamesInArticlesPublishedInASpecifiedPeriod(start);
    }

    public List<InfluencerInfoBean> getNumLikeForEachInfluencer()
    {
        return AnalyticsDBManager.getNumLikeForEachInfluencer();
    }

    public List<InfluencerInfoBean> getNumDisLikeForEachInfluencer()
    {
        return AnalyticsDBManager.getNumDislikeForEachInfluencer();
    }

    public List<VersatileUser> mostVersatileUsers(String type)
    {
        return it.unipi.dii.lsmdb.project.group5.persistence.Neo4jDBManager.AnalyticsDBManager.mostVersatileUsers(type);
    }

    public List<CategoryBean> getGamesDistribution()
    {
        return AnalyticsDBManager.gamesDistribution();
    }

    public List<ActivityBean> getDailyAvgLoginForCountry()
    {
        return AnalyticsDBManager.dailyAvgLoginForCountry();
    }
}

