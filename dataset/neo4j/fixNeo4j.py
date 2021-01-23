import numpy as np
import pandas as pd
import json
import random


def fixNeo4j(file):
    df = pd.read_csv(file)
    df[['title','idArt']]=df["title;idArt"].str.split(";",expand=True,)
    df.drop(columns=['title;idArt'], axis=1, inplace=True)
    print(df)

    df.to_csv('article.csv', index=False)  


fixNeo4j("article.csv")
