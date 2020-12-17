//CREATE REFERRED
match (a:Article{id:1}), (g:Game{id:"1234"}) create (a)-[:REFERRED]->(g)