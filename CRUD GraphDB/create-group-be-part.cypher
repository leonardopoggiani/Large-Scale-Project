//Create Group and be part
MATCH (u:User{id:"asdfg"}),(ga:Game{id:"1234"})
CREATE (u)-[:BE_PART{id:"3", timestamp:datetime(), admin:"si"}]->(gr:Group {name:"Gruppo brutto",id:"14"})-[:REFERRED{id:"3"}]->(ga)
