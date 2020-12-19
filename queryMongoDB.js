//######MONGODB########

//#QUERY

//1) ADD GAME (form nella parte dell'amministratore)
db.Games.insertOne({
  "name": "Spirit Island",
  "year": 2016,
  "description": "Powerful Spirits have existed on this isolated island for time immemorial. They are both part of the natural world and - at the same time - something beyond nature. Native Islanders, known as the Dahan, have learned how to co-exist with the spirits, but with a healthy dose of fear and reverence. However, now, the island has been \"discovered\" by invaders from a far-off land. These would-be colonists are taking over the land and upsetting the natural balance, destroying the presence of Spirits as they go. As Spirits, you must grow in power and work together to drive the invaders from your island... before it's too late!",
  "publisher": [
    "id",
    "name",
    "url",
    "num_games",
    "score",
    "game",
    "images"
  ],
  "url": "https://www.boardgameatlas.com/game/kPDxpJZ8PD/spirit-island",
  "image_url": [
    "https://s3-us-west-1.amazonaws.com/5cc.images/games/uploaded/1559254941010-61PJxjjnbfL.jpg"
  ],
  "rules_url": [
    "https://drive.google.com/file/d/0B9kp130SgLtdcGxTcTFodlhaWDg/view"
  ],
  "external_link": [
    "https://store.greaterthangames.com/spirit-island.html?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads"
  ],
  "list_price": "USD79.95",
  "min_players": 1,
  "max_players": 4,
  "min_players_rec": null,
  "max_players_rec": null,
  "min_players_best": null,
  "max_players_best": null,
  "min_age": 13,
  "max_age": null,
  "min_time": 90,
  "max_time": 120,
  "category": [
    "nWDac9tQzt",
    "gsekjrPJz0",
    "ZTneo8TaIO",
    "upXZ8vNfNO",
    "MHkqIVxwtx",
    "nuHYRFmMjU",
    "buDTYyPw4D"
  ],
  "mechanic": [
    "PGjmKGi26h",
    "05zCZoLvQJ",
    "9mNukNBxfZ",
    "WPytek5P8l",
    "U3zhCQH7Et",
    "lA3KUtVFCy",
    "DEvPj5twid",
    "XM2FYZmBHH"
  ],
  "cooperative": null,
  "num_votes": 280,
  "avg_rating": 3.93871829105474,
  "stddev_rating": null,
  "bayes_rating": null,
  "worst_rating": 1,
  "complexity": null,
  "easiest_complexity": null,
  "hardest_complexity": null,
  "language_dependency": null,
  "lowest_language_dependency": null,
  "highest_language_dependency": null,
  "bgg_id": null,
  "luding_id": null,
  "bga_id": "kPDxpJZ8PD",
  "scraped_at": "2020-12-02T11:23:38+0000"
})
	
//2) DELETE GAME (menù a scorrimento nella parte dell'amministratore)
db.Games.deleteMany({
	"name": "Spirit Island"
})
	
db.Games.deleteMany({
	"name": "Drei Haselnüsse für Aschenbrödel - Das Spiel"
})
	
	
//3) MODIFY A GAME (menù nella parte dell'amministratore)
db.Games.updateOne({
	"name": "Spirit Island"},
	{
		$set: {"year": 2015, "min_players" : 2}
	}
)


//INSERT ONE OR MORE USERS ( form per registrarsi )
db.users.insertOne(
  {"firstName": "Clarissa", "last_name": "Polidori", "registered": "2020", "last_login": "", "country":"Italy"}, 
);

db.users.insertMany([
{"firstName": "Ester", "last_name": "Pivirotto", "registered": "2020","last_login": "2020-11-30T00:00:00+0000", "country":"Italy"},
{"firstName": "Ester", "last_name": "Pivirotto", "registered": "2020","last_login": "2020-11-30T00:00:00+0000", "country":"Italy"},
{"firstName": "Ester", "last_name": "Pivirotto", "registered": "2020","last_login": "2020-11-30T00:00:00+0000", "country":"Italy"}, 
]);

//DELETE USER ONE OR MORE USERS (parte dell'amministratore E gestione del profilo dell'utente, deve potersi disiscrivere?)
db.users.deleteMany({name: "Ester" })

db.users.deleteOne({name: "Ester" })

//MODIFY USER (gestione del profilo dell'utente)
db.users.update(
{"item_id" : ""},
{$set: [{ "name" : "Veronica"}, { "surname" : "Veronica"}]}
);
	
	


//### Query MongoDB su articles e groups ###

