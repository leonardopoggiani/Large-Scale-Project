import numpy as np
import pandas as pd
import json

def arrangeGames (file):
    with open(file, 'r', encoding="utf8") as data_file:
        data = json.load(data_file)
    df = pd.json_normalize(data)

    df = df.drop_duplicates(subset=['name'])
    df.drop(columns=['image_file', 'bgg_id', 'easiest_complexity', 'hardest_complexity', 'highest_language_dependency', 'integration',
    'language_dependency', 'lowest_language_dependency', 'luding_id', 'max_players_best', 'max_players_rec', 'mechanic', 'min_players_best',
    'min_players_rec', 'rank', 'video_url', 'worst_rating', 'scraped_at', 'stddev_rating', 'family', 'best_rating', 'external_link',
    'game_type', 'review_url', '_id.$oid'], axis=1, inplace=True)
    df['num_reviews'] = 0
    df['rules_url'] = ""
    df['avg_rating'] = df['avg_rating'].map(lambda x: '{0:.1f}'.format(x)) 


    result = df.to_json(orient="records", double_precision=0)
    parsed = json.loads(result)

    
    #print(json.dumps(parsed, indent = 4, sort_keys=True))
    
    
    with open('games.json', 'w', encoding='utf-8') as f:
        json.dump(parsed, f, ensure_ascii=False, indent=4)



file = "boardgame.json"
arrangeGames(file)
