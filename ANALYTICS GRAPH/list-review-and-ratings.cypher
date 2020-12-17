// 10) [schermata del gioco] show reviews and ratings of a game
// 
MATCH (u:User)-[re:REVIEWED]->(g:Game)<-[ra:RATED]-(u:User)
RETURN g.name AS Gioco, re.text AS TestoReview, ra.vote AS Voto, u.name AS Autore