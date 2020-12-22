# -*- coding: utf-8 -*-
"""creatingRandom.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1wftVcDs-IwSFL8yyl2WRqhPnwCnzVdLX
"""

import pandas as pd
import random 

df_giochi = pd.read_csv("boardgame.csv",index_col="_id")
df_utenti = pd.read_csv("users.csv",index_col = "_id")

df_categorie_1 = []
df_categorie_2 = []
df_eta = []

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

for row in df_utenti.iterrows():
  eta = -1

  while eta < 18:
    eta = round((random.random()*80),0) 

  index_1 = 0
  index_2 = 0
  while (index_1 <= 0 or index_2 <= 0) or (index_1 == index_2):
    index_1 = round(random.random()*len(categorie_trovate),0) - 1
    index_2 = round(random.random()*len(categorie_trovate),0) - 1

  df_categorie_1.append(categorie_trovate[int(index_1)])
  df_categorie_2.append(categorie_trovate[int(index_2)])
  df_eta.append(int(eta))
  
df_utenti["Eta"] = df_eta
df_utenti["Categoria1"] = df_categorie_1
df_utenti["Categoria2"] = df_categorie_2

df_utenti.to_csv("filledUser.csv")