//Create BE_PART
MATCH (u:User),(gr:Group)
WHERE u.username = "sara" AND gr.name = "Gruppo mio" AND gr.admin="Gaia5"
CREATE (u)-[r:BE_PART {timestamp:"2021-01-06 13:34:12.89"}]->(gr)
RETURN r
