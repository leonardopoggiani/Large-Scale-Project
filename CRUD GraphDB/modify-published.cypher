//MODIFY PUBLISHED
MERGE (:User { name: "Antonio Di Noia"})-[r:PUBLISHED]->(:Article{id: 1}) ON MATCH SET r.id = 1, r.timestamp = datetime()