const router = require('express').Router()
const league = require('../../entities/League')

router.get('/', async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: 'Not logged in!' });
  }  
  return res.json(await league.getAll());
})

module.exports = router
