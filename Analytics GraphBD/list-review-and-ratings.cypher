// 10) [schermata del gioco] show reviews and ratings of a game
// 
MATCH (u:User)-[re:REVIEWED]->(g:Game)<-[ra:RATED]-(u:User)
RETURN g.name AS Gioco, re.text AS TestoReview, ra.vote AS Voto, u.name AS Autore

MATCH (u:User)-[re:REVIEWED]->(g:Game)
RETURN g.name AS Gioco, re.text AS TestoReview, u.name AS Autore

MATCH (u:User)-[r:RATED]->(g:Game)
WHERE g.id = 3
RETURN g.name AS Gioco, avg(r.vote) AS Voto, u.name AS Autore