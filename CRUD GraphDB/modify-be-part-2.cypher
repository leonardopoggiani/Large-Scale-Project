//Modify BE_PART 2
MATCH (u:User)-[b:BE_PART]->(g:Group)
WHERE b.id="12"
SET b.admin='si'