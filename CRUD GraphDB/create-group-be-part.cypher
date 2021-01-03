//Create Group and be part
MATCH (u:User{id:"asdfg"}),(ga:Game{id:"1234"})
CREATE (u)-[:BE_PART[timestamp:datetime(), admin:"si"}]->(gr:Group {name:"Gruppo brutto"})-[:REFERRED]->(ga)
