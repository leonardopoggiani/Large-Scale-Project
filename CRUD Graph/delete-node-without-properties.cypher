//Delete Node without properties
match (n)
WHERE NOT (EXISTS (n.id))
DELETE n