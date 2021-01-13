//Se hai già più di 5 amici
//PERSONE CHE SONO AMICHE DEI TUOI AMICI MA CHE TU NON SEGUI ANCORA
MATCH (me:User{username:"Gaia5"})-[:FOLLOW]->(friend:User), (friend)-[:FOLLOW]-(me), (tizio:User{role:"normalUser"})
WHERE NOT((me)-[:FOLLOW]->(tizio)) AND (tizio)-[:FOLLOW]->(friend) AND (friend)-[:FOLLOW]->(tizio) AND NOT tizio.username="Gaia5"
RETURN tizio



//1)[schermata amici se non hai amici] suggest users according to categories ( va a guardare se c'è almeno una categoria in comune,
// ritorna lista utenti, divisione tra suggeriti (una in comune) [1] e *best match* (con tutte e due in comune) ) 
MATCH (ub:User{username:"Gaia5"}),(ua:User)
WHERE (ub.category_1=ua.category_1 or ub.category_1=ua.category_2)
or (ub.category_2=ua.category_1 or ub.category_2=ua.category_2)
RETURN ua

//DA MODIFICARE ENTRAMBE

//2.1) retrieve list of users' groups (NON È ADMIN)
MATCH (u:User{username:$username})-[b:BE_PART]->(gr:Group)-[r:REFERRED]->(ga:Game) 
WHERE NOT (gr.admin=$username)
RETURN u,b,gr,ga ORDER BY b.timestamp



//2) retrieve list of users' groups (Admin)
MATCH (u:User{username:$username})-[b:BE_PART]->(gr:Group{admin:$username})-[r:REFERRED]->(ga:Game)
RETURN u,b,gr,ga ORDER BY b.timestamp

//Timestamp last post on a group
MATCH (gr:Group{name:$name, admin:$admin})<-[p:POST]-(u:User)
RETURN p ORDER BY p.timestamp DESC LIMIT 1 





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

//Influencer con più followers o con + articoli scritti!!


//3.1) [moderator] suggest the promotion of a user to influencer ( [1] quanti amici ha [2] quante review ha fatto )
MATCH (u:User)<-[r:FOLLOW]->(u1:User)
WHERE u.role="normalUser" AND r.type="friend"
RETURN u.id, u.name,COUNT(r) AS CountFriends
ORDER BY CountFriends DESC

// 7) influencer who wrote more articles about different genres of game

MATCH (u:User {role:"influencer"})-[:PUBLISHED]->(a)-[:REFERRED]->(g: Game)
WITH u, COUNT(DISTINCT g.category_1) AS categorie1, COUNT( DISTINCT g.category_2) AS categorie2
RETURN u.name AS Influencer, SUM(categorie1 - categorie2) AS numeroCategorie
ORDER BY numeroCategorie DESC 
LIMIT 1

// 7.bis) se metto user e :reviewed allora diventa utile per promuvere gente ad influencer
MATCH (u:User {role:"user"})-[:REVIEWED]->(g)
WITH u, COUNT(DISTINCT g.category_1) AS categorie1, COUNT( DISTINCT g.category_2) AS categorie2
RETURN u.name AS User, SUM(categorie1 - categorie2) AS numeroCategorie
ORDER BY numeroCategorie DESC 
LIMIT 1


