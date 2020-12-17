MATCH (u:User)-[:BE_PART]-()-[:BE_PART]-(member)
WHERE u.name = "Dania Scattino"
RETURN DISTINCT member.name