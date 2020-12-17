//DELETE LIKE
match (u:User)-[r:LIKED]->(a:Article) 
where a.id = 1 and u.id = "fd545634"
delete r
return r