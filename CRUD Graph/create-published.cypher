//ADD :PUBLISHED RELATIONS
match (p:User{name:"Antonio Di Noia"}),(a:Article{id:1}) create (p)-[:PUBLISHED]->(a)