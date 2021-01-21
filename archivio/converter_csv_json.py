import numpy as np
import pandas as pd
 

#Converter from csv to JSON
def conversion_csv_json(file):
    csv = pd.read_csv(file, sep=',')
    with open('.\csv\GameItem_bga.json', 'w') as f:
        f.write(csv.to_json(orient='records', lines=True))


file = ".\csv\GameItem_bga.csv"
conversion_csv_json(file)