//1) aggiungi un articolo (profilo dell'influencer)
db.users.updateOne(
    {bgg_user_name: "microcline"},
    {$push: 
        {articles: 
            {
                "title": "Nuovo articolo6", 
                "body": "Testo del nuovo articolo", 
                "timestamp": new Date(), 
                "author": "Autore", 
                "games": ["gioco1", "gioco2"], 
                "num_comments": 0, 
                "num_like": 0, 
                "num_dislike": 0
            }                   
        }
    }  
)

//2) cancella un articolo (profilo dell'influencer)
db.users.updateOne( 
    { bgg_user_name: "microcline" }, 
    { $pull: 
        { articles:
            { "title": "Nuovo articolo6"}
        }  
    } 
)

//3) modificare un articolo (qua solo il titolo all'occorrenza quello che si vuole) (profilo dell'influencer)

db.users.updateOne(
    { bgg_user_name: "microcline","articles.title": "Nuovo articolo"}, 
    {$set : {"articles.$.title" : "Articolo modificato"} }
)


//### GROUPS ### 
//1) creare un gruppo (lista degli amici)
db.boardgame.updateOne(
    {name: "Zombicide"},
    {$push: 
        {groups: 
            {
                "name": "Gruppo su Zombicide",
                "description":"In questo gruppo si parla di Zombicide",
                "creation_timestamp": new Date(),
                // "owner": "Leonardo",
                posts: [] 
            }
        }
    }
)

//2) aggiungere un post ad un gruppo (lista dei gruppi o schermata del gruppo)
db.boardgame.updateOne( 
    { name: "Renegade","groups.name":  "Gruppo su Renegade"}, 
    { $push: 
        { "groups.$.posts":
                {"text": "Questo è un nuovo post","timestamp": new Date(), "author": "Leonardo"}
        }  
    } 
)

//3)  rimuovere post da un Gruppo (schermata del gruppo da parte dell'amministratore)
db.boardgame.updateOne( 
    { name: "Renegade", "groups.name": "Gruppo su Renegade" }, 
    { $pull: 
        { "groups.$.posts":
                {"text": "Questo è un nuovo post", "author": "Leonardo"}
        }  
    } 
)

//4) Rimuovere un Gruppo (proprietario del gruppo o moderatore da schermata del gruppo = riepilogo messaggi)
db.boardgame.updateOne( 
    { name: "Renegade" }, 
    { $pull: 
        { groups:
            { "name": "Gruppo su Renegade2"}
        }  
    } 
)

// FIND QUERIES
// 1) Trovare i giochi con valutazione superiore a tot ( schermata dei giochi, indice su valutazione?  )
db.boardgame.find( 
    { avg_rating: {$gt: 7} } 
)

// 2) Trovare i giochi con un range di valutazione
db.boardgame.find( 
    { avg_rating: {$lt: 8, $gt: 5}}
)

// 3) Trovare i 3 giochi con valutazione più alta per tipo (schermata dei giochi)
db.boardgame.find( 
    {category: "Math:1104", avg_rating: {$ne: null}}
).sort( {avg_rating: -1} ).limit(3)

// 4) Trovare 3 giochi con valutazione più bassa per tipo (schermata dei giochi)
db.boardgame.find( 
    {category: "Math:1104", avg_rating: {$ne: null}}
).sort( {avg_rating: 1} ).limit(3)

// 5) Trovare i giochi in un range di difficoltà
db.boardgame.find( 
    { complexity: {$lt: 3, $gt: 2}}
)

// 6) Giochi con maggior numero di votanti
db.boardgame.find().sort({num_votes: -1}).limit(3)

// 7) Giochi più recenti
db.boardgame.find(
    { year: {$gt: 2019} }
).sort({year: -1})

// 8) Utenti registrati da più tempo
db.users.find().sort({registered: 1}).limit(3)

// 9) Utenti di un determinato paese
db.users.find( {country: "United States"} )

// 10) Lista articoli 
db.users.aggregate( {$match: {articles: {$ne: null}} }, {$project: {bgg_user_name: 1, articles: 1} } )

// 11) Articoli con il testo
db.users.aggregate( {$match: {articles: {$ne: null}} }, {$project: {bgg_user_name: 1, "articles.body": 1} } )

// crea index per avg_rating
db.boardgame.createIndex({avg_rating: 1}, {name: "query per rating"})

db.boardgame.createIndex({year: 1}, {name: "query per year"})

db.boardgame.createIndex({num_votes: 1}, {name: "query per num_votes"})
