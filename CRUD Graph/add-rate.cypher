//ADD RATE
match (u:User{id:"fd545634"}), (g:Game{id:"1234"}) create (u)-[:RATED]->(g)