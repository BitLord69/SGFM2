const router = require('express').Router()
const game = require('../../entities/Game')

router.get('/:league', async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: 'Not logged in!' });
  }  
  return res.json(await game.getAll(req.session.user.username, req.params.league));
})

router.get('/leaderboard/:league', async (req, res) => { 
  return res.json(await game.getLeaderboard(req.params.league));
})

router.post('/', async (req, res) => {
  console.log("game.save, body: ", req.body);
  return res.json(await game.saveGame(req.body));
})

module.exports = router