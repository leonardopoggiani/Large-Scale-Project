import numpy as np
import pandas as pd
import json
import random


def fixNeo4j(file):
    df = pd.read_csv(file)
    df[['category2','role','category1','username','type','timestamp','title','idArt']]=df['category2;role;category1;username;type;timestamp;title;idArt'].str.split(";",expand=True,)
    df.drop(columns=['category2;role;category1;username;type;timestamp;title;idArt'], axis=1, inplace=True)
    print(df)

    df.to_csv('liked.csv', index=False)  


fixNeo4j("liked.csv")
