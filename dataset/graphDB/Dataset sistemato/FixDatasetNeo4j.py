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


file = 'games.json'
fixGames(file)
'''file = "users.json"
fixUsers(file)'''