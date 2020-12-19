//MODIFY COMMENTED
MERGE (:User { name: "Leonardo Poggiani"})-[r:COMMENTED]->(:Article{id: 1}) ON MATCH SET r.id = 1, r.timestamp = datetime(), r.text="Articolo interessante"