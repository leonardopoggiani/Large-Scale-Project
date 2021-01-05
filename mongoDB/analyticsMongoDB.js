//#ANALYTICs

//# Show the game with the most number of rate for each year (pagina delle statistiche sui giochi o suggerimenti per i giochi)
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

// ---- sul graphDB ----
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

// --------

// 6) Distribuzione dei commenti su gruppi per Autore (ad esempio nella pagina del profilo personale oppure per mostrare persona più attiva)
db.boardgame.aggregate(
    {$unwind: "$groups"},
    {$unwind: "$groups.posts"},
    {$group: {
        _id: "$groups.posts.author", 
        totPosts: {$sum: 1}} 
    }
)

// 6.bis) Autore di più commenti in ogni gruppo


// ---- sul graphDB ----
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

// -------------

// numero di giocatori per paese
db.users.aggregate( 
    {$match: {country: {$ne: null} } }, 
    {$group: 
        { _id: "$country", totUsers: {$sum: 1} } 
    },
    {$sort: {totUsers: -1}}
)