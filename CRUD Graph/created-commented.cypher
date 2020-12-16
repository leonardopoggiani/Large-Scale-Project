//ADD :COMMENTED RELATION
match (p:User{name:"Leonardo Poggiani"}),(a:Article{id:1}) create (p)-[:COMMENTED]->(a)