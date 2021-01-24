import numpy as np
import pandas as pd
import json
import random


def fixNeo4j(file):
    df = pd.read_csv(file, sep="*")
    df[['title','idArt','name','category1']]=df['title;idArt;name;category1'].str.split(";",expand=True,)
    df.drop(columns=['title;idArt;name;category1'], axis=1, inplace=True)
    print(df)

    df.to_csv('referredArticle.csv', index=False)  


fixNeo4j("referredArticle1.csv")
