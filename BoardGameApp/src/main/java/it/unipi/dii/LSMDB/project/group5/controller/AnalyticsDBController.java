package it.unipi.dii.LSMDB.project.group5.controller;

import it.unipi.dii.LSMDB.project.group5.bean.LikeInfluencer;
import it.unipi.dii.LSMDB.project.group5.persistence.Neo4jDBManager.AnalyticsDBManager;

import java.util.List;
import java.util.logging.Logger;

public class AnalyticsDBController {
    Logger logger =  Logger.getLogger(this.getClass().getName());

    public List<LikeInfluencer> neo4jTop3InfluLikes(String type)
    {
        return AnalyticsDBManager.top3InfluLikes(type);
    }
}

