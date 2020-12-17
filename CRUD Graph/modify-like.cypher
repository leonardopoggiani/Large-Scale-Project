//MODIFY LIKE
MERGE (:User { name: "Leonardo Poggiani"})-[r:LIKED]->(:Article{id: 1}) ON MATCH SET r.timestamp = datetime()