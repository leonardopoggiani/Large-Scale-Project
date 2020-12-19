//Delete Published
MATCH (u:User)-[r:PUBLISHED]->(a:Article)
WHERE r.id=1
DELETE r
RETURN r