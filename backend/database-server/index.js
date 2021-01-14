const Neo4j = require('./modules/neo4j')
const express = require('express')
const encryptor = require('./modules/PasswordEncryptor');
const app = express()
const server = require('http').createServer(app)
const path = require('path')
const secrets = require('./secrets.json')

//app.use(require('connect-favicons')(path.join(__dirname, '../../frontend/src/statics/icons')))
app.use(express.static(path.join(__dirname, '../../frontend/dist/spa'))) 
app.use(express.static(path.join(__dirname, 'uploads')))

app.use(express.json({ limit: '5mb' }))
app.use(express.urlencoded({ extended: true }))
app.use('*', encryptor({
  // Our settings for the encryptor
  salt: 'bestComputedDevelopersEVER3xp019Z'
}));
app.use(require('cors')({ origin: 'http://192.168.1.8:8080' }))
//app.use(require('cors')({ origin: 'http://98.128.140.50:8080' }))

app.use('*', require('./middleware/session'))
app.use('/api', require('./middleware/api'))

app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, '../../frontend/dist/spa/index.html'))
})

const shutdownServer = async (signal) => {
  if (this.shuttingDown) { return }
  this.shuttingDown = true
  console.log('\nShutting down due to', signal === 'SIGUSR2' ? 'file changes' : 'Ctrl+C')
  setTimeout(() => {
    console.log('Force shutting down...')
    process.exit(1)
  }, 10000)
  await Neo4j.close()
  process.exit(0)
}

const start = async ()=>{
  await Neo4j.connect(secrets.db_user, secrets.db_pass)
  console.log('Connected to Neo4j')
  const expressPort = 8070
  server.listen(expressPort, async () => {
    console.log('Backend listening on port', expressPort)
    console.timeEnd('Backend started in')
  }).on('close', shutdownServer)
}

// SIGINT on CTRL+C
process.on('SIGINT', _ => shutdownServer('SIGINT'))
// SIGUSR2 on nodemon restart
process.on('SIGUSR2', _ => shutdownServer('SIGUSR2'))

console.time('Backend started in')
start()