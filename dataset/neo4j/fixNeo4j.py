import numpy as np
import pandas as pd
import json
import random


def fixNeo4j(file):
    df = pd.read_csv(file)
    df[['name','admin','description']]=df['name;admin;description'].str.split(";",expand=True,)
    df.drop(columns=['name;admin;description'], axis=1, inplace=True)
    print(df)

    df.to_csv('Groups.csv', index=False)  


fixNeo4j("Groups.csv")
