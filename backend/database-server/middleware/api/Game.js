const router = require('express').Router()
const game = require('../../entities/Game')

router.get('/', async (req, res) => {
  return res.json(await game.getAll());
})

router.post('/', async (req, res) => {
  console.log("game.save, body: ", req.body);
  return res.json(await game.saveGame(req.body));
})

module.exports = router