9) [bacheca] show comment list and number of like below an article (mettere tipo nel Like) 
// lista commenti
MATCH (a:Article)<-[c:COMMENTED]-(u:User) 
RETURN a.title AS Articolo, c.text AS Commenti

// numero LIKE
MATCH (u:User)-[l:LIKED {type: "like"}]->(a:Article)
RETURN a.title AS Articolo, COUNT(*) AS LIKE

// numero DISLIKE
MATCH (u:User)-[l:LIKED {type: "dislike"}]->(a:Article)
RETURN a.title AS Articolo, COUNT(*) AS LIKE