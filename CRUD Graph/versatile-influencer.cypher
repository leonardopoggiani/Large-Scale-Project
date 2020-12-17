// 7) influencer who wrote more articles about different genres of game

MATCH (u:User {role:"influencer"})-[:PUBLISHED]->(a)-[:REFERRED]->(g: Game)
WITH u, COUNT(DISTINCT g.category_1) AS categorie1, COUNT( DISTINCT g.category_2) AS categorie2
RETURN u.name AS Influencer, SUM(categorie1 - categorie2) AS numeroCategorie
ORDER BY numeroCategorie DESC 
LIMIT 1