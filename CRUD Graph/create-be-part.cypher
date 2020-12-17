//Create BE_PART
MATCH (u:User),(gr:Group)
WHERE u.id = 'fd545634' AND gr.id = '12'
CREATE (u)-[r:BE_PART {timestamp: datetime(),id:"1", admin:"fd545635"}]->(gr)
RETURN r