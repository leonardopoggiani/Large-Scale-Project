//Delete follow
MATCH (u:User{name:"Leonardo Poggiani"})-[r:FOLLOW]->(p1:User{name:"Clarissa Polidori"})
DELETE r
RETURN r