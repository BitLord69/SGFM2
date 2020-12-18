const router = require('express').Router()
const user = require('../../entities/User')

router.get('/', async (req, res) => {
  return res.json(await user.getAll());
})

router.post('/register', async(req, res) => {
  const { username, email, password, avatar } = req.body;
  const result = res.json(await user.registerNewUser(username, email, password, avatar))
  console.log("result:", result);
})


module.exports = router