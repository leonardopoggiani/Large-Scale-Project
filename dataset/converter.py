import pandas as pd

df = pd.read_json("2020-12-01T15-17-12.jl",lines=True)
df.to_csv("trasformati.csv")

