//DA SCEGLIERE UNA DELLE DUE

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

//DA MODIFICARE ENTRAMBE

//2.1) retrieve list of users' groups (NON È ADMIN)
MATCH (u:User)-[b:BE_PART]->(gr:Group)
WHERE u.id='asdfg'
RETURN gr

//DA MODIFICARE

//2) retrieve list of users' groups (Admin)
MATCH (u:User)-[b:BE_PART]->(gr:Group)
WHERE u.id='asdfg' and b.admin="si"
RETURN gr



//3.1) [moderator] suggest the promotion of a user to influencer ( [1] quanti amici ha [2] quante review ha fatto )
MATCH (u:User)<-[r:FOLLOW]->(u1:User)
WHERE u.role="normalUser" AND r.type="friend"
RETURN u.id, u.name,COUNT(r) AS CountFriends
ORDER BY CountFriends DESC


//DA MODIFICARE DATA E ORA

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

//FARE CONTROLLO SE NON SEGUE NESSUN INFLUENCER QUESTA QUERY QUA SOTTO ALTRIMENTI
//SOLO GLI ARTICOLI SCRITTA DAGLI INFLUENCERS CHE SEGUE QUESTO TIZIO

//Mostra tutti gli articoli, ordinati per data, che riguardano almeno 
// un gioco con la stessa categoria di quelle preferite dall'utente
MATCH (i:User)-[p:PUBBLISHED]->(a:Article)-[r:REFERRED]->(g:Game),(u:User)
WHERE u.username="Gaia4" AND ((g.category1 = u.category1 OR g.category1 = u.category2)
OR (g.category2 = u.category1 OR g.category2 = u.category2))
RETURN a.name, u.username, p.timestamp
ORDER BY p.timestamp


// ---- sul graphDB ----
//Nella pagina delle statistiche dell'admin
//TRADURRE IN NEO4J
//7)Users ordinati in base a di quanti gruppi sei admin
/*db.boardgame.aggregate(
    {$unwind: "$groups"},
    {$group: 
        {
            _id: "$groups.owner", 
            totAdminGroups: {$sum: 1}
        }
    }
)*/

