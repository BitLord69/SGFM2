const router = require('express').Router()
const user = require('../../entities/User')

router.get('/', async (req, res) => {
  return res.json(await user.getAll());
})

router.post('/register', async(req, res) => {
  const { username, email, password, avatar } = req.body;
  return res.json(await user.registerNewUser(username, email, password, avatar))
})


module.exports = router