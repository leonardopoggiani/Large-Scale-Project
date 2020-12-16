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
	
	
//#ANALYTIC

//#Show the game with the most number of rate for each year (pagina delle statistiche sui giochi o suggerimenti per i giochi)
db.Games.aggregate([
	{$match: 
		{"num_votes": { "$exists": true, "$ne": "" }}
	},
	{$group: 
		{_id: {name: "$name", year: "$year"}, totRate: {$sum: "$num_votes"}}
	},
	{$sort: {totRate:-1}},
	{$group: 
		{_id: "$_id.year",
		mostRated: {$first: "$_id.name"}
		}
	},
	{$sort:{_id:-1}}
])

//Show the game with the most number of rate for each category (pagina delle statistiche sui giochi)
db.Games.aggregate([
	{$match: 
		{"num_votes": { "$exists": true, "$ne": "" }}
	},
	{$unwind: "$category" },
	{$group: 
		{_id: {name: "$name", category: "$category"}, totRate: {$sum: "$num_votes"}}
	},
	{$sort: {totRate:-1}},
	{$group: {_id: "$_id.category", mostRated: {$first: "$_id.name"}}},
	{$sort:{_id:-1}}
])

//#Show the game with the most number of rate for each value of min_players (pagina delle statistiche sui giochi)
db.games.aggregate([
	{$match: 
		{"num_votes": { "$exists": true, "$ne": "" }, 
		"min_players": {"$exists": true, "$ne": null}
		}
	},
	{$group: 
		{_id: {name: "$name", min_players: "$min_players"}, totRate: {$sum: "$num_votes"}}
	},
	{$sort: {totRate:-1}},
	{$group: {_id: "$_id.min_players", mostRated: {$first: "$_id.name"}}}
])

//#Show the game with the most number of rate for each value of min_time
db.Games.aggregate([
	{$match: 
		{"num_votes": { "$exists": true, "$ne": "" }, 
		"min_time": {"$exists": true, "$ne": null}}
	},
	{$group: 
		{_id: {name: "$name", min_time: "$min_time"}, 
		totRate: {$sum: "$num_votes"}}
	},
	{$sort: {totRate:-1}},
	{$group: {_id: "$_id.min_time", mostRated: {$first: "$_id.name"}}}, 
	{$sort:{_id:1}}
])


//L'ho cambiata perchè l'altra doveva accedere a due collection, cosa che non esiste nella realtà, perchè
//Non avevamo la categoria dentro gli articoli, questa ho pensato che può essere utile per declassare un influencer
//Se non fa abbastanza articoli in un periodo

//avrà 1000 errori ma aspettavo la insert dell'article per provarla, e le date non so se si trattano così+
//Influencers who wrotes articles about less than 10 games in a period

// però questa query è un po' diversa in realtà:
// presi gli articoli pubblicati nell'ultimo mese (ad esempio) conta il numero di volte che un gioco è stato riferito da questi articoli, che potrebbe essere interessante
// da proporre nella schermata del gioco ma non riporta informazioni sugli autori
db.users.aggregate( 
[
  {$unwind: "$articles"}, 
  {$match: {"articles.timestamp": {"$lt": new Date("2020-12-31T00:00:00Z"),"$gt" : new Date("2020-12-01T00:00:00Z") } } },  
  {$unwind: "$articles.games"},
  {$group: {_id:"$articles.games", countGames: {$sum: 1}} },
  {$match: {countGames: {$lt: 10}}},
  {$sort: {countGames: 1}}
  ]
).pretty();

// (variante) Quante volte un utente ha recensito un gioco, potrebbe essere utile?
db.users.aggregate( 
[
  {$unwind: "$articles"}, 
  {$match: {"articles.timestamp": {"$lt": new Date("2020-12-31T00:00:00Z"),"$gt" : new Date("2020-12-01T00:00:00Z") } } },  
  {$unwind: "$articles.games"},
  {$group: {_id: {author: "$articles.author", game: "$articles.games"}, countAuthor: {$sum: 1}} },  ]
).pretty();


// questa sembra più simile a quello che hai scritto, trova il numero di giochi che un utente ha recensito in un tot periodo
db.users.aggregate( 
[
  {$unwind: "$articles"}, 
  {$match: {"articles.timestamp": {"$lt": new Date("2020-12-31T00:00:00Z"),"$gt" : new Date("2020-12-01T00:00:00Z") } } },  
  {$unwind: "$articles.games"},
  {$group: {_id: {author: "$articles.author", game: "$articles.games"}, countAuthor: {$sum: 1}} }, {$group: {_id: "$_id.author", countGames: {$sum: 1}} } ]
).pretty();

//### Query MongoDB su articles e groups ###

//1) aggiungi un articolo (profilo dell'infliencer)
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

//4) Distribuzione dei giochi per categoria (pagina delle statistiche sui giochi oppure sulla schermata del gioco)

db.boardgame.aggregate(
    [ 
        {$match: {category: {$ne: ''} } }, 
        {$unwind: "$category"}, 
        {$group: 
            {
                _id: "$category", 
                totalGames: {$sum: 1 }, 
                avgRating: {$avg: "$avg_rating"},
                avgNumVoter: {$avg: "$num_voters"} 
            } 
        }, 
        {$sort: {"totalGames": -1} } 
    ]
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
                "owner": "Leonardo",
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

//5) Giochi che hanno più gruppi che parlano di loro (statistiche sui giochi oppure suggerimenti su gruppi)

db.boardgame.aggregate(
    {$unwind: "$groups"}, 
    {$group: 
        {
            _id: "$name", 
            totGroups: {$sum: 1}
        }
    },
    {$sort: {totGroups: -1} }
)

// 6) Distribuzione dei commenti su gruppi per Autore (ad esempio nella pagina del profilo personale oppure per mostrare persona più attiva)
db.boardgame.aggregate(
    {$unwind: "$groups"},
    {$unwind: "$groups.posts"},
    {$group: {
        _id: "$groups.posts.author", 
        totPosts: {$sum: 1}} 
    }
)

// 7) Persona che modera più gruppi ( ad esempio nel profilo personale, potrebbe essere interessante vedere di quanti gruppi si è admin per poter proporre admin/moderatori)
db.boardgame.aggregate(
    {$unwind: "$groups"},
    {$group: 
        {
            _id: "$groups.owner", 
            totAdminGroups: {$sum: 1}
        }
    }
)


