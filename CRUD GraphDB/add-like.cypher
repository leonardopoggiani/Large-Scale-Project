//ADD LIKE
    match (u:User{id:"fd545634"}), (a:Article{id:1}) create (u)-[:LIKED]->(a)