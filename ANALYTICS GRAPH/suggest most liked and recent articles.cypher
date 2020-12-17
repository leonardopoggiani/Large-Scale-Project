match (u:User)-[r1:PUBLISHED]->(a:Article)<-[r:LIKED]-(u1:User) where r.status = "like" 
RETURN a.name, count(r) as LikeCount, r1.timestamp as ReleasedDate
ORDER BY  ReleasedDate, LikeCount DESC
