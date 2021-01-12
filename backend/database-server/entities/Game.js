const Neo4j = require('../modules/neo4j')

class Game {
  async getAll(username, league) {
    let query = 'MATCH (u:User{username: $username})-[p:PLAYED_GAME]->(g:Game)';
    
    if (league !== "undefined") {
      if (league === "No league") {
        query += " WHERE NOT (g)-[:IN_LEAGUE]->()"
      } else {
        query += "-[:IN_LEAGUE]-(l:League {league:$league})";
      }
    }

    const res = (await Neo4j.query(query + `RETURN p, g`, { username, league }))
    return res.map(o => ({...o.g.properties, ...o.p.properties}))
  }

  async getLeaderboard(league) {
    let query = 'MATCH (u:User)-[p:PLAYED_GAME]->(:Game)';

    if (league !== "undefined") {
      query += "-[:IN_LEAGUE]-(l:League {league:$league})";
    }

    const res = (await Neo4j.query(query + " RETURN u{.*,games:collect(p)}", { league }))
    const temp = res.map(o => (
                          {avatar: o.u.avatar,
                          username: o.u.username,
                          wins: o.u.games.filter(g => g.properties.winner).length,
                          losses: o.u.games.filter(g => !g.properties.winner).length
                          }))

    return temp.sort((a, b) => (b.wins - b.losses) - (a.wins - a.losses)).slice(0, 9);
  }

  async saveGame(body) {
    let queryString = `MATCH (u1: User{ username: $name1 }), (u2: User{ username: $name2 })
    CREATE (u1)-[pg1: PLAYED_GAME{ points: $points1, winner: $winner1, isStartPlayer: true }]->(g:Game{date: timestamp(), status: 'Finished', pointsToWin: $pointsToWin})
    MERGE (u2)-[pg2: PLAYED_GAME{ points: $points2, winner: $winner2, isStartPlayer: false }]->(g)`;

    if (body.league) {
      queryString += ` WITH u1, u2, pg1, pg2, g  
                       MATCH (l: League {league: $league}) 
                       MERGE (g)-[:IN_LEAGUE]->(l)`
    }
    queryString += " RETURN u1, u2, pg1, pg2, g";
    const res = (await Neo4j.query(queryString,
                                    {
                                      name1: body.players[0].name,
                                      name2: body.players[1].name,
                                      points1: body.players[0].score,
                                      winner1: body.gameWinner,
                                      points2: body.players[1].score,
                                      winner2: body.gameWinner,
                                      pointsToWin: body.pointsToWin,
                                      league: body.league
                                    }));
    return res;
  }

  async getLatestGame(username) {
   const res = (await Neo4j.query(`MATCH (u:User{username: $username})-[game:PLAYED_GAME]-(g:Game)-[:PLAYED_GAME]-(opponent:User) 
                                  RETURN game.winner AS winner, opponent.username AS opponent ORDER BY g.date DESC LIMIT 1`, {username: username}
   ));

   return res;
  }
}

module.exports = new Game()

