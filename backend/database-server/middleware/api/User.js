const router = require("express").Router();
const user = require("../../entities/User");

router.get("/", async (req, res) => {
  return res.json(await user.getAll());
});

router.post("/", async (req, res) => {
  const { username, email, password, avatar } = req.body;
  return res.json(
    await user.registerNewUser(username, email, password, avatar)
  );
});

router.post("/friends/", async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: "Not logged in!" });
  }
  return res.json(await user.createFriendRequest(req.session.user.username, req.body.friendname));
});

router.delete("/friends/", async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: "Not logged in!" });
  }
  return res.json(await user.deleteFriend(req.session.user.username, req.body.friendname));
});

router.get("/friends/", async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: "Not logged in!" });
  }
  return res.json(await user.getFriends(req.session.user.username));
});

router.get("/friends/requests/", async (req, res) => {
  if (!req.session.user) {
    return res.json({ error: "Not logged in!" });
  }
  return res.json(await user.getFriendRequests(req.session.user.username));
});

module.exports = router;
