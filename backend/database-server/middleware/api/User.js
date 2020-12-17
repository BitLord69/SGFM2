const router = require('express').Router()
const user = require('../../entities/User')

router.get('/', async (req, res) => {
  return res.json(await user.getAll());
})

module.exports = router