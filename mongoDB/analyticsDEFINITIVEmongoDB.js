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


//TODO

//Mostrare gli utenti ordinati per ultimo login fatto, o mostrare i primi tot
//Serve all'admin per decidere gli utenti da eliminare
//Nella pagina Users dell'admin, dalla quale lui li può eliminare

//Istogramma con fasce d'età e gli utenti che vi appartengono.
//Per vedere se aggiungere o togliere giochi da giovani o da vecchi.


//Grafici a linea per vedere le giornate in cui c'è stata maggiore aflfuenza/attività