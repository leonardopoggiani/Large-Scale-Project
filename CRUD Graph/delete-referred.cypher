//DELETE REFERRED
match (a:Article)-[r:REFERRED]->(g:Game) 
where a.id = 1 and g.id = "1234"
delete r
return r