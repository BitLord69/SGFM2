const router = require('express').Router()
const game = require('../../entities/Game')

router.get('/', async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: 'Not logged in!' });
  }  
  return res.json(await game.getAll(req.session.user.username));
})

router.get('/leaderboard', async (req, res) => { 
  return res.json(await game.getLeaderboard());
})

router.post('/', async (req, res) => {
  console.log("game.save, body: ", req.body);
  return res.json(await game.saveGame(req.body));
})

module.exports = router