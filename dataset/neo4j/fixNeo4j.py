import numpy as np
import pandas as pd
import json
import random


def fixNeo4j(file):
    df = pd.read_csv(file, sep="*")
    df[['category2','role','category1','username','vote','timestamp','category2-2','name','category1-2']]=df['category2;role;category1;username;vote;timestamp;category2-2;name;category1-2'].str.split(";",expand=True,)
    df.drop(columns=['category2;role;category1;username;vote;timestamp;category2-2;name;category1-2'], axis=1, inplace=True)
    print(df)

    df.to_csv('rated.csv', index=False)  


fixNeo4j("rated.csv")
