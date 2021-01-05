const Neo4j = require('../modules/neo4j')

class Game {
  async getAll(){
    const res = (await Neo4j.query(`MATCH (g:Game)
                                    RETURN g`, {}))
    return res.map//(o => ({ ...o.p.properties }))
  }

  async saveGame(body) {
    const res = (await Neo4j.query(`MATCH (u1: User{ username: $name1 }), (u2: User{ username: $name2 })
                                    MERGE (u1)-[pg1: PLAYED_GAME{ points: $points1, winner: $winner1, isStartPlayer: true }]->(g:Game)
                                    MERGE (u2)-[pg2: PLAYED_GAME{ points: $points2, winner: $winner2, isStartPlayer: false }]->(g)
                                    ON CREATE SET g.date = timestamp(), g.status = 'Finished', g.pointsToWin = $pointsToWin
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
    console.log("res i saveGame:", res);
    return res;
  }
}

module.exports = new Game()