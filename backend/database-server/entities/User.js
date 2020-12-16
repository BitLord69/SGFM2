const Neo4j = require('../modules/neo4j')

class User {
  async getAll(){
    const res = (await Neo4j.query(`MATCH (u:User)
                                    WITH u
                                    ORDER BY u.name
                                    RETURN u`, {}))
    return res.map//(o => ({ ...o.p.properties }))
  }
}

module.exports = new User()