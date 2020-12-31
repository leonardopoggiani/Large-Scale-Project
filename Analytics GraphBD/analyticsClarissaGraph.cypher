//1.1)[schermata amici se non hai amici] suggest users according to categories 
// ( va a guardare se c'è almeno una categoria in comune, ritorna lista utenti, divisione tra suggeriti 
//  *best match* (con tutte e due in comune) [1.1] ) 

MATCH (ub:User),(ua:User)
WHERE ub.id="fd545635" AND NOT EXISTS ((ub)-[:FOLLOW]->(ua))
AND NOT EXISTS((ua)-[:FOLLOW]->(ub)) AND
((ub.category_1=ua.category_1 or ub.category_1=ua.category_2) AND
(ub.category_2=ua.category_1 or ub.category_2=ua.category_2))
RETURN ua

//1)[schermata amici se non hai amici] suggest users according to categories ( va a guardare se c'è almeno una categoria in comune,
// ritorna lista utenti, divisione tra suggeriti (una in comune) [1] e *best match* (con tutte e due in comune) ) 
MATCH (ub:User),(ua:User)
WHERE ub.id="fd545635" AND NOT EXISTS ((ub)-[:FOLLOW]->(ua))
AND NOT EXISTS((ua)-[:FOLLOW]->(ub)) AND
(ub.category_1=ua.category_1 or ub.category_1=ua.category_2
or ub.category_2=ua.category_1 or ub.category_2=ua.category_2)
RETURN ua


//2.1) retrieve list of users' groups (All)
MATCH (u:User)-[b:BE_PART]->(gr:Group)
WHERE u.id='asdfg'
RETURN gr


//2) retrieve list of users' groups (Admin)
MATCH (u:User)-[b:BE_PART]->(gr:Group)
WHERE u.id='asdfg' and b.admin="si"
RETURN gr

//3.1) [moderator] suggest the promotion of a user to influencer ( [1] quanti amici ha [2] quante review ha fatto )
MATCH (u:User)<-[r:FOLLOW]->(u1:User)
WHERE u.role="normalUser" AND r.type="friend"
RETURN u.id, u.name,COUNT(r) AS CountFriends
ORDER BY CountFriends DESC

//3) [moderator] suggest the promotion of a user to influencer ( [1] quanti amici ha [2] quante review ha fatto )
MATCH (u:User)-[r:REVIEWED]->(ga:Game)
WHERE u.role="normalUser" AND r.timestamp >= datetime({year:2020, month:1, day:1}) AND r.timestamp <= datetime({year:2020, month:12, day:31})
RETURN u.id, u.name,COUNT(r) AS CountReviews
ORDER BY CountReviews DESC

//How many articles an influencer pubblished in a period, ordered
MATCH (u:User)-[p:PUBLISHED]->(a:Article)-[r:REFERRED]->(g:Game)
WHERE u.role="influencer" //Non servirebbe 
AND p.timestamp >= datetime({year:2020, month:1, day:1})
AND p.timestamp <= datetime({year:2020, month:12, day: 31})
RETURN u.name,  count (distinct g) AS countGames
ORDER BY countGames ASC

//Influencers who wrotes articles about less than 10 games in a period, ordered
MATCH (u:User)-[p:PUBLISHED]->(a:Article)-[r:REFERRED]->(g:Game)
WHERE u.role="influencer" 
AND p.timestamp >= datetime({year:2020, month:1, day:1})
AND p.timestamp <= datetime({year:2020, month:12, day: 31})
WITH u, count (distinct g) AS countGames
WHERE countGames <10
RETURN u.name, countGames
ORDER BY countGames ASC

//SERVE SOLO SE LE RECENSIONI SE NE PUÒ FARE PIÙ DI UNA
//Quante volte un utente ha recensito un gioco, potrebbe essere utile?
MATCH (u:User)-[r:REVIEWED]->(g:Game)
WHERE u.id="asdfg"AND g.id="14"
RETURN u.name, count(r) AS countReviewsGame

//NON LA USIAMO PIÙ PER ORA USIAMO QUELLA SOTTO 

//Mostra tutti gli articoli, ordinati per data, che riguardano almeno 
// un gioco con la stessa categoria dei giochi dell'articolo in questione
MATCH (aq:Article)-[rq:REFERRED]->(gq:Game),(a:Article)-[r:REFERRED]->(g:Game), (a)<-[p:PUBLISHED]-(u:User)
WHERE aq.id=2 AND ((g.category_1 = gq.category_1 OR g.category_1 = gq.category_2)
OR (g.category_2 = gq.category_1 OR g.category_2 = gq.category_2))
RETURN a.name, p.timestamp, u.name
ORDER BY p.timestamp


//Mostra tutti gli articoli, ordinati per data, che riguardano almeno 
// un gioco con la stessa categoria dei giochi preferiti dall'utente
MATCH (i:User)-[p:PUBBLISHED]->(a:Article)-[r:REFERRED]->(g:Game),(u:User)
WHERE u.username="Gaia4" AND ((g.category1 = u.category1 OR g.category1 = u.category2)
OR (g.category2 = u.category1 OR g.category2 = u.category2))
RETURN a.name, u.username, p.timestamp
ORDER BY p.timestamp
