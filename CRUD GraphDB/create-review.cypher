//Create Review
MATCH (u:User),(g:Game)
WHERE u.id = 'fdsf934' AND g.id = '1234'
CREATE (u)-[r:REVIEWED {text:"Carinissimo davvero!", timestamp:datetime(), id:"4567"}]->(g)
RETURN r