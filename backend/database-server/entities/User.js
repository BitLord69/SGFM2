const Neo4j = require("../modules/neo4j");

class User {
  async getAll() {
    const res = await Neo4j.query(
      `MATCH (u:User) WITH u ORDER BY u.username RETURN u`,
      {}
    );
    return res.map(o => ({ ...o.u.properties }))
  }

  async login(email, password) {
    const res = (
      await Neo4j.query(
        `MATCH (u:User {email: $email, password: $password})
                                    RETURN u`,
        { email, password }
      )
    )[0];
    return res === undefined ? null : { ...res.u.properties };
  }

  async registerNewUser(username, email, password, avatar) {
    const res = await Neo4j.query(
      `CREATE (u: User {username: $username, email: $email, password: $password,
                                    avatar: $avatar}) RETURN u`,
      { username, email, password, avatar }
    );
    return res;
  }

  async getFriends(username) {
    const res = await Neo4j.query(
      `MATCH (u: User {username: $username})-[f:FRIENDS]-(friend:User) WHERE COALESCE(f.pendingRequest, false) <> true RETURN friend ORDER BY friend.username`,
      { username }
    );

    return res === undefined
      ? null
      : res
          .map((o) => ({ ...o.friend.properties }))
          .map((o) => {
            delete o.password;
            return o;
          });
  }

  async getNonFriends(username) {
    const res = await Neo4j.query(
      `MATCH (nf:User) WHERE NOT (nf)-[:FRIENDS]-(:User {username: $username}) AND nf.username<>$username RETURN nf ORDER BY nf.username`,
      { username }
    );
    return res.map(o => ({ ...o.nf.properties }))
  }

  async getIncomingFriendRequests(username) {
    const res = await Neo4j.query(
      `MATCH (u: User {username: $username})<-[f:FRIENDS {pendingRequest:true}]-(friend:User) RETURN friend ORDER BY friend.username`,
      { username }
    );
    return res === undefined
      ? null
      : res
          .map((o) => ({ ...o.friend.properties }))
          .map((o) => {
            delete o.password;
            return o;
          });
  }

  async getOutgoingFriendRequests(username) {
    const res = await Neo4j.query(
      `MATCH (friend:User)<-[f:FRIENDS {pendingRequest:true}]-(u: User {username: $username}) RETURN friend ORDER BY friend.username`,
      { username }
    );
    return res === undefined
      ? null
      : res
          .map((o) => ({ ...o.friend.properties }))
          .map((o) => {
            delete o.password;
            return o;
          });
  }

  // async acceptFriendRequest(username, friendname) {
  //   const res = await Neo4j.query(
  //     `MATCH (u: User {username: $username}),(friend:User {username: $friendname})
  //     MERGE (u)<-[f:FRIENDS]->(friend)
  //     RETURN u, friend, f`,
  //     { username, friendname }
  //     );
  //     return res;
  //   }

  async createOrAcceptFriendRequest(username, friendname) {
    const res = await Neo4j.query(
      `MATCH (u: User {username: $username}),(friend:User {username: $friendname})
        MERGE (u)<-[f:FRIENDS]->(friend)
        ON CREATE SET f.pendingRequest = true
        ON MATCH SET f.pendingRequest = false
        RETURN u, friend, f`,
      { username, friendname }
    );
    return res;
  }

  async deleteFriend(username, friendname) {
    const res = await Neo4j.query(
      `MATCH (u: User {username: $username})-[f:FRIENDS]-(friend:User {username: $friendname}) DETACH DELETE f`,
      { username, friendname }
    );
    return res;
  }
}

module.exports = new User();
