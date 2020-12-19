//DELETE RATE
match (u:User)-[r:RATED]->(g:Game) 
where g.id = "1234" and u.id = "fd545634"
delete r
return r