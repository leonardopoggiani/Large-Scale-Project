//Delete BE_PART
MATCH (u:User)-[b:BE_PART]->(gr:Group)
WHERE b.id="3"
DELETE b