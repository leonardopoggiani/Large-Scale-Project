//ADD RATE
match (u:User{id:"asdf12"}), (g:Game{id:"1234"}) 
where not exists ((u:User)-[:RATED]->(g:Game))
create (u)-[:RATED {vote: 5}]->(g)

// guardare se si può mettere vincolo su numero di rate degli utenti