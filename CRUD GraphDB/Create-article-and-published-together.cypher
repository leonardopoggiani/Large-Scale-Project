//ADD :PUBLISHED RELATIONS
match (u:User{username:"Gaia4"}) create (u)-[:PUBLISHED{timestamp:datetime()}]->(a:Article{name: "Articolo Bello")
