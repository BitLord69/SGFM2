const Neo4j = require('../modules/neo4j')

class User {
  async getAll(){
    const res = (await Neo4j.query(`MATCH (u:User)
                                    WITH u
                                    ORDER BY u.name
                                    RETURN u`, {}))
    return res.map//(o => ({ ...o.p.properties }))
  }

  async login(email, password) {
    const res = (await Neo4j.query(`MATCH (u:User {email: $email, password: $password})
                                    RETURN u`, {email, password}))[0];
                                    console.log("res:", res);
    return res === undefined ? null : { ...res.u.properties }
  }

  async registerNewUser (username, email, password, avatar) {
    const res = (await Neo4j.query(`CREATE (u: User {username: $username, email: $email, password: $password,
                                    avatar: $avatar}) RETURN u`, {username, email, password, avatar}));
    console.log("res i registeruser", res);
    return res;
  }
}

module.exports = new User()