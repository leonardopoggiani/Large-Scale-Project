package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.bean.*;

import java.util.List;
import java.util.logging.Logger;

public class AnalyticsDBController {
    Logger logger = Logger.getLogger(this.getClass().getName());

    //MONGODB
    public List<VersatileUser> showMostVersatileInfluencer(int limit)
    {
        return showMostVersatileInfluencer(limit);
    }

    public List<GameBean> showLeastRatedGames(String mode, String value)
    {
        return showLeastRatedGames(mode, value);
    }


    public List<CountryBean> getUsersFromCountry()
    {
        return  getUsersFromCountry();
    }

    public List<CategoryBean> getCategoryInfo(String category)
    {
        return  getCategoryInfo(category);
    }

    public  List<UserBean> showLessRecentLoggedUsers()
    {
        return  showLessRecentLoggedUsers();
    }

    public  List<AgeBean> getUsersForAge ()
    {
        return  getUsersForAge();
    }

    public List<ActivityBean> getActivitiesStatisticsTotal ()
    {
        return  getActivitiesStatisticsTotal();
    }

    public  List<InfluencerInfoBean> numberOfArticlesPublishedInASpecifiedPeriod (String start)
    {
        return numberOfArticlesPublishedInASpecifiedPeriod(start);
    }

    public   List<InfluencerInfoBean> distinctGamesInArticlesPublishedInASpecifiedPeriod (String start)

    {
        return distinctGamesInArticlesPublishedInASpecifiedPeriod(start);
    }

    public List<InfluencerInfoBean> getNumLikeForEachInfluencer()
    {
        return getNumLikeForEachInfluencer();
    }

    public List<InfluencerInfoBean> getNumDisLikeForEachInfluencer()
    {
        return getNumDisLikeForEachInfluencer();
    }


    //NEO4J (da vedere se va cambiata con quella in mongoDB)
    public List<VersatileUser> mostVersatileUsers(String type)
    {
        return mostVersatileUsers(type);
    }



}

