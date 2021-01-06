const Neo4j = require('../modules/neo4j')

class League {
  async getAll(){
    const res = (await Neo4j.query(`MATCH (l:League)
                                    RETURN l`, {}))
    return res.map(o => ({...o.l.properties}))
  }
}

module.exports = new League()

