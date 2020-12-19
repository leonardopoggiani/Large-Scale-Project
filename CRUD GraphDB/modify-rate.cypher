//MODIFY RATE
MERGE (:User { id: "fd545634"})-[r:RATED]->(g:Game{id:"1234"}) ON MATCH SET r.timestamp = datetime(), r.id=1, r.value = 5
