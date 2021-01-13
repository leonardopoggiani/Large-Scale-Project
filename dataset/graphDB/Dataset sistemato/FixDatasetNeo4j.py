import numpy as np
import pandas as pd
import json
import random

random.seed(1)



def fixGames (file):
    with open(file, 'r', encoding="utf8") as data_file:
        data = json.load(data_file)
    df = pd.json_normalize(data)

    df = df.drop_duplicates(subset=['name'])
    df.drop(columns=['year', 'description', 'publisher', 'url', 'image_url', 'rules_url',
    'min_players', 'max_players', 'min_age', 'max_age','min_time', 'max_time', 'cooperative',
    'expansion', 'num_votes', 'avg_rating', 'num_reviews', 'complexity' ], axis=1, inplace=True)
    

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

def fixUsers (file):
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


'''file = 'games.json'
fixGames(file)'''
file = "usersOK.json"
fixUsers(file)