match (u:User)-[:PUBLISHED]->(a:Article)<-[r:LIKED]-(u1:User) 
where r.status = "like"  
return u.name, count(r) as likeCount 
order by likeCount desc 
limit 3

// per dislike
match (u:User)-[:PUBLISHED]->(a:Article)<-[r:LIKED]-(u1:User) 
where r.status = "dislike"  
return u.name, count(r) as likeCount 
order by likeCount desc 
limit 3
