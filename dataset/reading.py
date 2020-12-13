import json_lines
import pandas as pd
import json

list = []

with open('2020-12-02T11-22-52.jl', 'rb') as f:
    for item in json_lines.reader(f):
        list.append(item)

json.dumps(list)

with open("bga.json", "w") as outfile:
    json.dump(list, outfile, indent=4)