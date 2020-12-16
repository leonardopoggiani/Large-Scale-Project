//Delete Commented
MATCH (u:User)-[r:COMMENTED]->(a:Article)
WHERE r.id=1
DELETE r
RETURN r