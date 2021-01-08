MATCH (u:User)-[b:BE_PART]->(gr:Group)-[r:REFERRED]->(ga:Game)
WHERE gr.name="Gruppo mio" and  gr.admin="Gaia5"
DELETE b,gr,r
