//CREATE GROUP AND REFERRED
MATCH (u:User{username:"Gaia5"}),(ga:Game{name:"Spirit Island"})
CREATE (u)-[b:BE_PART{timestamp:"2021-01-02 17:40:43.783"}]->(gr:Group{name:"Gruppo Mio2", admin:"Gaia5", description:"bla bla"})-[:REFERRED]->(ga)
return u,b,gr
