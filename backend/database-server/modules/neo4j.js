const neo4jDriver = require('neo4j-driver')
const dbExporter = require('./db-exporter')
const ongoingSessions = []
let neoDriver
const neoPort = 7687

module.exports = class Neo4j {

  static async connect(username, password) {
    const credentials = neo4jDriver.auth.basic(username, password)
    const options = { disableLosslessIntegers: true }
    neoDriver = neo4jDriver.driver("bolt://localhost:" + neoPort, credentials, options)
    await neoDriver.verifyConnectivity()
    await dbExporter.import(this);
  }

  static #getSession = () => {
    const s = neoDriver.session()
    ongoingSessions.unshift(s)
    return s
  }
  static #closeSession = (s) => { ongoingSessions.splice(ongoingSessions.indexOf(s), 1)[0].close() }

  static async query(queryString, props = {}) {
    const session = this.#getSession()
    let result
    try {
      result = await session.writeTransaction(tx => tx.run(queryString, props))
      result = result.records.map(obj => obj.toObject())
    } finally { this.#closeSession(session) }
    return result
  }

  static async tryClose(retriesLeft = 5000) {
    if (ongoingSessions.length && retriesLeft) {
      await new Promise(res => setTimeout(res, 1))
      return this.tryClose(retriesLeft - 1)
    }
  }

  static async close(retriesLeft = 5000) {
    await dbExporter.export(this);
    // Let ongoing queries finish (max 5s)
    await this.tryClose(retriesLeft)
    neoDriver && await neoDriver.close()
  }
}
