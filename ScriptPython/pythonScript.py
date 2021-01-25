import pandas as pd
import numpy as np
import random 
import json
import random
from datetime import datetime
from json import encoder

encoder.FLOAT_REPR = lambda o: format(o, '.2f')

random.seed(1)


def convert_from_json_to_csv(json_name,csv_name):
    df = pd.read_json(json_name,lines=True)
    df.to_csv(csv_name)

def convert_from_csv_to_json(json_name,csv_name):
    csv = pd.read_csv(csv_name, sep=',')
    with open(json_name, 'w') as f:
        f.write(csv.to_json(orient='records', lines=True))

def fixGamesNeo4j (file):
    with open(file, 'r', encoding="utf8") as data_file:
        data = json.load(data_file)
    df = pd.json_normalize(data)

    df = df.drop_duplicates(subset=['name'])
    df.drop(columns=['year', 'description', 'publisher', 'url', 'image_url', 'rules_url',
    'min_players', 'max_players', 'min_age', 'max_age','min_time', 'max_time', 'cooperative',
    'expansion', 'num_votes', 'avg_rating', 'num_reviews', 'complexity'], axis=1, inplace=True)
    

    '''result = df.to_json(orient="records", double_precision=0)
    parsed = json.loads(result)'''

    array1 = []
    array2 = []
    for elem in df['category']:
        if not elem:
            array1.append("null")
            array2.append("null")
        elif len(elem)>1:
            array1.append(elem[0])
            array2.append(elem[1])
        elif len(elem)>0:
            array1.append(elem[0])
            array2.append("null")
        else:
            array1.append("null")
            array2.append("null")
    
    df['category1'] = array1
    df['category2'] = array2
    df.drop(columns=['category'],axis=1, inplace=True)
    
    df.to_csv('games.csv', index=False)  


    '''with open('games.json', 'w', encoding='utf-8') as f:
        json.dump(parsed, f, ensure_ascii=False, indent=4)'''

def fixUsersNeo4j (file):
    with open(file, 'r', encoding="utf8") as data_file:
        data = json.load(data_file)
    df = pd.json_normalize(data)

    df = df.drop_duplicates(subset=['username'])
    df.drop(columns=['first_name', 'last_name', 'age', 'registered', 'last_login', 'country',
    'updated', 'articles'], axis=1, inplace=True)

    df['role'] = "normalUser"


    categorie_trovate = ["Math:1104","Card Game:1002","Humor:1079","Party Game:1030",
                        "Number:1098","Puzzle:1028","Dice:1017","Sports:1038",
                        "Book:1117","Fantasy:1010","Miniatures:1047","Wargame:1019",
                        "Napoleonic:1051","Children's Game:1041","Memory:1045",
                        "Educational:1094","Medical:2145","Animals:1089","Racing:1031",
                        "Adventure:1022","Travel:1097","Abstact Strategy:1009",
                        "Economic:1021","Trains:1034","Transportation:1011","Real-time:1037",
                        "Action/Dexterity:1032","Ancient:1050","Collectible Components:1044",
                        "Fighting:1046","Movies/TV/Radio Theme:1064","Bluffing:1023",
                        "Zombies:2481","Medieval:1035","Negotiation:1026","World War II: 1049",
                        "Spies/Secret Agents:1081","Deduction:1039","Murder/Mystery:1040",
                        "Aviation/Flight:2650","Modern Warfare:1069","Territory Building:1086",
                        "Print & Play:1120","Novel-Based:1093","Puzzle:1028","Science Fiction:1016",
                        "Exploration:1020","Word-game:1025","Video Game Theme:1101"]

    
    df_categorie_1 = []
    df_categorie_2 = []


    for row in df.iterrows():

        index_1 = 0
        index_2 = 0
        while (index_1 <= 0 or index_2 <= 0) or (index_1 == index_2):
            index_1 = round(random.random()*len(categorie_trovate),0) - 1
            index_2 = round(random.random()*len(categorie_trovate),0) - 1

        df_categorie_1.append(categorie_trovate[int(index_1)])
        df_categorie_2.append(categorie_trovate[int(index_2)])        
    
    df["category1"] = df_categorie_1
    df["category2"] = df_categorie_2

    df.to_csv('users.csv', index=False)  


def fixGamesMongoDB (file):
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
            random = np.random.randint(1990,2020)
            array.append(random)
        else:
            array.append(item)
    
    df['year'] = array
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

    result = df.to_json(orient="records")
    parsed = json.loads(result)    
    
    with open('games.json', 'w', encoding='utf-8') as f:
        json.dump(parsed, f, ensure_ascii=False, indent=4)


def fixUsersMongoDB(file):
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
    df['role'] = 'normalUser'

    result = df.to_json(orient="records", double_precision=0, date_format= 'iso')
    parsed = json.loads(result)
    
    
    with open('usersOK.json', 'w', encoding='utf-8') as f:
        json.dump(parsed, f, ensure_ascii=False, indent=4)

