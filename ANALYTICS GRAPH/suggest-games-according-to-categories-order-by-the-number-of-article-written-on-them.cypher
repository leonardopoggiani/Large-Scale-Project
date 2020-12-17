//suggest games according to categories order by the number of article written on them
match (g:Game)<-[r:REFERRED]-(a:Article) where g.category_1 = "Card Game" or g.category_2="Mafia"
RETURN g.name, count(r) as popularity
ORDER BY popularity DESC