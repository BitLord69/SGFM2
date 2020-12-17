const router = require('express').Router()
const session = require('express-session')
const RedisStore = require('connect-redis')(session)
const redisClient = require('redis').createClient()
const secrets = require('../secrets.json');

redisClient.on('error', console.error);

const sessionMiddleware = session({
  store: new RedisStore({ client: redisClient }),
  secret: secrets.session_secrets,
  resave: false,
  saveUninitialized: true
})

router.use((req, res, next) => {
  let tries = 3

  function lookupSession(error) {
    if (error) {
      return next(error)
    }

    if (req.session !== undefined) {
      return next()
    }

    if (tries <= 0) {
      return next(new Error('Session middleware failed 3 times.'))
    } else if (tries < 3){
      console.warn(new Date().toLocaleString('sv-se'), 'session failed');
    }

    tries -= 1
    sessionMiddleware(req, res, lookupSession)
  }

  lookupSession()
})

module.exports = router;