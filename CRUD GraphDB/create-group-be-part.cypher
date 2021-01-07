//Create Group and be part
//MATCH (u:User{id:"asdfg"}),(ga:Game{id:"1234"})
//CREATE (u)-[:BE_PART[timestamp:datetime(), admin:"si"}]->(gr:Group {name:"Gruppo brutto"})-[:REFERRED]->(ga)


//Create Group and be part
MATCH (u:User{username:"sara"}),(ga:Game{name:"Spirit Island"})
CREATE (u)-[:BE_PART{timestamp:"2021-01-03 17:40:43.783"}]->(gr:Group {name:"Gruppo brutto",description:"bla bla bla", admin:"Gaia5"})-[:REFERRED]->(ga)
