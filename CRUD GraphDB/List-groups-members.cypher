//LIST GROUPS members
MATCH (gr:Group{name:"Gruppo Mio2", admin:"sara"})<-[b:BE_PART]-(u:User)
RETURN u
