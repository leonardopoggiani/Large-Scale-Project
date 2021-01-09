//#ANALYTICs

//Per decidere se eliminarlo
//# Show the game with less number of rate for each year (pagina delle statistiche sui giochi o suggerimenti per i giochi)
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


//Se ipoteticamente uno non specifica le sue categorie preferite allora mostro i giochi con le categorie + popolari
//Diagramma a torta forse, sul numero di giochi, 5 e altre
//4) informazioni su una particolare categoria di giochi (schermata statistiche sui giochi)

db.boardgame.aggregate(
    [ 
        {$match: {category: {$ne: ''} } }, 
        {$unwind: "$category"}, 
        {$group: 
            {
                _id: "$category", 
                totalGames: {$sum: 1 }, 
                avgRating: {$avg: "$avg_rating"},
                avgNumVoter: {$avg: "$num_votes"} // aggiungere roba
            } 
        }, 
        {$sort: {"totalGames": -1} } 
    ]
)

//Un'altra simile per la torta con solo i totalGames

// ---- sul graphDB ----
//5) Giochi che hanno più gruppi che parlano di loro (statistiche sui giochi)
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

// --------

/*// 6) Distribuzione dei post su gruppi per Autore (Per mostrare persona più attiva)
db.boardgame.aggregate(
    {$unwind: "$groups"},
    {$unwind: "$groups.posts"},
    {$group: {
        _id: "$groups.posts.author", 
        totPosts: {$sum: 1}} 
    }
)

// 6.bis) Autore di più posts in ogni gruppo
*/

//GraphDB 
//Sui gruppi di cui un utente, passato, è admin, ordinarli per timestamp dell'ultimo post e ritornarli
//Dividere la query che restituisce i gruppi in due.
//Fare anche la delete group

// ---- sul graphDB ----
//Nella pagina delle statistiche dell'admin
// 7) Persona che modera più gruppi
db.boardgame.aggregate(
    {$unwind: "$groups"},
    {$group: 
        {
            _id: "$groups.owner", 
            totAdminGroups: {$sum: 1}
        }
    }
)

// -------------
//Pagina delle statistiche dell'admin
//Numero di giocatori per paese
//Per esempio sulle prime 5/6 e altre
db.users.aggregate( 
    {$match: {country: {$ne: null} } }, 
    {$group: 
        { _id: "$country", totUsers: {$sum: 1} } 
    },
    {$sort: {totUsers: -1}}
)