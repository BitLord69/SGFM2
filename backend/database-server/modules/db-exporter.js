const fs = require('fs')
const { promisify } = require('util')
const exists = promisify(fs.exists)
const readFile = promisify(fs.readFile)
const writeFile = promisify(fs.writeFile)

const filename = "db-dump.cyp"
const apocFilename = "raw-db-dump.cyp"
const constraintsFilename = "db-dump-constraints.cyp"

async function exportDb(Neo4j) {
  await Neo4j.query(`CALL apoc.export.cypher.all("${apocFilename}", { cypherFormat: "updateAll", useOptimizations: {type: "UNWIND_BATCH", unwindBatchSize: 1000} })`)
  let data = await readFile(__dirname + '/' + apocFilename, 'utf8')
  
  if (!data) {
    return;
  }
  const constraintsSplit = data.split('CALL db.awaitIndexes(300);');
  data = constraintsSplit[constraintsSplit.length - 1];

  if (constraintsSplit.length > 1) {
    const constraints = constraintsSplit[0]
      .split(/\n/).slice(1, -2)
      .sort()
      .map(s => s.replace(';.*$', ';'))
      .filter(line => !line.includes('UNIQUE IMPORT'))
      .join('\n');

    await writeFile(__dirname + '/' + constraintsFilename, constraints, 'utf8')
  }

  let arrayStart = data.indexOf('UNWIND [') + 8
  let arrayEnd = data.indexOf('] AS row', arrayStart)

  while(arrayStart !== 7 && ~arrayEnd) {
    const toSort = data.slice(arrayStart, arrayEnd)
    const sorted = toSort
      .replace(/((?<!(, properties:))(?<!(start: ))(?<!(end: )){)/g, '___#___$1')
      .split('___#___')
      .filter(s=>s)
      .map(s=>s.replace(/,\s?$/, ''))
      .sort()
      .join(',\n')

    data = data.slice(0, arrayStart) + sorted + data.slice(arrayEnd)

    arrayStart = data.indexOf('UNWIND [', arrayEnd) + 8
    arrayEnd = arrayStart !== 7 ? data.indexOf('] AS row', arrayStart) : -1
  }

  await writeFile(__dirname + '/' + filename, data, 'utf8')
}

async function importConstraints(Neo4j) {
  if (!await exists(__dirname + '/' + constraintsFilename)) { return }

  const oldConstraints = (await Neo4j.query(`CALL db.constraints`))
    .map(obj => obj.description)
    .map(s => s.match(/CONSTRAINT ON \(.*:(.+)\) ASSERT \(.*\.(.+)\) IS UNIQUE/).slice(1,3))
    .map(arr => [arr[0].trim(), arr[1]])

  const data = await readFile(__dirname + '/' + constraintsFilename,'utf8')
  let constraints = data.replace(/\r/gm, '').split('\n')
  constraints = constraints.filter(line => !line.includes('UNIQUE IMPORT'))
  for (const oldC of oldConstraints) {
    const indexToRemove = constraints.findIndex(line => line.includes(oldC[0]) && line.includes(oldC[1]));
    if (~indexToRemove) {
      constraints.splice(indexToRemove, 1)
    }
  }

  const dropsToAdd = [];
  for (let c of constraints) {
    if (c.includes('CALL db.index.fulltext.createNodeIndex')) {
      const newString = c.replace(
        /createNodeIndex\(\'(.+?)\',.*$/,
        "drop('$1');"
      );
      dropsToAdd.push(newString);
    }
  }

  constraints.unshift(...dropsToAdd)

  const numNewUniqueConstraints = constraints.filter(s => s.includes('CREATE CONSTRAINT')).length
  numNewUniqueConstraints && console.log('Adding new constraints:', numNewUniqueConstraints)

  for await (let constraint of constraints) {
    await Neo4j.query(constraint).catch(_ => {console.log('error', constraint)})
  }

  const installUUIDsSequentially = async ()=>{
    // TODO after a few weeks, change to only new constraints
    for (const oldC of oldConstraints) {
      await Neo4j.query(`CALL apoc.uuid.install('${oldC[0]}') YIELD label RETURN label`).catch(_ => {console.log('error apoc install', oldC)});
    }
  }

  await installUUIDsSequentially();
}

async function importDb(Neo4j) {
  if (!await exists(__dirname + '/' + filename)) { return }
  await importConstraints(Neo4j);  

  const numDeleted = (await Neo4j.query(`MATCH (n) DETACH DELETE n RETURN count(n) AS numDeleted`))[0].numDeleted
  const results = await Neo4j.query(`CALL apoc.cypher.runFile("${filename}")`)

  const numCreated = results.map(result => result.result?.nodesCreated).reduce((prev, curr) => prev+curr, 0)
  const diff = (numCreated ?? 0) - numDeleted
  if (numCreated !== undefined && (numCreated !== numDeleted)) {
    console.log(`Deleted ${numDeleted} nodes. Imported ${numCreated} nodes. (${(diff >= 0 ? '+' : '-') + diff})`)
  }
}

module.exports = {import: importDb, export: exportDb}
