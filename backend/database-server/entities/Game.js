const Neo4j = require('../modules/neo4j')

class Game {
  async getAll(username){
    const res = (await Neo4j.query(`MATCH (u:User{username: $username})-[p:PLAYED_GAME]->(g:Game)
                                    RETURN p, g`, {username: username}))
    return res.map(o => ({...o.g.properties, ...o.p.properties}))
  }

  async getLeaderboard(){
    const res = (await Neo4j.query(`MATCH (u:User)-[p:PLAYED_GAME]->(:Game) RETURN u{.*,games:collect(p)}`,{}))
    const temp = res.map(o => (
                          {avatar: o.u.avatar,
                          username: o.u.username,
                          wins: o.u.games.filter(g => g.properties.winner).length,
                          losses: o.u.games.filter(g => !g.properties.winner).length
                          }))

    return temp.sort((a, b) => (b.wins - b.losses) - (a.wins - a.losses))
  }

  async saveGame(body) {
    const res = (await Neo4j.query(`MATCH (u1: User{ username: $name1 }), (u2: User{ username: $name2 })
                                    CREATE (u1)-[pg1: PLAYED_GAME{ points: $points1, winner: $winner1, isStartPlayer: true }]->(g:Game{date: timestamp(), status: 'Finished', pointsToWin: $pointsToWin})
                                    MERGE (u2)-[pg2: PLAYED_GAME{ points: $points2, winner: $winner2, isStartPlayer: false }]->(g)
                                    RETURN u1, u2, pg1, pg2, g`,
                                    {
                                      name1: body.players[0].name,
                                      name2: body.players[1].name,
                                      points1: body.players[0].score,
                                      winner1: body.gameWinner == '0',
                                      points2: body.players[1].score,
                                      winner2: body.gameWinner == '1',
                                      pointsToWin: body.pointsToWin
                                    }));
    return res;
  }
}

module.exports = new Game()
