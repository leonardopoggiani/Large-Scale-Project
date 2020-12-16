//Modify Review
MATCH (:User)-[r:REVIEWED]->(:Game)
WHERE r.id = "4567"
SET r.text = "Bello schifo sto gioco!"