//ADD :FOLLOW RELATION
match (p:User{name:"Leonardo Poggiani"}),(p1:User{name:"Antonio Di Noia"}) create (p)-[:FOLLOW]->(p1)