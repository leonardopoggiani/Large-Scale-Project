import numpy as np
import pandas as pd
import json
import random
from datetime import datetime

from json import encoder
encoder.FLOAT_REPR = lambda o: format(o, '.2f')

random.seed(1)

def fixGames (file):
    with open(file, 'r', encoding="utf8") as data_file:
        data = json.load(data_file)
    df = pd.json_normalize(data)

    df = df.drop_duplicates(subset=['name'])
    df.drop(columns=['image_file', 'bgg_id', 'easiest_complexity', 'hardest_complexity', 'highest_language_dependency', 'integration',
    'language_dependency', 'lowest_language_dependency', 'luding_id', 'max_players_best', 'max_players_rec', 'mechanic', 'min_players_best',
    'min_players_rec', 'rank', 'video_url', 'worst_rating', 'scraped_at', 'stddev_rating', 'family', 'best_rating', 'external_link',
    'game_type', 'review_url', '_id.$oid'], axis=1, inplace=True)
    

    df = df.astype(object)

    df['avg_rating'] = df['avg_rating'].replace('nan', 0)

    df['num_reviews'] = 0
    df['num_reviews'] = df['num_reviews'].fillna(0)
    array = []
    for item in df['year']:
        if np.isnan(item):
            #print("dentro")
            random = np.random.randint(1990,2020)
            array.append(random)
        else:
            array.append(item)
    
    df['year'] = array
    #df['year'] = df['year'].fillna(np.random.randint(1990,2020))
    df['min_players'] = df['min_players'].fillna(1)
    df['max_players'] = df['max_players'].fillna(10)
    df['min_age'] = df['min_age'].fillna(3)
    df['max_age'] = df['max_age'].fillna(99)
    df['min_time'] = df['min_time'].fillna(0)
    df['max_time'] = df['max_time'].fillna(180)
    df['num_votes'] = df['num_votes'].fillna(0)
    df['complexity'] = df['complexity'].fillna(1)
    df['avg_rating'] = df['avg_rating'].fillna(0)

    df['num_reviews'] = df['num_reviews'].astype('int')
    df['year'] = df['year'].astype('int')
    df['min_players'] = df['min_players'].astype('int')
    df['max_players'] = df['max_players'].astype('int')
    df['min_age'] = df['min_age'].astype('int')
    df['max_age'] = df['max_age'].astype('int')
    df['min_time'] = df['min_time'].astype('int')
    df['max_time'] = df['max_time'].astype('int')
    df['num_votes'] = df['num_votes'].astype('int')
    df['complexity'] = df['complexity'].astype('int')
    df['rules_url'] = ""
    df['avg_rating'] = df['avg_rating'].astype('float64')
    #df['avg_rating'] = df['avg_rating'].astype(float)
    #df['avg_rating'] = df['avg_rating'].map(lambda x: '{0:.1f}'.format(x)) 


    result = df.to_json(orient="records")
    parsed = json.loads(result)

    
    #print(json.dumps(parsed, indent = 4, sort_keys=True))
    
    
    with open('games.json', 'w', encoding='utf-8') as f:
        json.dump(parsed, f, ensure_ascii=False, indent=4)


def fixUsers(file):
    with open(file, 'r', encoding="utf8") as data_file:
        data = json.load(data_file)
    df = pd.json_normalize(data)
    df['username'] = df['bgg_user_name']

    df['updated'] = df['updated_at']
    
    
    df = df.drop_duplicates(subset=['username'])
    df.drop(columns=['item_id', 'bgg_user_name', 'updated_at', 'image_url', 'region',
    'scraped_at', '_id.$oid'], axis=1, inplace=True)
    df['age'] =  np.random.randint(18,70, size=len(df))
    df['password'] = 'eae453819442937c9a7e02d0e8e6265d9659950f38adf026ada8f1ac13ba65f2'

    result = df.to_json(orient="records", double_precision=0, date_format= 'iso')
    parsed = json.loads(result)

    
    #print(json.dumps(parsed, indent = 4, sort_keys=True))
    
    
    with open('usersOK.json', 'w', encoding='utf-8') as f:
        json.dump(parsed, f, ensure_ascii=False, indent=4)


file = 'boardgame.json'
#fixGames(file)
file = "users.json"
fixUsers(file)
