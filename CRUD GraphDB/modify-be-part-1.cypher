//Modify BE_PART 1
MATCH (u:User)-[b:BE_PART]->(g:Group)
WHERE u.id="fd545634"and g.id="12"
SET b.admin='si'