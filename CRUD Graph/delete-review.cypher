//Delete Review
MATCH (u:User)-[r:REVIEWED]->(g:Game)
WHERE r.id="4567"
DELETE r
RETURN r