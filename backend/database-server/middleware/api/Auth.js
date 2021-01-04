// const db = require("./utils/DBHelper").getInstance();
const router = require('express').Router()
const user = require('../../entities/User')

router.post("/login", async(req, res) => {

  if (req.session.user) {
    return res.json({ error: 'Already logged in!' });
  }

  const { email, password } = req.body;
  const result = await user.login(email, password);

  if (result === null) {
   return res.json({ error: 'Incorrect username or password!' })
  }

  // Log in by adding user to the session
  req.session.user = result;
  delete req.session.user.password
  return res.json(req.session.user);
});

router.post("/logout", (req, res) => {
  if (!req.session.user) {
    return res.json({ error: "Already logged out!" });
  }

  // Log out the user by deleting the session.user property
  delete req.session.user;
  res.json({ success: "Logged out!" });
});

router.get('/whoami', (req, res) => {
  res.json(req.session.user || { error: 'No user logged in!' });
});

module.exports = router